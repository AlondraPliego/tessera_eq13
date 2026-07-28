package com.TESSERA.Eq13Tessera.compras.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

/**
 * Habla con seatmap.pro para:
 *  1) Crear un "evento" en seatmap.pro por cada función/fecha nuestra.
 *  2) Crear un precio y pintarlo sobre la zona correspondiente (para que el
 *     mapa se vea coloreado con el precio de cada sección).
 *
 * Si todavía no configuraron sus credenciales de seatmap.pro, este servicio
 * NO truena: solo se salta la parte de seatmap.pro y deja todo lo demás
 * (evento, zonas, boletos) funcionando normal en nuestra base de datos.
 */
@Service
public class SeatmapService {

    private static final Logger log = LoggerFactory.getLogger(SeatmapService.class);

    private final SeatmapClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SeatmapService(SeatmapClient client) {
        this.client = client;
    }

    /**
     * Crea el "evento" en seatmap.pro para una función/fecha, ligado al schema del recinto.
     * Regresa el eventId de seatmap.pro (vacío si no se pudo, ej. sin credenciales).
     */
    public Optional<String> crearEventoParaFecha(Long schemaId, String nombreEvento, LocalDate fecha, LocalTime hora) {
        if (schemaId == null) {
            log.info("El recinto no tiene seatmapSchemaId configurado, se omite la creación en seatmap.pro");
            return Optional.empty();
        }

        String inicio = fecha.atTime(hora).toString();
        String fin = fecha.atTime(hora).plusHours(3).toString(); // duración estimada, editable después

        ObjectNode body = objectMapper.createObjectNode();
        body.putNull("id");
        body.putNull("createdDate");
        body.put("start", inicio);
        body.put("endDate", fin);
        body.put("name", nombreEvento);
        body.put("schemaId", schemaId);

        return client.enviar("POST", "/api/private/v2.0/events/", body.toString())
                .flatMap(this::extraerId);
    }

    /**
     * Crea un precio para el evento y lo asigna a la zona (sección) indicada,
     * para que el mapa se pinte con ese color/precio.
     */
    public void crearYAsignarPrecio(String seatmapEventId, Long seatmapObjectId, BigDecimal precio) {
        if (seatmapEventId == null || seatmapObjectId == null) {
            log.info("Falta seatmapEventId o seatmapObjectId, se omite el pintado de esta zona en seatmap.pro");
            return;
        }

        // 1) Crear el precio
        ObjectNode price = objectMapper.createObjectNode();
        price.putNull("id");
        price.put("name", precio.toPlainString());
        price.put("eventId", seatmapEventId);
        price.putNull("externalId");
        ArrayNode listaPrecios = objectMapper.createArrayNode().add(price);

        Optional<String> respuestaPrecio = client.enviar(
                "POST", "/api/private/v2.0/event/" + seatmapEventId + "/prices/", listaPrecios.toString());

        Long priceId = respuestaPrecio.flatMap(this::extraerIdNumericoDeArray).orElse(null);
        if (priceId == null) {
            log.info("No se pudo crear el precio en seatmap.pro para el evento {}", seatmapEventId);
            return;
        }

        // 2) Asignar ese precio a la zona (sección completa = "groupOfSeats")
        ObjectNode grupo = objectMapper.createObjectNode();
        grupo.put("objectId", seatmapObjectId);
        grupo.put("assignmentId", priceId);
        grupo.putNull("activeCount");

        ObjectNode seleccion = objectMapper.createObjectNode();
        seleccion.set("seats", objectMapper.createArrayNode());
        seleccion.set("groupOfSeats", objectMapper.createArrayNode().add(grupo));

        client.enviar("POST", "/api/private/v2.0/event/" + seatmapEventId + "/prices/assignments/", seleccion.toString());
    }

    /** Borra el evento de seatmap.pro (ej. cuando se elimina el evento en Tessera) */
    public void eliminarEvento(String seatmapEventId) {
        if (seatmapEventId == null) return;
        client.enviar("DELETE", "/api/private/v2.0/events/" + seatmapEventId, null);
    }

    // --- helpers para leer las respuestas JSON ---

    private Optional<String> extraerId(String json) {
        try {
            JsonNode nodo = objectMapper.readTree(json);
            return Optional.ofNullable(nodo.get("id")).map(JsonNode::asText);
        } catch (Exception e) {
            log.error("No se pudo leer la respuesta de seatmap.pro: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<Long> extraerIdNumericoDeArray(String json) {
        try {
            JsonNode nodo = objectMapper.readTree(json);
            if (nodo.isArray() && !nodo.isEmpty()) {
                return Optional.ofNullable(nodo.get(0).get("id")).map(JsonNode::asLong);
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("No se pudo leer la respuesta de seatmap.pro: {}", e.getMessage());
            return Optional.empty();
        }
    }
}

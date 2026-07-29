package com.TESSERA.Eq13Tessera.compras.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

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

        // Campos según el modelo "Event" real de la API: name, schemaId, startDate, endDate
        ObjectNode body = objectMapper.createObjectNode();
        body.put("name", nombreEvento);
        body.put("schemaId", schemaId);
        body.put("startDate", inicio);
        body.put("endDate", fin);

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

        // 1) Crear el precio. Campos según el modelo "Price" real: name, price, currency
        ObjectNode price = objectMapper.createObjectNode();
        price.put("name", "$" + precio.toPlainString());
        price.put("price", precio.doubleValue());
        price.put("currency", "MXN");
        ArrayNode listaPrecios = objectMapper.createArrayNode().add(price);

        Optional<String> respuestaPrecio = client.enviar(
                "POST", "/api/private/v2.0/event/" + seatmapEventId + "/prices/", listaPrecios.toString());

        Long priceId = respuestaPrecio.flatMap(this::extraerIdNumericoDeArray).orElse(null);
        if (priceId == null) {
            log.info("No se pudo crear el precio en seatmap.pro para el evento {}", seatmapEventId);
            return;
        }

        // 2) Asignar ese precio a la zona (sección completa).
        // objectId = el id de la zona/sección dentro del mapa; assignmentId = el id del precio que acabamos de crear.
        ObjectNode asignacion = objectMapper.createObjectNode();
        asignacion.put("objectId", seatmapObjectId);
        asignacion.put("assignmentId", priceId);
        asignacion.putNull("activeCount");
        ArrayNode listaAsignaciones = objectMapper.createArrayNode().add(asignacion);

        client.enviar("POST", "/api/private/v2.0/event/" + seatmapEventId + "/selection/", listaAsignaciones.toString());
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

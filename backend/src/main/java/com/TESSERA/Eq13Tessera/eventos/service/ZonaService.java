package com.TESSERA.Eq13Tessera.eventos.service;

import com.TESSERA.Eq13Tessera.common.exception.OperacionNoPermitidaException;
import com.TESSERA.Eq13Tessera.common.exception.ResourceNotFoundException;
import com.TESSERA.Eq13Tessera.eventos.dto.ZonaRequest;
import com.TESSERA.Eq13Tessera.eventos.dto.ZonaResponse;
import com.TESSERA.Eq13Tessera.eventos.entity.Recinto;
import com.TESSERA.Eq13Tessera.eventos.entity.Zona;
import com.TESSERA.Eq13Tessera.eventos.repository.RecintoRepository;
import com.TESSERA.Eq13Tessera.eventos.repository.ZonaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZonaService {

    private final ZonaRepository zonaRepository;
    private final RecintoRepository recintoRepository;

    public ZonaService(ZonaRepository zonaRepository, RecintoRepository recintoRepository) {
        this.zonaRepository = zonaRepository;
        this.recintoRepository = recintoRepository;
    }

    public List<ZonaResponse> listarPorRecinto(Long recintoId) {
        buscarRecintoOFallar(recintoId); // valida que el recinto exista
        return zonaRepository.findByRecintoId(recintoId).stream().map(this::toResponse).toList();
    }

    public ZonaResponse crear(Long recintoId, ZonaRequest dto, Long empresaId) {
        Recinto recinto = buscarRecintoOFallar(recintoId);
        validarPropietario(recinto, empresaId);

        Zona zona = new Zona();
        zona.setRecintoId(recintoId);
        zona.setNombre(dto.getNombre());
        zona.setCapacidad(dto.getCapacidad());
        zona.setColor(dto.getColor());
        zona.setCoordenadas(dto.getCoordenadas());
        zona.setSeatmapObjectId(dto.getSeatmapObjectId());
        return toResponse(zonaRepository.save(zona));
    }

    public ZonaResponse actualizar(Long zonaId, ZonaRequest dto, Long empresaId) {
        Zona zona = buscarZonaOFallar(zonaId);
        Recinto recinto = buscarRecintoOFallar(zona.getRecintoId());
        validarPropietario(recinto, empresaId);

        zona.setNombre(dto.getNombre());
        zona.setCapacidad(dto.getCapacidad());
        zona.setColor(dto.getColor());
        zona.setCoordenadas(dto.getCoordenadas());
        zona.setSeatmapObjectId(dto.getSeatmapObjectId());
        return toResponse(zonaRepository.save(zona));
    }

    public void eliminar(Long zonaId, Long empresaId) {
        Zona zona = buscarZonaOFallar(zonaId);
        Recinto recinto = buscarRecintoOFallar(zona.getRecintoId());
        validarPropietario(recinto, empresaId);
        zonaRepository.delete(zona);
    }

    // --- helpers ---

    private Recinto buscarRecintoOFallar(Long recintoId) {
        return recintoRepository.findById(recintoId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un recinto con id " + recintoId));
    }

    private Zona buscarZonaOFallar(Long zonaId) {
        return zonaRepository.findById(zonaId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe una zona con id " + zonaId));
    }

    private void validarPropietario(Recinto recinto, Long empresaId) {
        if (!recinto.getEmpresaId().equals(empresaId)) {
            throw new OperacionNoPermitidaException("Esa zona no pertenece a un recinto de tu empresa");
        }
    }

    private ZonaResponse toResponse(Zona z) {
        return new ZonaResponse(
                z.getId(), z.getRecintoId(), z.getNombre(), z.getCapacidad(), z.getColor(), z.getCoordenadas(),
                z.getSeatmapObjectId());
    }
}

package com.TESSERA.Eq13Tessera.eventos.service;

import com.TESSERA.Eq13Tessera.common.exception.OperacionNoPermitidaException;
import com.TESSERA.Eq13Tessera.common.exception.ResourceNotFoundException;
import com.TESSERA.Eq13Tessera.eventos.dto.RecintoRequest;
import com.TESSERA.Eq13Tessera.eventos.dto.RecintoResponse;
import com.TESSERA.Eq13Tessera.eventos.entity.Recinto;
import com.TESSERA.Eq13Tessera.eventos.repository.RecintoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RecintoService {

    private final RecintoRepository recintoRepository;

    public RecintoService(RecintoRepository recintoRepository) {
        this.recintoRepository = recintoRepository;
    }

    // Lista todos los recintos, con filtro opcional por nombre, paginado
    public Page<RecintoResponse> listar(String nombre, Pageable pageable) {
        Page<Recinto> pagina = StringUtils.hasText(nombre)
                ? recintoRepository.findByNombreContainingIgnoreCase(nombre, pageable)
                : recintoRepository.findAll(pageable);
        return pagina.map(this::toResponse);
    }

    // Lista solo los recintos de UNA empresa (para su panel/dashboard)
    public Page<RecintoResponse> listarPorEmpresa(Long empresaId, Pageable pageable) {
        return recintoRepository.findByEmpresaId(empresaId, pageable).map(this::toResponse);
    }

    public RecintoResponse obtenerPorId(Long id) {
        return toResponse(buscarOFallar(id));
    }

    public RecintoResponse crear(RecintoRequest dto, Long empresaId) {
        Recinto recinto = new Recinto();
        recinto.setNombre(dto.getNombre());
        recinto.setDireccion(dto.getDireccion());
        recinto.setMapaSvg(dto.getMapaSvg());
        recinto.setEmpresaId(empresaId);
        recinto.setSeatmapSchemaId(dto.getSeatmapSchemaId());
        return toResponse(recintoRepository.save(recinto));
    }

    public RecintoResponse actualizar(Long id, RecintoRequest dto, Long empresaId) {
        Recinto recinto = buscarOFallar(id);
        validarPropietario(recinto, empresaId);

        recinto.setNombre(dto.getNombre());
        recinto.setDireccion(dto.getDireccion());
        recinto.setMapaSvg(dto.getMapaSvg());
        recinto.setSeatmapSchemaId(dto.getSeatmapSchemaId());
        return toResponse(recintoRepository.save(recinto));
    }

    public void eliminar(Long id, Long empresaId) {
        Recinto recinto = buscarOFallar(id);
        validarPropietario(recinto, empresaId);
        recintoRepository.delete(recinto);
    }

    // --- helpers ---

    private Recinto buscarOFallar(Long id) {
        return recintoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un recinto con id " + id));
    }

    private void validarPropietario(Recinto recinto, Long empresaId) {
        if (!recinto.getEmpresaId().equals(empresaId)) {
            throw new OperacionNoPermitidaException("Este recinto no pertenece a tu empresa");
        }
    }

    private RecintoResponse toResponse(Recinto r) {
        return new RecintoResponse(
                r.getId(), r.getNombre(), r.getDireccion(), r.getMapaSvg(), r.getEmpresaId(), r.getCreatedAt(),
                r.getSeatmapSchemaId());
    }
}

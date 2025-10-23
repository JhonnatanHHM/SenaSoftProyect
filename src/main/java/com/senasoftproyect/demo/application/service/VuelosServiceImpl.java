package com.senasoftproyect.demo.application.service;

import com.senasoftproyect.demo.application.dtos.*;
import com.senasoftproyect.demo.domain.entitys.*;
import com.senasoftproyect.demo.domain.repository.*;
import com.senasoftproyect.demo.domain.service.AerolineasService;
import com.senasoftproyect.demo.domain.service.VuelosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VuelosServiceImpl implements VuelosService {

    private final AerolineasService aerolineasService;
    private final VuelosRepository vuelosRepository;
    private final CiudadesRepository ciudadesRepository;
    private final AerolineasRepository aerolineasRepository;
    private final AvionesRepository avionesRepository;

    @Autowired
    public VuelosServiceImpl(AerolineasService aerolineasService, VuelosRepository vuelosRepository,
                             CiudadesRepository ciudadesRepository,
                             AerolineasRepository aerolineasRepository,
                             AvionesRepository avionesRepository) {
        this.aerolineasService = aerolineasService;
        this.vuelosRepository = vuelosRepository;
        this.ciudadesRepository = ciudadesRepository;
        this.aerolineasRepository = aerolineasRepository;
        this.avionesRepository = avionesRepository;
    }

    @Override
    public List<VuelosCompleteDTO> getAll() {
        return vuelosRepository.getAll()
                .stream()
                .map(this::convertToCompleteDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<VuelosCompleteDTO> getByIdVuelo(Long idVuelo) {
        return vuelosRepository.getByIdVuelo(idVuelo)
                .map(this::convertToCompleteDto);
    }

    @Transactional
    @Override
    public VuelosDTO save(VuelosDTO dto) {
        VuelosEntity entity = convertFromDto(dto);
        VuelosEntity saved = vuelosRepository.save(entity);
        return convertToDto(saved);
    }

    @Transactional
    @Override
    public VuelosDTO update(VuelosDTO dto) {
        VuelosEntity entity = vuelosRepository.getByIdVuelo(dto.getIdVuelo())
                .orElseThrow(() -> new RuntimeException("Vuelo no encontrado con id: " + dto.getIdVuelo()));

        entity.setHoraSalida(dto.getHoraSalida());
        entity.setHoraLlegada(dto.getHoraLlegada());
        entity.setLugarSalida(dto.getLugarSalida());
        entity.setLugarLlegada(dto.getLugarLlegada());

        CiudadesEntity ciudadSalida = ciudadesRepository.getByIdCiudad(dto.getIdCiudadSalida())
                .orElseThrow(() -> new RuntimeException("Ciudad de salida no encontrada"));
        CiudadesEntity ciudadLlegada = ciudadesRepository.getByIdCiudad(dto.getIdCiudadLlegada())
                .orElseThrow(() -> new RuntimeException("Ciudad de llegada no encontrada"));
        AerolineasEntity aerolinea = aerolineasRepository.getByIdAerolinea(dto.getIdAerolinea())
                .orElseThrow(() -> new RuntimeException("Aerolinea no encontrada"));
        AvionesEntity avion = avionesRepository.getByIdAvion(dto.getIdAvion())
                .orElseThrow(() -> new RuntimeException("Avion no encontrado"));

        entity.setCiudadSalida(ciudadSalida);
        entity.setCiudadLlegada(ciudadLlegada);
        entity.setAerolinea(aerolinea);
        entity.setAvion(avion);

        VuelosEntity updated = vuelosRepository.update(entity);
        return convertToDto(updated);
    }

    @Transactional
    @Override
    public void delete(Long idVuelo) {
        vuelosRepository.delete(idVuelo);
    }


    public VuelosCompleteDTO convertToCompleteDto(VuelosEntity entity) {

        List<AsientosDTO> asientosDto = entity.getAvion().getAsientos()
                .stream()
                .map(asiento -> new AsientosDTO(
                        asiento.getIdAsiento(),
                        asiento.getNombre(),
                        asiento.getPrecio(),
                        asiento.getEstado(),
                        asiento.getAvion().getIdAvion(),
                        asiento.getUsuarioReservado()
                ))
                .collect(Collectors.toList());

        AerolineasCompleteDTO aerolineaDto = null;
        try {
            aerolineaDto = aerolineasService.getById(entity.getAerolinea().getIdAerolinea());
        } catch (Exception e) {
            System.err.println("Error obteniendo Aerolínea completa: " + e.getMessage());
        }

        return new VuelosCompleteDTO(
                entity.getIdVuelo(),
                new CiudadesDTO(
                        entity.getCiudadSalida().getIdCiudad(),
                        entity.getCiudadSalida().getNombre(),
                        entity.getCiudadSalida().getRegion().getIdRegion(),
                        entity.getCiudadSalida().getRegion().getNombre()
                ),
                new CiudadesDTO(
                        entity.getCiudadLlegada().getIdCiudad(),
                        entity.getCiudadLlegada().getNombre(),
                        entity.getCiudadLlegada().getRegion().getIdRegion(),
                        entity.getCiudadLlegada().getRegion().getNombre()
                ),
                entity.getHoraSalida(),
                entity.getHoraLlegada(),
                entity.getLugarSalida(),
                entity.getLugarLlegada(),
                aerolineaDto,
                new AvionesDTO(
                        entity.getAvion().getIdAvion(),
                        entity.getAvion().getModelo(),
                        entity.getAvion().getCapacidad(),
                        asientosDto
                )
        );
    }

    private VuelosEntity convertFromDto(VuelosDTO dto) {
        VuelosEntity vuelo = new VuelosEntity();
        vuelo.setHoraSalida(dto.getHoraSalida());
        vuelo.setHoraLlegada(dto.getHoraLlegada());
        vuelo.setLugarSalida(dto.getLugarSalida());
        vuelo.setLugarLlegada(dto.getLugarLlegada());

        ciudadesRepository.getByIdCiudad(dto.getIdCiudadSalida())
                .ifPresent(vuelo::setCiudadSalida);
        ciudadesRepository.getByIdCiudad(dto.getIdCiudadLlegada())
                .ifPresent(vuelo::setCiudadLlegada);
        aerolineasRepository.getByIdAerolinea(dto.getIdAerolinea())
                .ifPresent(vuelo::setAerolinea);
        avionesRepository.getByIdAvion(dto.getIdAvion())
                .ifPresent(vuelo::setAvion);

        return vuelo;
    }

    private VuelosDTO convertToDto(VuelosEntity entity) {
        VuelosDTO dto = new VuelosDTO();
        dto.setIdVuelo(entity.getIdVuelo());
        dto.setHoraSalida(entity.getHoraSalida());
        dto.setHoraLlegada(entity.getHoraLlegada());
        dto.setLugarSalida(entity.getLugarSalida());
        dto.setLugarLlegada(entity.getLugarLlegada());
        dto.setIdCiudadSalida(entity.getCiudadSalida().getIdCiudad());
        dto.setIdCiudadLlegada(entity.getCiudadLlegada().getIdCiudad());
        dto.setIdAerolinea(entity.getAerolinea().getIdAerolinea());
        dto.setIdAvion(entity.getAvion().getIdAvion());
        return dto;
    }
}

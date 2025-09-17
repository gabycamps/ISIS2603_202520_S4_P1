package co.edu.uniandes.dse.parcial1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcial1.entities.UbicacionBodegaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.UbicacionBodegaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UbicacionBodegaService {

    @Autowired
    UbicacionBodegaRepository ubicacionBodegaRepository;

    @Transactional
    public List<UbicacionBodegaEntity> getUbicaciones(){
        log.info("Inicia proceso de consultar todas las ubicaciones de las bodegas");
        return ubicacionBodegaRepository.findAll();
    }

    @Transactional
    public UbicacionBodegaEntity createUbicacion(UbicacionBodegaEntity ubicacion) throws IllegalOperationException {
        log.info("Inicia proceso de creación de una nueva ubicación");

        if (ubicacion.getNumeroEstante() == null || ubicacion.getNumeroEstante() > 0) {
            throw new IllegalOperationException("El número del estante no puede ser nulo o negativo");
        }

        return ubicacionBodegaRepository.save(ubicacion);
    }

    @Transactional
    public UbicacionBodegaEntity getUbicacion(Long ubicacionId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la ubicación con id = {0}", ubicacionId);
        Optional<UbicacionBodegaEntity> ubicacionEntity = ubicacionBodegaRepository.findById(ubicacionId);

        if (ubicacionEntity.isEmpty())
            throw new EntityNotFoundException("La ubicación con el id solicitado no fue encontrada");

        log.info("Termina proceso de consultar la ubicación con id = {0}", ubicacionId);
        return ubicacionEntity.get();
    }


    @Transactional
    public UbicacionBodegaEntity updateUbicacion(Long ubicacionId, UbicacionBodegaEntity ubicacionConNuevosDatos) throws EntityNotFoundException, IllegalOperationException{
        
        log.info("Inicia proceso de actualizar el piloto con id = {}", ubicacionId);

        UbicacionBodegaEntity ubicacionExistente = ubicacionBodegaRepository.findById(ubicacionId)
                .orElseThrow(() -> new EntityNotFoundException("La mercancía con el id solicitado no fue encontrada"));

        if (ubicacionConNuevosDatos.getCanasta() == null) {
            throw new IllegalOperationException("La canasta de la bodega no puede ser nulo o vacío");
        }

        if (ubicacionConNuevosDatos.getNumeroEstante() == null || ubicacionConNuevosDatos.getNumeroEstante() > 0) {
            throw new IllegalOperationException("El número del estante no puede ser nulo o negativo");
        }

        ubicacionExistente.setCanasta(ubicacionConNuevosDatos.getCanasta());
        ubicacionExistente.setNumeroEstante(ubicacionConNuevosDatos.getNumeroEstante());
        ubicacionExistente.setPesoMax(ubicacionConNuevosDatos.getPesoMax());

        log.info("Termina proceso de actualizar la ubicación con id = {}", ubicacionId);
        return ubicacionBodegaRepository.save(ubicacionExistente);
    }

    @Transactional
    public void deleteUbicacion(Long ubicacionId) throws IllegalOperationException, EntityNotFoundException{
        log.info("Inicia proceso de borrar la mercancía con id = {0}", ubicacionId);
        Optional < UbicacionBodegaEntity > ubicacionEntity = ubicacionBodegaRepository.findById(ubicacionId);

        if(ubicacionEntity.isEmpty())
            throw new EntityNotFoundException("La ubicación con el id solicitado no fue encontrada");
        ubicacionBodegaRepository.deleteById(ubicacionId);
        log.info("Termina proceso de borrar ubicación con id = {0}", ubicacionId);
    }
}

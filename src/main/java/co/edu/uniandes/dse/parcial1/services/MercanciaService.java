package co.edu.uniandes.dse.parcial1.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcial1.entities.MercanciaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.MercanciaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MercanciaService {
    
    @Autowired
    MercanciaRepository mercanciaRepository;

    @Transactional
    public List<MercanciaEntity> getMercancias(){
        log.info("Inicia proceso de consultar todas las mercancías");
        return mercanciaRepository.findAll();
    }

    @Transactional
    public MercanciaEntity createMercancia(MercanciaEntity mercancia) throws IllegalOperationException {
        log.info("Inicia proceso de creación de una nueva mercancía");

        if (mercancia.getNombre() == null || mercancia.getNombre().isEmpty()) {
            throw new IllegalOperationException("El nombre de la mercancía no puede ser nulo o vacío");
        }
        
        if (mercancia.getCodigoBarras() == null) {
            throw new IllegalOperationException("El código de barras debe ser único");
        }

        if ((mercancia.getFechaRecepcion()).isAfter(LocalDateTime.now())) {
            throw new IllegalOperationException("La fecha no puede ser vencida");
        }

        return mercanciaRepository.save(mercancia);
    }

    @Transactional
    public MercanciaEntity getMercancia(Long mercanciaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la mercancía con id = {0}", mercanciaId);
        Optional<MercanciaEntity> mercanciaEntity = mercanciaRepository.findById(mercanciaId);

        if (mercanciaEntity.isEmpty())
            throw new EntityNotFoundException("La mercancía con el id solicitado no fue encontrada");

        log.info("Termina proceso de consultar la mercancía con id = {0}", mercanciaId);
        return mercanciaEntity.get();
    }


    @Transactional
    public MercanciaEntity updateMercancia(Long mercanciaId, MercanciaEntity mercanciaConNuevosDatos) throws EntityNotFoundException, IllegalOperationException{
        
        log.info("Inicia proceso de actualizar la mercancía con id = {}", mercanciaId);

        MercanciaEntity mercanciaExistente = mercanciaRepository.findById(mercanciaId)
                .orElseThrow(() -> new EntityNotFoundException("La mercancía con el id solicitado no fue encontrada"));

        if (mercanciaConNuevosDatos.getNombre() == null || mercanciaConNuevosDatos.getNombre().isEmpty()) {
            throw new IllegalOperationException("El nombre de la mercancía no puede ser nulo o vacío");
        }

        if (mercanciaConNuevosDatos.getCantidad() == null || mercanciaConNuevosDatos.getCantidad() < 0) {
            throw new IllegalOperationException("La cantidad de mercancía disponible no puede ser nula o negativa");
        }

        if (mercanciaConNuevosDatos.getCodigoBarras() == null) {
            throw new IllegalOperationException("El código de barras debe ser único");
        }


        if ((mercanciaConNuevosDatos.getFechaRecepcion()).isAfter(LocalDateTime.now())) {
            throw new IllegalOperationException("La fecha no puede ser vencida");
        }

        mercanciaExistente.setNombre(mercanciaConNuevosDatos.getNombre());
        mercanciaExistente.setCantidad(mercanciaConNuevosDatos.getCantidad());
        mercanciaExistente.setFechaRecepcion(mercanciaConNuevosDatos.getFechaRecepcion());
        mercanciaExistente.setCodigoBarras(mercanciaConNuevosDatos.getCodigoBarras());

        log.info("Termina proceso de actualizar la mercancía con id = {}", mercanciaId);
        return mercanciaRepository.save(mercanciaExistente);
    }

    @Transactional
    public void deleteMercancia(Long mercanciaId) throws IllegalOperationException, EntityNotFoundException{
        log.info("Inicia proceso de borrar la mercancía con id = {0}", mercanciaId);
        Optional < MercanciaEntity > mercanciaEntity = mercanciaRepository.findById(mercanciaId);

        if(mercanciaEntity.isEmpty())
            throw new EntityNotFoundException("La mercancía con el id solicitado no fue encontrada");
        mercanciaRepository.deleteById(mercanciaId);
        log.info("Termina proceso de borrar mercancía con id = {0}", mercanciaId);
    }
}

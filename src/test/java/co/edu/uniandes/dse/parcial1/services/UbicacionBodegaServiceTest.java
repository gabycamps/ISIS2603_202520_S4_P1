package co.edu.uniandes.dse.parcial1.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcial1.entities.MercanciaEntity;
import co.edu.uniandes.dse.parcial1.entities.UbicacionBodegaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(UbicacionBodegaService.class)
public class UbicacionBodegaServiceTest {

    @Autowired
    private UbicacionBodegaService ubicacionService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();


    private List<UbicacionBodegaEntity> ubicacionList = new ArrayList<>();
    private List<MercanciaEntity> mercanciaList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MercanciaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from UbicacionBodegaEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            UbicacionBodegaEntity ubicacion = factory.manufacturePojo(UbicacionBodegaEntity.class);
            ubicacion.setMercancia(new ArrayList<>());
            entityManager.persist(ubicacion);
            ubicacionList.add(ubicacion);

            MercanciaEntity mercancia = factory.manufacturePojo(MercanciaEntity.class);
            mercancia.setUbicacionBodegaEntity(ubicacion);
            entityManager.persist(mercancia);
            ubicacion.getMercancia().add(mercancia);
            mercanciaList.add(mercancia);
        }
    }

    @Test
    void testCreateUbicacionValida() throws IllegalOperationException {
        UbicacionBodegaEntity nueva = factory.manufacturePojo(UbicacionBodegaEntity.class);
        nueva.setCanasta(1);
        nueva.setNumeroEstante(01);
        nueva.setPesoMax(50);

        UbicacionBodegaEntity result = ubicacionService.createUbicacion(nueva);
        assertNotNull(result);

        UbicacionBodegaEntity stored = entityManager.find(UbicacionBodegaEntity.class, result.getId());
        assertEquals(nueva.getCanasta(), stored.getCanasta());

    }

    @Test
    void testCreateUbicacionInvalida(){
        UbicacionBodegaEntity nueva = factory.manufacturePojo(UbicacionBodegaEntity.class);
        nueva.setNumeroEstante(-1);

        assertThrows(IllegalOperationException.class, () -> ubicacionService.createUbicacion(nueva));
    }


    
}

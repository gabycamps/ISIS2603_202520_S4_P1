package co.edu.uniandes.dse.parcial1.services;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcial1.entities.MercanciaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(MercanciaService.class)
public class MercanciaServiceTest {
    
    @Autowired
    private MercanciaService mercanciaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

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
		MercanciaEntity entity = factory.manufacturePojo(MercanciaEntity.class);
		entityManager.persist(entity);
	}

    @Test
    public void testCreateMercancia() throws IllegalOperationException {
        MercanciaEntity newEntity = factory.manufacturePojo(MercanciaEntity.class);
        newEntity.setFechaRecepcion(LocalDateTime.of(2026, 4, 5, 3, 30, 30));
        newEntity.setCodigoBarras(1234);
        newEntity.setNombre("Paquete");;

        MercanciaEntity createdEntity = mercanciaService.createMercancia(newEntity);
        Assertions.assertNotNull(createdEntity);
        
        MercanciaEntity testedEntity = entityManager.find(MercanciaEntity.class, createdEntity.getId());
        Assertions.assertNotNull(testedEntity);
        Assertions.assertEquals(newEntity.getNombre(), testedEntity.getNombre());
        Assertions.assertEquals(newEntity.getCodigoBarras(), testedEntity.getCodigoBarras());
        Assertions.assertEquals(newEntity.getFechaRecepcion(), testedEntity.getFechaRecepcion());
    }

    
    @Test
    public void testMercanciaNotNombre() {
        MercanciaEntity newEntity = factory.manufacturePojo(MercanciaEntity.class);
        newEntity.setNombre(null);;

        // test with null name
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            newEntity.setNombre(null);
            mercanciaService.createMercancia(newEntity);
        });
    }
}

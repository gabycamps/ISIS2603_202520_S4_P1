package co.edu.uniandes.dse.parcial1.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class UbicacionBodegaEntity extends BaseEntity{
    
    private Integer numeroEstante;
    private Integer canasta;
    private Integer pesoMax;

    @PodamExclude
    @OneToMany (mappedBy = "ubicacion", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MercanciaEntity> mercancia = new ArrayList<>();
    
}

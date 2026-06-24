package ec.edu.ups.icc.fundamentos01.productos.entities;

import ec.edu.ups.icc.fundamentos01.core.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Entidad JPA del recurso products.
 *
 * Representa la tabla products en PostgreSQL.
 * Esta clase pertenece a la capa de persistencia.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class ProductEntity extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;
}
package ec.edu.ups.icc.fundamentos01.productos.entities;

import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.core.entities.BaseEntity;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity owner;

/*    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;
*/

    /*
     * Relación muchos a muchos entre productos y categorías.
     *
     * Un producto puede pertenecer a varias categorías.
     * Una categoría puede tener varios productos.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    //TreeSet : Sirve para que se ordenen los datos
    private Set<CategoryEntity> categories = new HashSet<>();
}
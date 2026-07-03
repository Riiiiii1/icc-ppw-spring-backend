package ec.edu.ups.icc.fundamentos01.categories.entities;

import ec.edu.ups.icc.fundamentos01.core.entities.BaseEntity;
import ec.edu.ups.icc.fundamentos01.productos.entities.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class CategoryEntity extends BaseEntity {

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @Column(length = 500)
    private String description;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<ProductEntity> products = new HashSet<>();
}
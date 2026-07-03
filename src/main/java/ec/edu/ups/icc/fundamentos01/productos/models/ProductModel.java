package ec.edu.ups.icc.fundamentos01.productos.models;

import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@Data
@AllArgsConstructor
public class ProductModel {

    private Long id;
    private String name;
    private Double price;
    private Integer stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;
    private UserEntity owner;
    //private CategoryEntity category;
    private List<CategoryEntity> categories;
    public ProductModel(Long id, String name, Double price, Integer stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
}
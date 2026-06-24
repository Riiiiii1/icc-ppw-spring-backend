package ec.edu.ups.icc.fundamentos01.productos.models;

import ec.edu.ups.icc.fundamentos01.productos.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.entities.ProductEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
 * Modelo de dominio del recurso products.
 *
 * Ya no depende de un Mapper externo: sabe construirse a partir
 * de DTOs/Entity, y convertirse a Entity/DTO de respuesta.
 */
@Data
@NoArgsConstructor
public class Product {

    private Long id;
    private String name;
    private Double price;
    private Integer stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;

    public static Product fromDto(CreateProductDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        return product;
    }

    public static Product fromEntity(ProductEntity entity) {
        Product product = new Product();
        product.setId(entity.getId());
        product.setName(entity.getName());
        product.setPrice(entity.getPrice());
        product.setStock(entity.getStock());
        product.setCreatedAt(entity.getCreatedAt());
        product.setUpdatedAt(entity.getUpdatedAt());
        product.setDeleted(entity.isDeleted());
        return product;
    }

    public ProductEntity toEntity() {
        ProductEntity entity = new ProductEntity();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setPrice(this.price);
        entity.setStock(this.stock);
        entity.setCreatedAt(this.createdAt);
        entity.setUpdatedAt(this.updatedAt);
        entity.setDeleted(this.deleted);
        return entity;
    }

    public ProductResponseDto toResponseDto() {
        ProductResponseDto response = new ProductResponseDto();
        response.setId(this.id);
        response.setName(this.name);
        return response;
    }

    public void update(UpdateProductDto dto) {
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.stock = dto.getStock();
    }

    public void partialUpdate(PartialUpdateProductDto dto) {
        if (dto.getName() != null) {
            this.name = dto.getName();
        }
        if (dto.getPrice() != null) {
            this.price = dto.getPrice();
        }
    }
}
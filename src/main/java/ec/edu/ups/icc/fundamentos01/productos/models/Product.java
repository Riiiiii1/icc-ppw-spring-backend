package ec.edu.ups.icc.fundamentos01.productos.models;

import ec.edu.ups.icc.fundamentos01.categories.dtos.CategoryResponseDto;
import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.productos.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private UserEntity owner;
    private CategoryEntity category;

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
        product.setOwner(entity.getOwner());
        product.setCategory(entity.getCategory());
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
        entity.setOwner(this.owner);
        entity.setCategory(this.category);
        return entity;
    }

    public ProductResponseDto toResponseDto() {
        ProductResponseDto response = new ProductResponseDto();
        response.setId(this.id);
        response.setName(this.name);
        response.setPrice(this.price);
        response.setStock(this.stock);
        response.setCreatedAt(this.createdAt);
        response.setUpdatedAt(this.updatedAt);

        if (this.owner != null) {
            UserResponseDto ownerDto = new UserResponseDto();
            ownerDto.setId(this.owner.getId());
            ownerDto.setName(this.owner.getName());
            ownerDto.setEmail(this.owner.getEmail());
            response.setOwner(ownerDto);
        }

        if (this.category != null) {
            CategoryResponseDto categoryDto = new CategoryResponseDto();
            categoryDto.setId(this.category.getId());
            categoryDto.setName(this.category.getName());
            categoryDto.setDescription(this.category.getDescription());
            response.setCategory(categoryDto);
        }

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
        if (dto.getStock() != null) {
            this.stock = dto.getStock();
        }
    }
}
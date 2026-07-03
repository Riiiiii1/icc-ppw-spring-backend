package ec.edu.ups.icc.fundamentos01.productos.dtos;

import ec.edu.ups.icc.fundamentos01.categories.dtos.CategoryResponseDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

    private Long id;

    private String name;

    private Double price;

    private Integer stock;

    private UserResponseDto owner;

    //private CategoryResponseDto category;
    private List<CategoryResponseDto> categories;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}

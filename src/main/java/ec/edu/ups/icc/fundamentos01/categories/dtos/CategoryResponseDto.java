package ec.edu.ups.icc.fundamentos01.categories.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDto {

    private Long id;

    private String name;

    private String description;


}
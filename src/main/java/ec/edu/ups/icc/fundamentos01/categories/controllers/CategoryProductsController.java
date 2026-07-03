package ec.edu.ups.icc.fundamentos01.categories.controllers;

import ec.edu.ups.icc.fundamentos01.productos.dtos.ProductFilterByCategoryDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.productos.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryProductsController {
     @Autowired
     private ProductService productService;

    @GetMapping("/{id}/products")
    public List<ProductResponseDto> findProductsByCategory(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductFilterByCategoryDto filters
    ) {
        return productService.findByCategoryIdWithFilters(id, filters);
    }
}

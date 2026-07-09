package ec.edu.ups.icc.fundamentos01.productos.controllers;

import ec.edu.ups.icc.fundamentos01.core.dto.PaginationDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductControllers {
    @Autowired
    private  ProductService productService;



    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductResponseDto>> findAll() {
        List<ProductResponseDto> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ProductResponseDto findOne(@PathVariable Long id) {
        return productService.findOne(id);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody CreateProductDto dto) {
        ProductResponseDto created = productService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ProductResponseDto update(@PathVariable Long id, @Valid @RequestBody UpdateProductDto dto) {
        return productService.update(id, dto);
    }

    @PatchMapping("/{id}")
    public ProductResponseDto partialUpdate(@PathVariable Long id, @Valid @RequestBody PartialUpdateProductDto dto) {
        return productService.partialUpdate(id, dto);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAll(){
        productService.deleteAll();

    }

    @GetMapping("/page")
    public Page<ProductResponseDto> findAllPage(
            @Valid @ModelAttribute PaginationDto pagination
    ) {
        return productService.findAllPage(pagination);
    }

    @GetMapping("/slice")
    public Slice<ProductResponseDto> findAllSlice(
            @Valid @ModelAttribute PaginationDto pagination
    ) {
        return productService.findAllSlice(pagination);
    }




}
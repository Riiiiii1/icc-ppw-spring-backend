package ec.edu.ups.icc.fundamentos01.productos.services;

import ec.edu.ups.icc.fundamentos01.productos.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.UpdateProductDto;

import java.util.List;

public interface ProductService {

    List<ProductResponseDto> findAll();

    ProductResponseDto findOne(Long id);

    ProductResponseDto create(CreateProductDto dto);

    ProductResponseDto update(Long id, UpdateProductDto dto);

    ProductResponseDto partialUpdate(Long id, PartialUpdateProductDto dto);

    void delete(Long id);
}
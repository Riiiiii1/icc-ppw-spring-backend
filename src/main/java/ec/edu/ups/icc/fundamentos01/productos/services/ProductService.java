package ec.edu.ups.icc.fundamentos01.productos.services;

import ec.edu.ups.icc.fundamentos01.core.dto.PaginationDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.*;
import ec.edu.ups.icc.fundamentos01.security.services.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ProductService {

    List<ProductResponseDto> findAll();

    ProductResponseDto findOne(Long id, UserDetailsImpl currentUser);
    //ProductResponseDto create(CreateProductDto dto);


    ProductResponseDto create(
            CreateProductDto dto,
            UserDetailsImpl currentUser
    );





    //ProductResponseDto update(Long id, UpdateProductDto dto);
    ProductResponseDto update(
            Long id,
            UpdateProductDto dto,
            UserDetailsImpl currentUser
    );

    //ProductResponseDto partialUpdate(Long id, PartialUpdateProductDto dto);
    ProductResponseDto partialUpdate(
            Long id,
            PartialUpdateProductDto dto,
            UserDetailsImpl currentUser
    );
    //void delete(Long id);

    void delete(
            Long id,
            UserDetailsImpl currentUser
    );

    List<ProductResponseDto> findByUserIdWithFilters(
            Long userId,
            ProductFilterByUserDto filters
    );
    List<ProductResponseDto> findByCategoryIdWithFilters(
            Long categoryId,
            ProductFilterByCategoryDto filters
    );

    void deleteAll();

    /*
     * Retorna productos activos usando Page.
     */
    Page<ProductResponseDto> findAllPage(PaginationDto pagination);

    /*
     * Retorna productos activos usando Slice.
     */
    Slice<ProductResponseDto> findAllSlice(PaginationDto pagination, UserDetailsImpl currentUser);

    Page<ProductResponseDto> findByCategoryIdWithFiltersPage(
            Long categoryId,
            ProductFilterByCategoryDto filters,
            PaginationDto pagination
    );

    Slice<ProductResponseDto> findByCategoryIdWithFiltersSlice(
            Long categoryId,
            ProductFilterByCategoryDto filters,
            PaginationDto pagination
    );
}
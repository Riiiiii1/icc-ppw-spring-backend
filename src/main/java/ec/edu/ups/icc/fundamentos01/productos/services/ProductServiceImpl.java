package ec.edu.ups.icc.fundamentos01.productos.services;

import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.BadRequestException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.productos.dtos.*;
import ec.edu.ups.icc.fundamentos01.productos.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.productos.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos01.productos.models.ProductModel;
import ec.edu.ups.icc.fundamentos01.productos.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<ProductResponseDto> findAll() {
        return productRepository.findAll()
                .stream()
                .filter(entity -> !entity.isDeleted())
                .map(ProductMapper::toModelFromEntity)
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponseDto findOne(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (entity.isDeleted()) {
            throw new NotFoundException("Product not found");
        }

        ProductModel model = ProductMapper.toModelFromEntity(entity);
        return ProductMapper.toResponse(model);
    }

    @Override
    public ProductResponseDto create(CreateProductDto dto) {
        UserEntity owner = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (owner.isDeleted()) {
            throw new NotFoundException("User not found");
        }

        Set<CategoryEntity> categories = validateAndGetCategories(dto.getCategoryIds());



        ProductEntity entity = new ProductEntity();
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setStock(dto.getStock());
        entity.setOwner(owner);
        entity.setCategories(categories);

        ProductEntity savedEntity = productRepository.save(entity);

        ProductModel savedModel = ProductMapper.toModelFromEntity(savedEntity);
        return ProductMapper.toResponse(savedModel);
    }

    @Override
    public ProductResponseDto update(Long id, UpdateProductDto dto) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (entity.isDeleted()) {
            throw new NotFoundException("Product not found");
        }

        Set<CategoryEntity> categories = validateAndGetCategories(dto.getCategoryIds());



        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setStock(dto.getStock());
        entity.setCategories(categories);

        ProductEntity savedEntity = productRepository.save(entity);

        ProductModel model = ProductMapper.toModelFromEntity(savedEntity);
        return ProductMapper.toResponse(model);
    }

    @Override
    public ProductResponseDto partialUpdate(Long id, PartialUpdateProductDto dto) {
        ProductEntity entity = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }

        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice());
        }

        if (dto.getStock() != null) {
            entity.setStock(dto.getStock());
        }

        if (dto.getCategoryIds() != null) {
            Set<CategoryEntity> categories = validateAndGetCategories(dto.getCategoryIds());
            entity.setCategories(categories);
        }

        ProductEntity savedEntity = productRepository.save(entity);

        ProductModel model = ProductMapper.toModelFromEntity(savedEntity);
        return ProductMapper.toResponse(model);
    }

    @Override
    public void delete(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (entity.isDeleted()) {
            throw new NotFoundException("Product not found");
        }

        entity.setDeleted(true);
        productRepository.save(entity);
    }
    @Override
    public void deleteAll() {
        
            productRepository.deleteAll();
    }



    @Override
    public List<ProductResponseDto> findByUserIdWithFilters(
            Long userId,
            ProductFilterByUserDto filters
    ) {
        if (!userRepository.existsByIdAndDeletedFalse(userId)) {
            throw new NotFoundException("User not found");
        }

        validateUserFilters(filters);

        String name = normalizeName(filters.getName());

        return productRepository.findByOwnerIdWithFilters(
                        userId,
                        name,
                        filters.getMinPrice(),
                        filters.getMaxPrice(),
                        filters.getCategoryId()
                )
                .stream()
                .map(ProductMapper::toModelFromEntity)
                .map(ProductMapper::toResponse)
                .toList();
    }
    @Override
    public List<ProductResponseDto> findByCategoryIdWithFilters(
            Long categoryId,
            ProductFilterByCategoryDto filters
    ) {
        if (!categoryRepository.existsByIdAndDeletedFalse(categoryId)) {
            throw new NotFoundException("Category not found");
        }

        validateFilters(filters);

        String name = normalizeName(filters.getName());

        return productRepository.findByCategoryIdWithFilters(
                        categoryId,
                        name,
                        filters.getMinPrice(),
                        filters.getMaxPrice(),
                        filters.getUserId()
                )
                .stream()
                .map(ProductMapper::toModelFromEntity)
                .map(ProductMapper::toResponse)
                .toList();
    }



    // HELPERS PARA METODOS DE FILTOS
    private void validateFilters(ProductFilterByCategoryDto filters) {

        if (filters == null) {
            return;
        }

        if (!filters.hasValidPriceRange()) {
            throw new BadRequestException("El precio máximo debe ser mayor o igual al precio mínimo");
        }

        if (filters.getUserId() != null &&
                !userRepository.existsByIdAndDeletedFalse(filters.getUserId())) {
            throw new NotFoundException("User not found");
        }
    }
    private void validateUserFilters(ProductFilterByUserDto filters) {

        if (filters == null) {
            return;
        }

        if (!filters.hasValidPriceRange()) {
            throw new BadRequestException("El precio máximo debe ser mayor o igual al precio mínimo");
        }

        if (filters.getCategoryId() != null &&
                !categoryRepository.existsByIdAndDeletedFalse(filters.getCategoryId())) {
            throw new NotFoundException("Category not found");
        }


    }

    private String normalizeName(String name) {

        if (name == null || name.isBlank()) {
            return null;
        }

        return name.trim();
    }

    // HELPER PARA CREATE, UPDATE , PARTIAL Y DEMAS

    private Set<CategoryEntity> validateAndGetCategories(Set<Long> categoryIds) {

        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new BadRequestException("Debe seleccionar al menos una categoría");
        }

        Set<CategoryEntity> categories = new HashSet<>();

        for (Long categoryId : categoryIds) {
            CategoryEntity category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Category not found"));

            if (category.isDeleted()) {
                throw new NotFoundException("Category not found");
            }

            categories.add(category);
        }

        return categories;
    }

}
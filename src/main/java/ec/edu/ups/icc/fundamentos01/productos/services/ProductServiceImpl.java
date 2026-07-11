package ec.edu.ups.icc.fundamentos01.productos.services;

import ec.edu.ups.icc.fundamentos01.categories.dtos.CategoryResponseDto;
import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.core.dto.PaginationDto;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.BadRequestException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.productos.dtos.*;
import ec.edu.ups.icc.fundamentos01.productos.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.productos.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos01.productos.models.ProductModel;
import ec.edu.ups.icc.fundamentos01.productos.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos01.security.services.UserDetailsImpl;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole;

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
    public ProductResponseDto findOne(Long id, UserDetailsImpl currentUser) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (entity.isDeleted()) {
            throw new NotFoundException("Product not found");
        }

        validateOwnership(entity, currentUser);

        ProductModel model = ProductMapper.toModelFromEntity(entity);
        return ProductMapper.toResponse(model);
    }

    @Override
    public ProductResponseDto create(CreateProductDto dto, UserDetailsImpl currentUser) {
        UserEntity owner = findCurrentUserEntity(currentUser);

        if (owner.isDeleted()) {
            throw new NotFoundException("User not found");
        }

        if (productRepository.existsByNameAndOwnerIdAndDeletedFalse(dto.getName(), owner.getId())) {
            throw new ConflictException("Ya tienes un producto registrado con ese nombre");
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
    public ProductResponseDto update(Long id, UpdateProductDto dto, UserDetailsImpl currentUser) {
        ProductEntity entity = findActiveProductOrThrow(id);
        validateOwnership(entity, currentUser);
        if (entity.isDeleted()) {
            throw new NotFoundException("Product not found");
        }

        if (productRepository.existsByNameAndOwnerIdAndIdNotAndDeletedFalse(
                dto.getName(), entity.getOwner().getId(), entity.getId())) {
            throw new ConflictException("Ya tienes un producto registrado con ese nombre");
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
    public ProductResponseDto partialUpdate(Long id, PartialUpdateProductDto dto, UserDetailsImpl currentUser) {
        ProductEntity entity = findActiveProductOrThrow(id);

        // SE VALIDA QUE EL USUARIO AUTENTICADO PUEDA MODIFICAR ESTE PRODUCTO
        validateOwnership(entity, currentUser);

        if (dto.getName() != null) {
            if (productRepository.existsByNameAndOwnerIdAndIdNotAndDeletedFalse(
                    dto.getName(), entity.getOwner().getId(), entity.getId())) {
                throw new ConflictException("Ya tienes un producto registrado con ese nombre");
            }
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
    public void delete(Long id, UserDetailsImpl currentUser) {
        ProductEntity entity = findActiveProductOrThrow(id);
        validateOwnership(entity, currentUser);
        if (entity.isDeleted()) {
            throw new NotFoundException("Product not found");
        }

        entity.setDeleted(true);
        productRepository.save(entity);
    }

/*
    @Override
    public ProductResponseDto create(CreateProductDto dto, UserEntity currentUser) {
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

*/


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

    @Override
    public void deleteAll() {

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
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findAllPage(PaginationDto pagination) {

        Pageable pageable = createPageable(pagination);

        return productRepository.findActivePage(pageable)
                .map(ProductMapper::toModelFromEntity).map(ProductMapper::toResponse);
    }
    @Override
    @Transactional(readOnly = true)
    public Slice<ProductResponseDto> findAllSlice(PaginationDto pagination, UserDetailsImpl currentUser) {

        UserEntity owner = findCurrentUserEntity(currentUser);

        Pageable pageable = createPageable(pagination);

        return productRepository.findActiveSliceByOwner(owner.getId(), pageable)
                .map(ProductMapper::toModelFromEntity).map(ProductMapper::toResponse);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findByCategoryIdWithFiltersPage(
            Long categoryId,
            ProductFilterByCategoryDto filters,
            PaginationDto pagination
    ) {
        if (!categoryRepository.existsByIdAndDeletedFalse(categoryId)) {
            throw new NotFoundException("Category not found");
        }

        validateFilters(filters);

        String name = normalizeName(filters.getName());

        Pageable pageable = createPageable(pagination);

        return productRepository.findByCategoryIdWithFiltersPage(
                        categoryId,
                        name,
                        filters.getMinPrice(),
                        filters.getMaxPrice(),
                        filters.getUserId(),
                        pageable
                )
                .map(ProductMapper::toModelFromEntity)
                .map(ProductMapper::toResponse);
    }

    /*
     * Retorna productos activos de una categoría usando Slice.
     *
     * No calcula totalElements ni totalPages.
     */
    @Override
    @Transactional(readOnly = true)
    public Slice<ProductResponseDto> findByCategoryIdWithFiltersSlice(
            Long categoryId,
            ProductFilterByCategoryDto filters,
            PaginationDto pagination
    ) {
        if (!categoryRepository.existsByIdAndDeletedFalse(categoryId)) {
            throw new NotFoundException("Category not found");
        }

        validateFilters(filters);

        String name = normalizeName(filters.getName());

        Pageable pageable = createPageable(pagination);

        return productRepository.findByCategoryIdWithFiltersSlice(
                        categoryId,
                        name,
                        filters.getMinPrice(),
                        filters.getMaxPrice(),
                        filters.getUserId(),
                        pageable
                )
                .map(ProductMapper::toModelFromEntity)
                .map(ProductMapper::toResponse);
    }

    private Pageable createPageable(PaginationDto pagination) {

        String sortBy = normalizeSortBy(pagination.getSortBy());

        Sort.Direction direction = normalizeDirection(pagination.getDirection());

        Sort sort = Sort.by(direction, sortBy);

        return PageRequest.of(
                pagination.getPage(),
                pagination.getSize(),
                sort
        );
    }

    private String normalizeSortBy(String sortBy) {

        if (sortBy == null || sortBy.isBlank()) {
            return "id";
        }

        Set<String> allowedFields = Set.of(
                "id",
                "name",
                "price",
                "stock",
                "createdAt",
                "updatedAt"
        );

        if (!allowedFields.contains(sortBy)) {
            throw new BadRequestException("Campo de ordenamiento no permitido: " + sortBy);
        }

        return sortBy;
    }

    private Sort.Direction normalizeDirection(String direction) {

        if (direction == null || direction.isBlank()) {
            return Sort.Direction.ASC;
        }

        if (direction.equalsIgnoreCase("asc")) {
            return Sort.Direction.ASC;
        }

        if (direction.equalsIgnoreCase("desc")) {
            return Sort.Direction.DESC;
        }

        throw new BadRequestException("Dirección de ordenamiento no válida: " + direction);
    }


    private UserEntity findCurrentUserEntity(UserDetailsImpl currentUser) {

        if (currentUser == null) {
            throw new AccessDeniedException("Usuario no autenticado");
        }

        return userRepository.findByIdAndDeletedFalse(currentUser.getId())
                .orElseThrow(() -> new AccessDeniedException("Usuario no autorizado"));
    }



    private void validateOwnership(
            ProductEntity product,
            UserDetailsImpl currentUser
    ) {
        if (currentUser == null) {
            throw new AccessDeniedException("Usuario no autenticado");
        }

        // Esta validación permite que un ADMIN pueda modificar cualquier producto
        if (hasRole(currentUser, "ROLE_ADMIN")) {
            return;
        }

        if (product.getOwner() == null || product.getOwner().getId() == null) {
            throw new AccessDeniedException("El producto no tiene propietario válido");
        }

        if (!product.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No puedes modificar productos ajenos");
        }
    }

    private boolean hasRole(
            UserDetailsImpl user,
            String role
    ) {
        return user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(role));
    }
    private ProductEntity findActiveProductOrThrow(Long id) {
        return productRepository.findById(id)
                .filter(product -> !product.isDeleted())
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }
}
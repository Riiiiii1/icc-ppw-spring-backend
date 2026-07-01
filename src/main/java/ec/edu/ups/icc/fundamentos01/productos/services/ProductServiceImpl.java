package ec.edu.ups.icc.fundamentos01.productos.services;

import ec.edu.ups.icc.fundamentos01.categories.dtos.CategoryResponseDto;
import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.productos.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.productos.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos01.productos.models.Product;
import ec.edu.ups.icc.fundamentos01.productos.models.ProductModel;
import ec.edu.ups.icc.fundamentos01.productos.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private  ProductRepository productRepository;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    
    @Override
    public List<ProductResponseDto> findAll() {

        return productRepository.findAll()
                .stream()
                .map(Product::fromEntity)
                .filter(product -> !product.isDeleted())
                .map(Product::toResponseDto)
                .toList();
    }

    @Override
    public ProductResponseDto findOne(Long id) {

        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (entity.isDeleted()) {
            throw new NotFoundException("Product not found");
        }

        return Product.fromEntity(entity).toResponseDto();
    }
// SIN RELACION
//    @Override
//    public ProductResponseDto create(CreateProductDto dto) {
//
//        Product product = Product.fromDto(dto);
//
//        ProductEntity entity = product.toEntity();
//
//        ProductEntity savedEntity = productRepository.save(entity);
//
//        return Product.fromEntity(savedEntity).toResponseDto();
//    }

//    @Override
//    public ProductResponseDto update(Long id, UpdateProductDto dto) {
//
//        ProductEntity entity = productRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Product not found"));
//
//        if (entity.isDeleted()) {
//            throw new NotFoundException("Product not found");
//        }
//
//        Product product = Product.fromEntity(entity);
//
//        product.update(dto);
//
//        ProductEntity savedEntity = productRepository.save(product.toEntity());
//
//        return Product.fromEntity(savedEntity).toResponseDto();
//    }

//    @Override
//    public ProductResponseDto partialUpdate(Long id, PartialUpdateProductDto dto) {
//
//        ProductEntity entity = productRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Product not found"));
//
//        if (entity.isDeleted()) {
//            throw new NotFoundException("Product not found");
//        }
//
//        Product product = Product.fromEntity(entity);
//
//        product.partialUpdate(dto);
//
//        ProductEntity savedEntity = productRepository.save(product.toEntity());
//
//        return Product.fromEntity(savedEntity).toResponseDto();
//    }

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
    public List<ProductResponseDto> findByUserId(Long userId) {
        if (!userRepository.existsByIdAndDeletedFalse(userId)) {
            throw new NotFoundException("User not found");
        }

        List<ProductEntity> list = productRepository.findByOwner_IdAndDeletedFalse(userId);

        return list
                .stream()
                .map(ProductMapper::toModelFromEntity)
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponseDto> findByCategoryId(Long categoryId) {

        if (!categoryRepository.existsByIdAndDeletedFalse(categoryId)) {
            throw new NotFoundException("Category not found");
        }

        return productRepository.findByCategory_IdAndDeletedFalse(categoryId)
                .stream()
                .map(this::toResponse)
                .toList();
    }
    // Crear un producto pasandole un DTO
    @Override
    public ProductResponseDto create(CreateProductDto dto) {

        // Buscar al owner, llamando al metodo findById() del usuario pero pasandole como argumento el id del dto.1
        UserEntity owner = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        // Comprobar si el owner no esta eliminado
        if (owner.isDeleted()) {
            throw new NotFoundException("User not found");
        }

        // Buscar a la categoria, llamando al metodo findById() de la categoria pasandole como argumento el id del dto.
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        // Comprobar si la category no esta eliminada
        if (category.isDeleted()) {
            throw new NotFoundException("Category not found");
        }

        // Comprobar si existe el producto por el nombre sin importar si tiene mayusculas
        if (productRepository.findByNameIgnoreCaseAndDeletedFalse(dto.getName()).isPresent()) {
            throw new ConflictException("Product name already registered");
        }


        // Instanciar una entidad de producto
        ProductEntity entity = new ProductEntity();

        // Pasarle los argumentos del dto a la entidad.
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setStock(dto.getStock());
        entity.setOwner(owner);
        entity.setCategory(category);

        // Llamar al metodo save() para cargar en la base de datos

        ProductEntity savedEntity = productRepository.save(entity);

        // Crear el modelo apartir del mapper de entidad a modelo pasandole  la entidad guardad.
        ProductModel savedModel = ProductMapper.toModelFromEntity(savedEntity);
        // Retornar el modelo mappeado a response
        return ProductMapper.toResponse(savedModel);
    }
    @Override
    public ProductResponseDto update(Long id, UpdateProductDto dto) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (category.isDeleted()) {
            throw new NotFoundException("Category not found");
        }

        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setStock(dto.getStock());
        entity.setCategory(category);

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

        if (dto.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));

            if (category.isDeleted()) {
                throw new NotFoundException("Category not found");
            }

            entity.setCategory(category);
        }

        ProductEntity savedEntity = productRepository.save(entity);

        ProductModel model = ProductMapper.toModelFromEntity(savedEntity);

        return ProductMapper.toResponse(model);
    }
    private ProductResponseDto toResponse(ProductEntity entity) {

        ProductResponseDto dto = new ProductResponseDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setStock(entity.getStock());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        UserResponseDto ownerDto = new UserResponseDto();

        ownerDto.setId(entity.getOwner().getId());
        ownerDto.setName(entity.getOwner().getName());
        ownerDto.setEmail(entity.getOwner().getEmail());

        dto.setOwner(ownerDto);


        CategoryResponseDto categoryDto = new CategoryResponseDto();

        categoryDto.setId(entity.getCategory().getId());
        categoryDto.setName(entity.getCategory().getName());
        categoryDto.setDescription(entity.getCategory().getDescription());

        dto.setCategory(categoryDto);

        return dto;
    }

}
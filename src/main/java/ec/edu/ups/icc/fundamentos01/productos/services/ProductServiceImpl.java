package ec.edu.ups.icc.fundamentos01.productos.services;

import ec.edu.ups.icc.fundamentos01.productos.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.productos.models.Product;
import ec.edu.ups.icc.fundamentos01.productos.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

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
                .orElseThrow(() -> new IllegalStateException("Product not found"));

        return Product.fromEntity(entity).toResponseDto();
    }

    @Override
    public ProductResponseDto create(CreateProductDto dto) {
        Product product = Product.fromDto(dto);

        ProductEntity entity = product.toEntity();
        ProductEntity savedEntity = productRepository.save(entity);

        return Product.fromEntity(savedEntity).toResponseDto();
    }

    @Override
    public ProductResponseDto update(Long id, UpdateProductDto dto) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product not found"));

        if (entity.isDeleted()) {
            throw new IllegalStateException("Cannot update a deleted product");
        }

        Product product = Product.fromEntity(entity);
        product.update(dto);

        ProductEntity savedEntity = productRepository.save(product.toEntity());

        return Product.fromEntity(savedEntity).toResponseDto();
    }

    @Override
    public ProductResponseDto partialUpdate(Long id, PartialUpdateProductDto dto) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product not found"));

        if (entity.isDeleted()) {
            throw new IllegalStateException("Cannot update a deleted product");
        }

        Product product = Product.fromEntity(entity);
        product.partialUpdate(dto);

        ProductEntity savedEntity = productRepository.save(product.toEntity());

        return Product.fromEntity(savedEntity).toResponseDto();
    }

    @Override
    public void delete(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product not found"));

        if (entity.isDeleted()) {
            throw new IllegalStateException("Product already deleted");
        }

        entity.setDeleted(true);
        productRepository.save(entity);
    }
}
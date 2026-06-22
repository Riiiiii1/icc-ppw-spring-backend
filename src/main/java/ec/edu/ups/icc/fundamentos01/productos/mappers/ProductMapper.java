package ec.edu.ups.icc.fundamentos01.productos.mappers;

import ec.edu.ups.icc.fundamentos01.productos.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.productos.models.ProductModel;

public class ProductMapper {

    public static ProductModel toModel(CreateProductDto createProductDto){
        ProductModel model = new ProductModel();
        model.setName(createProductDto.getName());
        model.setPrice(createProductDto.getPrice());
        model.setStock(createProductDto.getStock());
        return model;
    }
    public static ProductResponseDto toResponse(ProductModel productModel){
        ProductResponseDto response = new ProductResponseDto();
        response.setId(productModel.getId());
        response.setName(productModel.getName());
        return response;
    }
}

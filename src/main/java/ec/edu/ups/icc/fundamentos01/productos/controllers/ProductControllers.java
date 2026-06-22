package ec.edu.ups.icc.fundamentos01.productos.controllers;

import ec.edu.ups.icc.fundamentos01.productos.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.productos.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.productos.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos01.productos.models.ProductModel;
import ec.edu.ups.icc.fundamentos01.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.mappers.UserMapper;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/product")
public class ProductControllers {
    private List<ProductModel> products = new ArrayList<>();
    private AtomicLong idContadorProducts = new AtomicLong(1);

    @GetMapping
    public List<ProductResponseDto> findAll(){
        return products.stream().map(ProductMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public Object findOne(@PathVariable Long id){
        return products.stream()
                // Recuerda el .filter(), devuelve una lista o un nuevo array con los objetos que cumplan una condion
                .filter(p -> p.getId().equals(id))
                // Al hacer .findFirst(), el array o el conjunto de datos
                // pasa a ser un Optional<ProductResponseDTO>
                // Por lo que es incomatible con Objecto por lo que debemos Castear con (Object)
                .findFirst()
                .map(product -> (Object) ProductMapper.toResponse(product))
                .orElseGet(() -> new Object(){
                    public String error = "Producto No Encontrado";
                });
    }
    @PostMapping
    public ProductResponseDto create(@RequestBody CreateProductDto product){
        ProductModel productModel= ProductMapper.toModel(product);
        productModel.setId(idContadorProducts.getAndIncrement());
        products.add(productModel);
        return ProductMapper.toResponse(productModel);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable Long id, @RequestBody UpdateProductDto productModified){
        // Recuerda el .filter(), devuelve una lista o un nuevo array con los objetos que cumplan una condion
        ProductModel productModel = products.stream().filter(p->p.getId().equals(id)).findFirst().orElse(null);

        if (productModel==null){
            return new Object() {
                public String error = "Product Not Found";
            };
        }

        productModel.setName(productModified.getName());
        productModel.setPrice(productModified.getPrice());
        productModel.setStock(productModified.getStock());
        // Hereda Implicitamente de Object.
        return ProductMapper.toResponse(productModel);
    }


    @PatchMapping("/{id}")
    public Object patch(@PathVariable Long id, @RequestBody PartialUpdateProductDto productModified){
        ProductModel productModel = products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                //Este metodo es esencial ya que devuelve el productModel o si no, devuelve null
                .orElse(null);

        if (productModel==null){
            return new Object() {
                public String error = "Product Not Found";
            };
        }
        if (productModified.getName() != null)
            productModel.setName(productModified.getName());
        if (productModel.getPrice() != null)
            productModel.setPrice(productModified.getPrice());
        return ProductMapper.toResponse(productModel);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable int id) {

        // Programacion funcional
        // Remover el objeto del array segun el id que coincida
        boolean exists = products.removeIf(u -> u.getId().equals(id));
        if (!exists)
            return new Object() {
                public String error = "User not found";
            };

        return new Object() {
            public String message = "Deleted successfully";
        };
    }
}

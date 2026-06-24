package ec.edu.ups.icc.fundamentos01.productos.repositories;

import ec.edu.ups.icc.fundamentos01.productos.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
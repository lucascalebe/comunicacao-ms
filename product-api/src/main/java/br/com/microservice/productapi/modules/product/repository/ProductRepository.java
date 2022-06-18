package br.com.microservice.productapi.modules.product.repository;

import br.com.microservice.productapi.modules.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

  List<Product> findByNameIgnoreCaseContaining(String name);
  List<Product> findByCategoryId(Integer categoryId);
  List<Product> findBySupplierId(Integer supplierId);


}

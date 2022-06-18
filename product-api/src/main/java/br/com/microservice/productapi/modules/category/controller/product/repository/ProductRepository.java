package br.com.microservice.productapi.modules.category.controller.product.repository;

import br.com.microservice.productapi.modules.category.controller.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}

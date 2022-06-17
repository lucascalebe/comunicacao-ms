package br.com.microservice.productapi.modules.product.repository;

import br.com.microservice.productapi.modules.product.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
}

package br.com.microservice.productapi.modules.product.model;

import br.com.microservice.productapi.modules.category.dto.CategoryRequest;
import br.com.microservice.productapi.modules.category.model.Category;
import br.com.microservice.productapi.modules.product.dto.ProductRequest;
import br.com.microservice.productapi.modules.supplier.model.Supplier;
import br.com.microservice.productapi.modules.supplier.service.SupplierService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  @Column(name = "NAME", nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "FK_SUPPLIER", nullable = false)
  private Supplier supplier;

  @ManyToOne
  @JoinColumn(name = "FK_CATEGORY", nullable = false)
  private Category category;

  @Column(name = "QUANTITY_AVAILABLE", nullable = false)
  private Integer quantityAvailable;

  @Column(name = "CREATED_AT", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @PrePersist
  public void prePersist() {
    createdAt = LocalDateTime.now();
  }
  public static Product of(ProductRequest request, Category category, Supplier supplier) {
    return Product.builder()
            .name(request.getName())
            .quantityAvailable(request.getQuantityAvailable())
            .category(category)
            .supplier(supplier)
            .build();
  }
}

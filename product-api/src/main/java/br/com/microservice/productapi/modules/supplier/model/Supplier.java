package br.com.microservice.productapi.modules.supplier.model;

import br.com.microservice.productapi.modules.category.dto.CategoryRequest;
import br.com.microservice.productapi.modules.category.model.Category;
import br.com.microservice.productapi.modules.supplier.dto.SupplierRequest;
import br.com.microservice.productapi.modules.supplier.dto.SupplierResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SUPPLIER")
public class Supplier {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  @Column(name = "NAME", nullable = false)
  private String name;

  public static Supplier of(SupplierRequest request) {
    var supplier = new Supplier();
    BeanUtils.copyProperties(request, supplier);
    return supplier;
  }
}

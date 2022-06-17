package br.com.microservice.productapi.modules.product.model;

import br.com.microservice.productapi.modules.product.dto.CategoryRequest;
import br.com.microservice.productapi.modules.product.dto.CategoryResponse;
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
@Table(name = "CATEGORY")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  @Column(name = "DESCRIPTION", nullable = false)
  private String description;

  public static Category of(CategoryRequest request) {
    var category = new Category();
    BeanUtils.copyProperties(request, category);
    return category;
  }
}

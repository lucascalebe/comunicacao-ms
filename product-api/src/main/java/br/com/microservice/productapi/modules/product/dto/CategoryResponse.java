package br.com.microservice.productapi.modules.product.dto;

import br.com.microservice.productapi.modules.product.model.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class CategoryResponse {

  private Integer id;
  private String description;

  public static CategoryResponse of(Category category) {
    var response = new CategoryResponse();
    BeanUtils.copyProperties(category, response);
    return response;
  }
}

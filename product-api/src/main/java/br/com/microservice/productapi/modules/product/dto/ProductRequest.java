package br.com.microservice.productapi.modules.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

  private String name;
  @JsonProperty("quantity_available")
  private Integer quantityAvailable;
  private Integer supplierId;
  private Integer categoryId;
}

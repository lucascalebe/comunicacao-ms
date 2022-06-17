package br.com.microservice.productapi.modules.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

  private String name;
  private Integer quantityAvailable;
  private Integer supplierId;
  private Integer categoryId;
}

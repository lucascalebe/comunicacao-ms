package br.com.microservice.productapi.modules.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCheckStockRequest {

  private List<ProductQuantityDTO> products;
}

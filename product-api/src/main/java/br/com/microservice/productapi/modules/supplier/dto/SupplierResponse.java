package br.com.microservice.productapi.modules.supplier.dto;

import br.com.microservice.productapi.modules.supplier.model.Supplier;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class SupplierResponse {

  private Integer id;
  private String name;

  public static SupplierResponse of(Supplier supplier) {
    var response = new SupplierResponse();
    BeanUtils.copyProperties(supplier, response);
    return response;
  }
}

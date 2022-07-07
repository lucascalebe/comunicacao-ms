package br.com.microservice.productapi.modules.sales.dto;

import br.com.microservice.productapi.modules.sales.enums.SalesStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesConfirmationDTO {

  private String salesId;
  private SalesStatus status;
  private String transactionid;
}

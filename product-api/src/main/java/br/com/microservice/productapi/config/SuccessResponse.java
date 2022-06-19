package br.com.microservice.productapi.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessResponse {
  private Integer status;
  private String message;

  public static SuccessResponse create(String message) {
    return SuccessResponse.builder()
            .status(HttpStatus.OK.value())
            .message(message)
            .build();
  }
}

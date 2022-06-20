package br.com.microservice.productapi.modules.jwt.dto;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JwtResponse {

  private Integer id;
  private String name;
  private String email;

  public static JwtResponse getUser(Claims jwtClaims) {
    try {
      return JwtResponse.builder()
              .id((Integer) jwtClaims.get("id"))
              .name((String) jwtClaims.get("name"))
              .email((String) jwtClaims.get("email"))
              .build();
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }
}

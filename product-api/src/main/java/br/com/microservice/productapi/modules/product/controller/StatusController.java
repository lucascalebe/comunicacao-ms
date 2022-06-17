package br.com.microservice.productapi.modules.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class StatusController {

  @GetMapping("/status")
  public ResponseEntity<HashMap<String, Object>> getApiStatus() {
    var response = new HashMap<String, Object>();
    response.put("service", "product-API");
    response.put("status", "UP");
    response.put("httpStatus", HttpStatus.OK);

    return ResponseEntity.ok(response);
  }
}

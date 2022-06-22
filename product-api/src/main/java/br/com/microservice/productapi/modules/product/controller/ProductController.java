package br.com.microservice.productapi.modules.product.controller;

import br.com.microservice.productapi.config.SuccessResponse;
import br.com.microservice.productapi.modules.product.dto.ProductCheckStockRequest;
import br.com.microservice.productapi.modules.product.dto.ProductRequest;
import br.com.microservice.productapi.modules.product.dto.ProductResponse;
import br.com.microservice.productapi.modules.product.dto.ProductSalesResponse;
import br.com.microservice.productapi.modules.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

  @Autowired
  private ProductService productService;

  @PostMapping
  public ProductResponse save(@RequestBody ProductRequest request) {
    return productService.save(request);
  }

  @GetMapping
  public List<ProductResponse> findAll() {
    return productService.findAll();
  }

  @GetMapping("/{id}")
  public ProductResponse findById(@PathVariable Integer id) {
    return productService.findByIdResponse(id);
  }

  @GetMapping("/name/{name}")
  public List<ProductResponse> findByName(@PathVariable String name) {
    return productService.findByName(name);
  }

  @GetMapping("/category/{categoryId}")
  public List<ProductResponse> findByCategoryId(@PathVariable Integer categoryId) {
    return productService.findByCategoryId(categoryId);
  }

  @GetMapping("/supplier/{supplierId}")
  public List<ProductResponse> findBySupplierId(@PathVariable Integer supplierId) {
    return productService.findBySupplierId(supplierId);
  }

  @DeleteMapping("/{id}")
  public SuccessResponse delete(@PathVariable Integer id) {
    return productService.delete(id);
  }

  @PutMapping("/{id}")
  public ProductResponse update(@RequestBody ProductRequest request , @PathVariable Integer id) {
    return productService.update(request, id);
  }

  @PostMapping("check-stock")
  public SuccessResponse checkProductStock(@RequestBody ProductCheckStockRequest request) {
    return productService.checkProductsStock(request);
  }

  @GetMapping("{productId}/sales")
  public ProductSalesResponse findProductSales(@PathVariable Integer productId) {
    return productService.findProductSales(productId);
  }
}

package br.com.microservice.productapi.modules.product.service;

import br.com.microservice.productapi.config.exception.ValidationException;
import br.com.microservice.productapi.modules.category.dto.CategoryResponse;
import br.com.microservice.productapi.modules.category.model.Category;
import br.com.microservice.productapi.modules.product.repository.ProductRepository;
import br.com.microservice.productapi.modules.category.service.CategoryService;
import br.com.microservice.productapi.modules.product.dto.ProductRequest;
import br.com.microservice.productapi.modules.product.dto.ProductResponse;
import br.com.microservice.productapi.modules.product.model.Product;
import br.com.microservice.productapi.modules.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CategoryService categoryService;

  @Autowired
  private SupplierService supplierService;

  public Product findById(Integer id) {
    return productRepository.findById(id)
            .orElseThrow(() -> new ValidationException("There's no product for the given id"));
  }

  public ProductResponse findByIdResponse(Integer id) {
    if (isEmpty(id)) {
      throw new ValidationException("The product ID was not informed.");
    }
    return ProductResponse.of(findById(id));
  }

  public List<ProductResponse> findAll() {
    return productRepository.findAll().stream()
            .map(ProductResponse::of).collect(Collectors.toList());
  }

  public List<ProductResponse> findByName(String name) {
    if (isEmpty(name)) {
      throw new ValidationException("The product name must be informed");
    }
    return productRepository.findByNameIgnoreCaseContaining(name)
            .stream().map(ProductResponse::of).collect(Collectors.toList());
  }

  public List<ProductResponse> findBySupplierId(Integer supplierId) {
    if (isEmpty(supplierId)) {
      throw new ValidationException("The supplier id must be informed");
    }
    return productRepository.findBySupplierId(supplierId)
            .stream().map(ProductResponse::of).collect(Collectors.toList());
  }

  public List<ProductResponse> findByCategoryId(Integer categoryId) {
    if (isEmpty(categoryId)) {
      throw new ValidationException("The category id must be informed");
    }
    return productRepository.findByCategoryId(categoryId)
            .stream().map(ProductResponse::of).collect(Collectors.toList());
  }

  public ProductResponse save(ProductRequest request) {
    this.validateProductDataInformed(request);
    this.validateCategoryAndSupplierIdInformed(request);

    var category = categoryService.findById(request.getCategoryId());
    var supplier = supplierService.findById(request.getSupplierId());

    var product = productRepository.save(Product.of(request, category, supplier));

    return ProductResponse.of(product);
  }

  private void validateProductDataInformed(ProductRequest request) {
    if (isEmpty(request.getName())) {
      throw new ValidationException("The product name was not informed.");
    }

    if (isEmpty(request.getQuantityAvailable())) {
      throw new ValidationException("The quantity available was not informed.");
    }

    if (request.getQuantityAvailable() <= BigDecimal.ZERO.intValue()) {
      throw new ValidationException("The quantity available should not be less or equal zero.");
    }
  }

  private void validateCategoryAndSupplierIdInformed(ProductRequest request) {
    if (isEmpty(request.getCategoryId())) {
      throw new ValidationException("The category ID was not informed.");
    }

    if (isEmpty(request.getSupplierId())) {
      throw new ValidationException("The supplier ID was not informed.");
    }
  }
}

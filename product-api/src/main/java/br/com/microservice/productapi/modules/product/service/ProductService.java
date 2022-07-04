package br.com.microservice.productapi.modules.product.service;

import br.com.microservice.productapi.config.SuccessResponse;
import br.com.microservice.productapi.config.exception.ValidationException;
import br.com.microservice.productapi.modules.category.service.CategoryService;
import br.com.microservice.productapi.modules.product.dto.ProductCheckStockRequest;
import br.com.microservice.productapi.modules.product.dto.ProductQuantityDTO;
import br.com.microservice.productapi.modules.product.dto.ProductRequest;
import br.com.microservice.productapi.modules.product.dto.ProductResponse;
import br.com.microservice.productapi.modules.product.dto.ProductSalesResponse;
import br.com.microservice.productapi.modules.product.dto.ProductStockDTO;
import br.com.microservice.productapi.modules.product.model.Product;
import br.com.microservice.productapi.modules.product.repository.ProductRepository;
import br.com.microservice.productapi.modules.sales.client.SalesClient;
import br.com.microservice.productapi.modules.sales.dto.SalesConfirmationDTO;
import br.com.microservice.productapi.modules.sales.enums.SalesStatus;
import br.com.microservice.productapi.modules.sales.rabbitmq.SalesConfirmationSender;
import br.com.microservice.productapi.modules.supplier.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
public class ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CategoryService categoryService;

  @Autowired
  private SupplierService supplierService;

  @Autowired
  private SalesConfirmationSender salesConfirmationSender;

  @Autowired
  private SalesClient salesClient;

  public Product findById(Integer id) {
    return productRepository.findById(id)
            .orElseThrow(() -> new ValidationException("There's no product for the given id"));
  }

  public ProductResponse findByIdResponse(Integer id) {
    validateInformedId(id);
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

  public Boolean existsByCategoryId(Integer categoryId) {
    return productRepository.existsByCategoryId(categoryId);
  }

  public Boolean existsBySupplierId(Integer supplierId) {
    return productRepository.existsBySupplierId(supplierId);
  }

  public ProductResponse save(ProductRequest request) {
    this.validateProductDataInformed(request);
    this.validateCategoryAndSupplierIdInformed(request);

    var category = categoryService.findById(request.getCategoryId());
    var supplier = supplierService.findById(request.getSupplierId());

    var product = productRepository.save(Product.of(request, category, supplier));

    return ProductResponse.of(product);
  }

  public ProductResponse update(ProductRequest request, Integer id) {
    validateInformedId(id);
    this.validateProductDataInformed(request);
    this.validateCategoryAndSupplierIdInformed(request);

    var category = categoryService.findById(request.getCategoryId());
    var supplier = supplierService.findById(request.getSupplierId());
    var product = Product.of(request, category, supplier);
    product.setId(id);
    productRepository.save(product);
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

  private void validateInformedId(Integer id) {
    if (isEmpty(id)) {
      throw new ValidationException("The product id must be informed");
    }
  }

  public SuccessResponse delete(Integer id) {
    validateInformedId(id);
    findById(id);
    productRepository.deleteById(id);
    return SuccessResponse.create("Product was deleted.");
  }

  public void updateProductStock(ProductStockDTO productStockDTO) {
    try {
      validateStockUpdateData(productStockDTO);
      updateStock(productStockDTO);
    } catch (Exception e) {
      log.error("Error while trying to update stock for message with error: {}", e.getMessage(), e);
      var rejectedMessage = new SalesConfirmationDTO(productStockDTO.getSalesId(), SalesStatus.REJECTED);
      salesConfirmationSender.sendSalesConfirmationMessage(rejectedMessage);
    }
  }

  @Transactional
  private void updateStock(ProductStockDTO productStockDTO) {
    var productsForUpdate = new ArrayList<Product>();
    productStockDTO.getProducts().forEach(salesProduct -> {
      var existingProduct = findById(salesProduct.getProductId());
      validateQuantityInStock(salesProduct, existingProduct);
      existingProduct.updateStock(salesProduct.getQuantity());
      productsForUpdate.add(existingProduct);
    });
    if (!isEmpty(productsForUpdate)) {
      productRepository.saveAll(productsForUpdate);
      var approvedMessage = new SalesConfirmationDTO(productStockDTO.getSalesId(), SalesStatus.APPROVED);
      salesConfirmationSender.sendSalesConfirmationMessage(approvedMessage);
    }
  }

  private void validateStockUpdateData(ProductStockDTO product) {
    if (isEmpty(product) || isEmpty(product.getSalesId())) {
      throw new ValidationException("The product data or sales id must be informed.");
    }

    if (isEmpty(product.getProducts())) {
      throw new ValidationException("The sales' products must be informed.");
    }

    product.getProducts().forEach(salesProduct -> {
      if (isEmpty(salesProduct.getQuantity()) || isEmpty(salesProduct.getProductId())) {
        throw new ValidationException("The product ID and quantity must be informed.");
      }
    });
  }

  public void validateQuantityInStock(ProductQuantityDTO salesProduct, Product existingProduct) {
    if (salesProduct.getQuantity() > existingProduct.getQuantityAvailable()) {
      throw new ValidationException(String.format("The product %s is out of Stock", existingProduct.getId()));
    }
  }

  public ProductSalesResponse findProductSales(Integer id) {
    var product = findById(id);
    try {
      var sales = salesClient.findSalesByProductId(product.getId())
              .orElseThrow(() -> new ValidationException("The sales was not found by this product."));

      return ProductSalesResponse.of(product, sales.getSalesIds());
    } catch (Exception e) {
      e.printStackTrace();
      throw new ValidationException("There was an error trying to get the product's sales.");

    }
  }

  public SuccessResponse checkProductsStock(ProductCheckStockRequest request) {
    if (isEmpty(request) || isEmpty(request.getProducts())) {
      throw new ValidationException("The request data must be informed.");
    }

    request.getProducts().forEach(this::validateStock);
    return SuccessResponse.create("The stock is ok.");
  }

  private void validateStock(ProductQuantityDTO productQuantity) {
    if (isEmpty(productQuantity.getProductId()) || isEmpty(productQuantity.getQuantity())) {
      throw new ValidationException("Product ID and quantity must be informed.");
    }
    var product = findById(productQuantity.getProductId());
    if (productQuantity.getQuantity() > product.getQuantityAvailable()) {
      throw new ValidationException(String.format("The product %s is out of stock!", product.getId()));
    }
  }
}

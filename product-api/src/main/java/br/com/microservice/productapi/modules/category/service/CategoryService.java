package br.com.microservice.productapi.modules.category.service;

import br.com.microservice.productapi.config.SuccessResponse;
import br.com.microservice.productapi.config.exception.ValidationException;
import br.com.microservice.productapi.modules.category.dto.CategoryRequest;
import br.com.microservice.productapi.modules.category.dto.CategoryResponse;
import br.com.microservice.productapi.modules.category.model.Category;
import br.com.microservice.productapi.modules.category.repository.CategoryRepository;
import br.com.microservice.productapi.modules.product.service.ProductService;
import br.com.microservice.productapi.modules.supplier.dto.SupplierRequest;
import br.com.microservice.productapi.modules.supplier.dto.SupplierResponse;
import br.com.microservice.productapi.modules.supplier.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private ProductService productService;

  public CategoryResponse findByIdResponse(Integer id) {
    if (isEmpty(id)) {
      throw new ValidationException("The category ID was not informed.");
    }
    return CategoryResponse.of(findById(id));
  }

  public Category findById(Integer id) {
    return categoryRepository.findById(id)
            .orElseThrow(() -> new ValidationException("There's no category for the given id"));
  }

  public List<CategoryResponse> findAll() {
    return categoryRepository.findAll().stream()
            .map(CategoryResponse::of).collect(Collectors.toList());
  }

  public List<CategoryResponse> findByDescription(String description) {
    if (isEmpty(description)) {
      throw new ValidationException("The category description must be informed");
    }
    return categoryRepository.findByDescriptionIgnoreCaseContaining(description)
            .stream().map(CategoryResponse::of).collect(Collectors.toList());
  }

  public SuccessResponse delete(Integer id) {
    validateInformedId(id);
    if (productService.existsByCategoryId(id)) {
      throw new ValidationException("You cannot delete this category because it's already defined by a product.");
    }
    findById(id);
    categoryRepository.deleteById(id);
    return SuccessResponse.create("Category was deleted.");
  }

  private void validateInformedId(Integer id) {
    if (isEmpty(id)) {
      throw new ValidationException("The category id must be informed");
    }
  }

  public CategoryResponse save(CategoryRequest request) {
    this.validateCategoryNameInformed(request);
    var category = categoryRepository.save(Category.of(request));
    return CategoryResponse.of(category);
  }

  public CategoryResponse update(CategoryRequest request, Integer id) {
    this.validateInformedId(id);
    this.validateCategoryNameInformed(request);
    var category = Category.of(request);
    category.setId(id);
    categoryRepository.save(category);
    return CategoryResponse.of(category);
  }

  private void validateCategoryNameInformed(CategoryRequest request) {
    if (isEmpty(request.getDescription())) {
      throw new ValidationException("The category description was not informed.");
    }
  }
}

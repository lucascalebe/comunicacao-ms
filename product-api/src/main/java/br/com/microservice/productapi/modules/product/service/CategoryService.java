package br.com.microservice.productapi.modules.product.service;

import br.com.microservice.productapi.config.exception.ValidationException;
import br.com.microservice.productapi.modules.product.dto.CategoryRequest;
import br.com.microservice.productapi.modules.product.dto.CategoryResponse;
import br.com.microservice.productapi.modules.product.model.Category;
import br.com.microservice.productapi.modules.product.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;

  public CategoryResponse save(CategoryRequest request) {
    this.validateCategoryNameInformed(request);
    var category = categoryRepository.save(Category.of(request));
    return CategoryResponse.of(category);
  }

  private void validateCategoryNameInformed(CategoryRequest request) {
    if (isEmpty(request.getDescription())) {
      throw new ValidationException("The category description was not informed.");
    }
  }
}

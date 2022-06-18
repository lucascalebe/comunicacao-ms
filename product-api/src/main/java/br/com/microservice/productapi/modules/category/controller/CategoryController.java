package br.com.microservice.productapi.modules.category.controller;

import br.com.microservice.productapi.modules.category.dto.CategoryRequest;
import br.com.microservice.productapi.modules.category.dto.CategoryResponse;
import br.com.microservice.productapi.modules.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @PostMapping
  public CategoryResponse save(@RequestBody CategoryRequest request) {
    return categoryService.save(request);
  }

  @GetMapping
  public List<CategoryResponse> findAll() {
    return categoryService.findAll();
  }

  @GetMapping("/{id}")
  public CategoryResponse findById(@PathVariable Integer id) {
    return categoryService.findByIdResponse(id);
  }

  @GetMapping("/description/{description}")
  public List<CategoryResponse> findByDescription(@PathVariable String description) {
    return categoryService.findByDescription(description);
  }
}

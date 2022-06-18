package br.com.microservice.productapi.modules.supplier.service;

import br.com.microservice.productapi.config.exception.ValidationException;
import br.com.microservice.productapi.modules.category.dto.CategoryResponse;
import br.com.microservice.productapi.modules.category.model.Category;
import br.com.microservice.productapi.modules.supplier.dto.SupplierRequest;
import br.com.microservice.productapi.modules.supplier.dto.SupplierResponse;
import br.com.microservice.productapi.modules.supplier.model.Supplier;
import br.com.microservice.productapi.modules.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierService {

  @Autowired
  private SupplierRepository supplierRepository;

  public Supplier findById(Integer id) {
    return supplierRepository.findById(id)
            .orElseThrow(() -> new ValidationException("There's no supplier for the given id"));
  }

  public SupplierResponse findByIdResponse(Integer id) {
    if (isEmpty(id)) {
      throw new ValidationException("The supplier ID was not informed.");
    }
    return SupplierResponse.of(findById(id));
  }
  public List<SupplierResponse> findAll() {
    return supplierRepository.findAll().stream()
            .map(SupplierResponse::of).collect(Collectors.toList());
  }

  public List<SupplierResponse> findByName(String name) {
    if (isEmpty(name)) {
      throw new ValidationException("The supplier name must be informed");
    }
    return supplierRepository.findByNameIgnoreCaseContaining(name)
            .stream().map(SupplierResponse::of).collect(Collectors.toList());
  }
  public SupplierResponse save(SupplierRequest request) {
    this.validateSupplierNameInformed(request);
    var supplier = supplierRepository.save(Supplier.of(request));
    return SupplierResponse.of(supplier);
  }

  private void validateSupplierNameInformed(SupplierRequest request) {
    if (isEmpty(request.getName())) {
      throw new ValidationException("The supplier name was not informed.");
    }
  }
}

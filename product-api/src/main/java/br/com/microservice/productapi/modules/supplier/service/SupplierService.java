package br.com.microservice.productapi.modules.supplier.service;

import br.com.microservice.productapi.config.exception.ValidationException;
import br.com.microservice.productapi.modules.supplier.dto.SupplierRequest;
import br.com.microservice.productapi.modules.supplier.dto.SupplierResponse;
import br.com.microservice.productapi.modules.supplier.model.Supplier;
import br.com.microservice.productapi.modules.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierService {

  @Autowired
  private SupplierRepository supplierRepository;

  public Supplier findById(Integer id) {
    return supplierRepository.findById(id)
            .orElseThrow(() -> new ValidationException("There's no supplier for the given id"));
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

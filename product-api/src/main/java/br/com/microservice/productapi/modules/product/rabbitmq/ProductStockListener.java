package br.com.microservice.productapi.modules.product.rabbitmq;

import br.com.microservice.productapi.modules.product.dto.ProductStockDTO;
import br.com.microservice.productapi.modules.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductStockListener {

  @Autowired
  private ProductService productService;

  @RabbitListener(queues = "${app-config.rabbit.queue.product-stock}")
  public void receiveProductStockMessage(ProductStockDTO product) throws JsonProcessingException {
    productService.updateProductStock(product);
    System.out.println(new ObjectMapper().writeValueAsString(product));
  }
}

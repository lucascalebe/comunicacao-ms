package br.com.microservice.productapi.config.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import static br.com.microservice.productapi.config.RequestUtil.getCurrentRequest;

@Component
public class FeignClientAuthInterceptor implements RequestInterceptor {

  private static final String AUTHORIZATION = "Authorization";
  private static final String TRANSACTION_ID = "transactionid";


  @Override
  public void apply(RequestTemplate template) {
    var currrentRequest = getCurrentRequest();
    template.header(AUTHORIZATION, currrentRequest.getHeader(AUTHORIZATION))
            .header(TRANSACTION_ID, currrentRequest.getHeader(TRANSACTION_ID));
  }
}

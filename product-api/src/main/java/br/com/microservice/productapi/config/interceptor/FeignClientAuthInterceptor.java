package br.com.microservice.productapi.config.interceptor;

import br.com.microservice.productapi.config.exception.ValidationException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class FeignClientAuthInterceptor implements RequestInterceptor {

  private static final String AUTHORIZATION = "Authorization";

  @Override
  public void apply(RequestTemplate template) {
    var currrentRequest = getCurrentRequest();
    template.header(AUTHORIZATION, currrentRequest.getHeader(AUTHORIZATION));
  }

  private HttpServletRequest getCurrentRequest() {
    try {
      return ((ServletRequestAttributes) RequestContextHolder
              .currentRequestAttributes())
              .getRequest();
    } catch (Exception e) {
      e.printStackTrace();
      throw new ValidationException("The current request could not be proccessed.");
    }
  }
}

package ar.com.bancogalicia.po.bean;

import ar.com.bancogalicia.api.model.AddResponse;
import ar.com.bancogalicia.api.model.Response;
import org.apache.camel.Exchange;

/**
 * Service interface for name bean.
 * 
 */
public interface MathRestResponseService {

   AddResponse getAddResponse(Exchange exchange);
   Response getOperationResponse(Exchange exchange);
}

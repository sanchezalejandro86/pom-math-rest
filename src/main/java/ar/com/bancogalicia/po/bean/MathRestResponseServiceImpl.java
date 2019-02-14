package ar.com.bancogalicia.po.bean;

import ar.com.bancogalicia.api.model.OperationResult;
import ar.com.bancogalicia.api.model.Response;
import ar.com.bancogalicia.api.model.Meta;
import ar.com.bancogalicia.api.model.AddResponse;

import ar.com.bancogalicia.common.logging.GaliciaLogger;
import ar.com.bancogalicia.common.logging.utils.GaliciaLoggerFactory;
import ar.com.bancogalicia.po.model.OperationDTO;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service("responseService")
public class MathRestResponseServiceImpl implements MathRestResponseService {

    @Autowired
    private GaliciaLoggerFactory factory;
    private GaliciaLogger logger;

    @PostConstruct
    public void init() {
        this.logger = factory.getLogger(this.getClass());
    }

    @Override
    public AddResponse getAddResponse(Exchange exchange) {
        AddResponse response = new AddResponse();

        response.meta(getMetadata(exchange));
        response.data(new ArrayList<>());
        response.errors(new ArrayList<>());

        return response;
    }

    @Override
    public Response getOperationResponse(Exchange exchange) {
        Response response = new Response();
        List<OperationResult> operationsResult = exchange.getIn().getBody(List.class);

        response.meta(getMetadata(exchange));
        response.setData(operationsResult);
        response.setErrors(new ArrayList<>());
        return response;
    }

    private Meta getMetadata(Exchange exchange) {
        Meta meta = new Meta();
        meta.setMethod(exchange.getIn().getHeader(Exchange.HTTP_METHOD, String.class));
        meta.setOperation(exchange.getIn().getHeader(Exchange.HTTP_URI, String.class));
        return meta;
    }

}

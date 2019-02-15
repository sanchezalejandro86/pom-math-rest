package ar.com.bancogalicia.po.route;

import ar.com.bancogalicia.api.model.AddResponse;
import ar.com.bancogalicia.api.model.Operands;
import ar.com.bancogalicia.api.model.OperationResult;
import ar.com.bancogalicia.api.model.Response;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.apache.camel.component.mongodb3.MongoDbConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A simple Camel REST DSL route that implement the greetings bean.
 * 
 */
@Component
public class CamelRouter extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        JacksonDataFormat operationsResult = new ListJacksonDataFormat(OperationResult.class);
        operationsResult.disableFeature(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        restConfiguration()
                .apiContextPath("/api-docs")
                .apiProperty("api.title", "pom-math-rest")
                .apiProperty("api.description", "REST API exposed by Springboot & Fuse compatible with RHOAR")
                .apiProperty("api.version", "1.0-SNAPSHOT")
                .apiProperty("cors", "true")
                .apiProperty("base.path", "v1/pom/math")
                .apiProperty("api.path", "/")
                .apiProperty("host", "")
                .apiContextRouteId("doc-api")
            .component("servlet")
            .bindingMode(RestBindingMode.json);
        
        rest("/add/").description("Operation ADD")
            .get().outType(Response.class)
                .route().routeId("add-operations-api")
                .setHeader(MongoDbConstants.CRITERIA, constant(Filters.eq("operator", "ADD")))
                .to("mongodb3:mongo?database=math" +
                        "&collection=results" +
                        "&operation=findAll")
                .process(exchange -> {
                    if (exchange.getIn().getBody(List.class) != null){
                        List<Document> mongoResponse = exchange.getIn().getBody(List.class);
                        String responseJson = JSON.serialize(mongoResponse);
                        exchange.getIn().setBody(responseJson, String.class);
                    }
                })
                .unmarshal(operationsResult)
                .bean("responseService", "getOperationResponse")
                .endRest()
            .post().outType(AddResponse.class)
                .type(Operands.class)
                .route().routeId("add-api")
                .process(exchange -> {
                    Operands ops = exchange.getIn().getBody(Operands.class);
                    OperationResult o = new OperationResult();
                    o.setOperand1(ops.getOp1());
                    o.setOperand2(ops.getOp2());
                    o.setOperationId(-1);
                    o.setOperator(OperationResult.OperatorEnum.ADD);
                    exchange.getIn().setBody(o);
                })
                .marshal().json(JsonLibrary.Jackson)
                .log("BODY: ${body}")
                .to(ExchangePattern.InOnly, "activemq:queue:math/calculate")
                .bean("responseService", "getAddResponse")
        ;

        from("activemq:queue:math/operations")
                .log("operacion de la cola: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, Document.class)
                .to("mongodb3:mongo?database=math" +
                        "&collection=results" +
                        "&operation=insert")
        ;

    }

}

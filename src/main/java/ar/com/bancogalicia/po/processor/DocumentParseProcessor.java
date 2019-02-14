package ar.com.bancogalicia.po.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.bson.Document;

public class DocumentParseProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        if (exchange.getIn().getBody(Document.class) != null){
            Document mongoResponse = exchange.getIn().getBody(Document.class);
            String responseJson = mongoResponse.toJson();
            exchange.getIn().setBody(responseJson, String.class);
        }
    }
}

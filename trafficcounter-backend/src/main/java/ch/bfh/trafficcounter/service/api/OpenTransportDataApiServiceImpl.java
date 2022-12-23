package ch.bfh.trafficcounter.service.api;

import ch.opentdata.wsdl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link OpenTransportDataApiService}.
 *
 * @author Manuel Riesen
 */
@Service
public class OpenTransportDataApiServiceImpl implements OpenTransportDataApiService {

    private final String HEADER_KEY_AUTHORIZATION = "Authorization";

    private final PullService pullService;

    private final PullInterface pullInterface;

    private final String apiKey;

    @Autowired
    public OpenTransportDataApiServiceImpl(
        final PullService pullService,
        @Value("${trafficcounter.api-key}") final String apiKey
    ) {
        this.pullService = pullService;
        this.apiKey = apiKey;

        // setup pull interface to use API key
        final Map<String, List<String>> requestHeaders = new HashMap<>(1);
        requestHeaders.put(HEADER_KEY_AUTHORIZATION, List.of(apiKey));
        final PullInterface pullInterface = pullService.getPullSoapEndPoint();
        final BindingProvider bindingProvider = (BindingProvider) pullInterface;
        bindingProvider.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, requestHeaders);
        this.pullInterface = pullInterface;
    }

    private D2LogicalModel createConsumerBody() {
        final D2LogicalModel consumerBody = new D2LogicalModel();
        final Exchange exchange = new Exchange();
        final InternationalIdentifier identifier = new InternationalIdentifier();
        identifier.setCountry(CountryEnum.CH);
        exchange.setSupplierIdentification(identifier);
        consumerBody.setExchange(exchange);
        return consumerBody;
    }

    @Override
    public D2LogicalModel pullMeasuredData() {
        final D2LogicalModel consumerBody = createConsumerBody();
        return pullInterface.pullMeasuredData(consumerBody);
    }

    @Override
    public D2LogicalModel pullMeasurementSiteTable() {
        final D2LogicalModel consumerBody = createConsumerBody();
        return pullInterface.pullMeasurementSiteTable(consumerBody);
    }


}

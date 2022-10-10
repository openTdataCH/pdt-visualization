package ch.bfh.trafficcounter.service.api;

import ch.opentdata.wsdl.PullInterface;
import ch.opentdata.wsdl.PullService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}

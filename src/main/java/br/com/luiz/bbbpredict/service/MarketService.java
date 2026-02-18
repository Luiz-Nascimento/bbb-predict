package br.com.luiz.bbbpredict.service;

import br.com.luiz.bbbpredict.dto.MarketPriceResponse;
import br.com.luiz.bbbpredict.infra.exception.InvalidClobTokenIdException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class MarketService {

    private final RestClient restClient;

    public MarketService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://clob.polymarket.com").build();
    }

    public Double fetchBuyPrice(String tokenId) {
        MarketPriceResponse response = restClient.get().uri("/price?token_id={tokenId}&side=buy", tokenId)
                .retrieve()
                .body(MarketPriceResponse.class);

        if (response == null || response.price() == null) {
            throw new InvalidClobTokenIdException("Invalid clob token id");
        }
        return response.price();

    }
    public Double fetchSellPrice(String tokenId) {
        MarketPriceResponse response = restClient.get().uri("/price?token_id={tokenId}&side=sell", tokenId)
                .retrieve()
                .body(MarketPriceResponse.class);

        if (response == null || response.price() == null) {
            throw new InvalidClobTokenIdException("Invalid clob token id");
        }
        return response.price();

    }


}

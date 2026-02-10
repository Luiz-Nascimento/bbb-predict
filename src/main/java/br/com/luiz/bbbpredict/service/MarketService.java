package br.com.luiz.bbbpredict.service;

import br.com.luiz.bbbpredict.dto.MarketPriceResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class MarketService {

    private final RestClient restClient;

    public MarketService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://clob.polymarket.com").build();
    }

    public MarketPriceResponse fetchBuyPrice(String tokenId) {
        MarketPriceResponse response = restClient.get().uri("/price?token_id={tokenId}&side=buy", tokenId)
                .retrieve()
                .body(MarketPriceResponse.class);

        return response;

    }
    public MarketPriceResponse fetchSellPrice(String tokenId) {
        MarketPriceResponse response = restClient.get().uri("/price?token_id={tokenId}&side=sell", tokenId)
                .retrieve()
                .body(MarketPriceResponse.class);

        return response;

    }


}

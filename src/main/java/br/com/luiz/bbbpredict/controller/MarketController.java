package br.com.luiz.bbbpredict.controller;

import br.com.luiz.bbbpredict.service.MarketService;
import br.com.luiz.bbbpredict.dto.MarketPriceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/market")
public class MarketController {

    private final MarketService marketService;

    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping("/buyPrice/{tokenId}")
    public ResponseEntity<MarketPriceResponse> fetchMarketBuyPrice(@PathVariable String tokenId) {
        MarketPriceResponse response = marketService.fetchBuyPrice(tokenId);
        return ResponseEntity.ok(response);
    }
}

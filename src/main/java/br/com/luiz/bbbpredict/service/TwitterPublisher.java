package br.com.luiz.bbbpredict.service;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;


@Service
public class TwitterPublisher {

    private final RestClient restClient;
    private final OAuth10aService oauthService;
    private final OAuth1AccessToken accessToken;

    public TwitterPublisher(
            @Value("${twitter.api.key}") String apiKey,
            @Value("${twitter.api.secret}") String apiSecret,
            @Value("${twitter.access.token}") String token,
            @Value("${twitter.access.secret}") String secret
    ) {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.twitter.com/2")
                .build();

        this.oauthService = new ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .build(TwitterApi.instance());

        this.accessToken = new OAuth1AccessToken(token, secret);
    }

    public void publishTweet(String content) {
        String url = "https://api.twitter.com/2/tweets";
        Map<String, String> body = Map.of("text", content);

        OAuthRequest request = new OAuthRequest(Verb.POST, url);
        oauthService.signRequest(accessToken, request);

        restClient.post()
                .uri("/tweets")
                .header("Authorization", request.getHeaders().get("Authorization"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new RuntimeException("Erro na API do X. Status: " + res.getStatusCode());
                })
                .toBodilessEntity();
    }
 }

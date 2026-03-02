package br.com.luiz.bbbpredict.service;

import br.com.luiz.bbbpredict.infra.exception.ImageDownloadException;
import br.com.luiz.bbbpredict.infra.exception.TwitterApiException;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class TwitterClient {

    private final RestClient xV2Client;
    private final RestClient uploadClient;
    private final OAuth10aService oauthService;
    private final OAuth1AccessToken accessToken;
    private static final String UPLOAD_URL = "https://upload.twitter.com/1.1/media/upload.json";


    public TwitterClient(@Value("${twitter.api.key}") String apiKey,
            @Value("${twitter.api.secret}") String apiSecret,
            @Value("${twitter.access.token}") String token,
            @Value("${twitter.access.secret}") String secret
    ) {
        this.uploadClient =
                RestClient.builder()
                        .baseUrl("https://upload.twitter.com")
                        .build();

        this.xV2Client = RestClient.builder()
                .baseUrl("https://api.twitter.com/2")
                .build();

        this.oauthService = new ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .build(TwitterApi.instance());

        this.accessToken = new OAuth1AccessToken(token, secret);
    }

    public byte[] downloadImage(String url) {
        return RestClient.create()
                .get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new ImageDownloadException("Failed to download contestant photo: " + response.getStatusCode());
                })
                .body(byte[].class);
    }

    public String uploadMedia(byte[] imageBytes, String filename) {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.POST, UPLOAD_URL);
        oauthService.signRequest(accessToken, oAuthRequest);

        ByteArrayResource imageResource = new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return filename;
            }
        };
        MultiValueMap<String, Object> multipartBody = new LinkedMultiValueMap<>();
        multipartBody.add("media", imageResource);

        Map<?, ?> response = uploadClient.post()
                .uri("/1.1/media/upload.json")
                .header("Authorization", oAuthRequest.getHeaders().get("Authorization"))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(multipartBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, resp) -> {
                    throw new TwitterApiException("Unexpected error while uploading media", resp.getStatusCode());
                })
                .body(Map.class);
        return (String) response.get("media_id_string");
    }

    public void publishTweet(String content) {
        publishTweet(content, null);
    }

    public void publishTweet(String content, String mediaId) {
        String url = "https://api.twitter.com/2/tweets";

        OAuthRequest request = new OAuthRequest(Verb.POST, url);
        oauthService.signRequest(accessToken, request);

        Map<String, Object> body = new HashMap<>();
        body.put("text", content);

        if (mediaId != null) {
            body.put("media", Map.of("media_ids", List.of(mediaId)));
        }


        xV2Client.post()
                .uri("/tweets")
                .header("Authorization", request.getHeaders().get("Authorization"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new TwitterApiException("Unexpected error while consuming twitter api: ", res.getStatusCode());
                })
                .toBodilessEntity();
    }
 }

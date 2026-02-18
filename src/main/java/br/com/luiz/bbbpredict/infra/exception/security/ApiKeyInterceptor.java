package br.com.luiz.bbbpredict.infra.exception.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    private final String API_KEY;

    public ApiKeyInterceptor(@Value("${security.api.key}") String API_KEY) {
        this.API_KEY = API_KEY;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String apiKey = request.getHeader("API_KEY");

        if (apiKey == null || !apiKey.equals(API_KEY)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;

        }
        return true;
    }
}

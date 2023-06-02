package jpa.experiment.experimentjpa.restTemplateConfig;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import static java.time.Duration.ofSeconds;

@Configuration
public class RestTemplateConfig {

    @Bean()
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.requestFactory(this::requestFactory)
                .setReadTimeout(ofSeconds(60))
                .setConnectTimeout(ofSeconds(5))
                .build();
    }

    private HttpComponentsClientHttpRequestFactory requestFactory() {
        HttpsURLConnection.setDefaultHostnameVerifier((s, sslSession) -> true);
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        SSLContext sslContext = null;
        try {
            sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
            HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
            requestFactory.setHttpClient(httpClient);
            return requestFactory;
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            return requestFactory;
        }

    }
}

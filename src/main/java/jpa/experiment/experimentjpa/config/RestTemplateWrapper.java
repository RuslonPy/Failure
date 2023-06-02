package jpa.experiment.experimentjpa.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.vavr.control.Try;
import jpa.experiment.experimentjpa.exception.CommonException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.*;
import org.w3c.dom.Document;

import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@Component
@Log4j2
public class RestTemplateWrapper {

    private final RestTemplate restTemplate;


    public RestTemplateWrapper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T doRequest(RestTemplate restTemplate,
                           CircuitBreaker circuitBreaker,
                           Bulkhead bulkhead,
                           boolean logging,
                           String url,
                           HttpMethod method,
                           Object request,
                           HttpHeaders headers,
                           ParameterizedTypeReference<T> responseType,
                           T defaultResponse) throws CommonException {
        return doRequest(restTemplate, circuitBreaker, bulkhead, logging, url, method, request, headers, responseType, defaultResponse, false, null);
    }

    public <T> T doRequest(RestTemplate restTemplate,
                           CircuitBreaker circuitBreaker,
                           Bulkhead bulkhead,
                           boolean logging,
                           String url,
                           HttpMethod method,
                           Object request,
                           HttpHeaders headers,
                           ParameterizedTypeReference<T> responseType,
                           T defaultResponse,
                           boolean throwOnTimeout) throws CommonException {
        return doRequest(restTemplate, circuitBreaker, bulkhead, logging, url, method, request, headers, responseType, defaultResponse, throwOnTimeout, null);
    }

    public <T> T doRequest(RestTemplate restTemplate,
                           CircuitBreaker circuitBreaker,
                           Bulkhead bulkhead,
                           boolean logging,
                           String url,
                           HttpMethod method,
                           Object request,
                           HttpHeaders headers,
                           ParameterizedTypeReference<T> responseType,
                           T defaultResponse,
                           boolean throwOnTimeout,
                           String logId) throws ResourceAccessException {
        try {
            HttpEntity<Object> entity = new HttpEntity<>(request, headers);
            ResponseEntity<T> response = restTemplate.exchange(url, method, entity, responseType);
            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            // Handle any HTTP status code exceptions.
            System.out.println("throw new CommonException(\"HttpStatusCodeException error occurred in doRequest()\");");
        } catch (RestClientException ex) {
            // Handle any other RestClientExceptions.
            System.out.println("throw new CommonException(\"RestClientException error occurred in doRequest()\");");
        } catch (StackOverflowError ex) {
            // Handle the StackOverflowError exception by throwing a CommonRequestException with an appropriate error message.
            throw new CommonException("Stack overflow error occurred in doRequest()");
        }

// If an exception is thrown, return the default response.

        return defaultResponse;
    }
}

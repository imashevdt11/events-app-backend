package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.dto.request.PaymentServiceRequest;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.exception.ServiceNotAvailableException;
import kg.something.events_app_backend.service.PaymentService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final WebClient webClient;

    public PaymentServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void payForTickets(PaymentServiceRequest paymentRequest) {
        try {
            webClient.post()
                    .uri("/api/payments/process")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(paymentRequest)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            error -> error.bodyToMono(String.class)
                                    .flatMap(errorMessage -> Mono.error(
                                            new ResourceNotFoundException(errorMessage)
                                    ))
                    )
                    .onStatus(HttpStatusCode::is5xxServerError,
                            error -> error.bodyToMono(String.class)
                                    .flatMap(errorMessage -> Mono.error(
                                            new ServiceNotAvailableException(errorMessage)
                                    ))
                    )
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientRequestException ex) {
            throw new ServiceNotAvailableException("Не удалось подключиться к сервису оплаты");
        }
    }
}

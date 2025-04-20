package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.request.PaymentServiceRequest;

public interface PaymentService {

    void payForTickets(PaymentServiceRequest paymentRequest);
}

package kg.something.events_app_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentServiceRequest (
        @JsonProperty("card_number")
        String cardNumber,

        @JsonProperty("card_holder")
        String cardHolder,

        @JsonProperty("expiry_date")
        String expiryDate,

        @JsonProperty("cvv")
        String cvv,

        @JsonProperty("amount")
        Double amountOfMoney
) {}

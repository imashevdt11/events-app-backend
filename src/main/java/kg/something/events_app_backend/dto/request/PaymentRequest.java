package kg.something.events_app_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentRequest (
    @JsonProperty("card_number")
    String cardNumber,

    @JsonProperty("expiry_date")
    String expiryDate,

    @JsonProperty("cvv")
    String cvv,

    @JsonProperty("amount_of_money")
    Double amountOfMoney,

    @JsonProperty("amount_of_tickets")
    Integer amountOfTickets
) {}

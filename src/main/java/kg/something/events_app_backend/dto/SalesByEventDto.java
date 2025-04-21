package kg.something.events_app_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

public record SalesByEventDto(
        @JsonProperty("event_id")
        UUID eventId,

        @JsonProperty("event_name")
        String eventName,

        @JsonProperty("number_of_sailed_tickets")
        Integer numberOfSoldTickets,

        @JsonProperty("percent_of_sailed_tickets")
        Integer percentSold,

        @JsonProperty("amount_of_money")
        Double totalRevenue
) {
        public SalesByEventDto(UUID eventId, String eventName, Long numberOfSoldTickets,
                               Integer percentSold, BigDecimal totalRevenue) {
                this(eventId, eventName, numberOfSoldTickets.intValue(), percentSold, totalRevenue.doubleValue());
        }
}

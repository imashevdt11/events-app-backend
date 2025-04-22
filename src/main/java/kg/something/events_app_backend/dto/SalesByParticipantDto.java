package kg.something.events_app_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record SalesByParticipantDto(
        @JsonProperty("participant_first_name") String participantFirstName,
        @JsonProperty("participant_last_name") String participantLastName,
        @JsonProperty("number_of_bought_tickets") Integer numberOfBoughtTickets,
        @JsonProperty("sum_of_wasted_money") Double sumOfWastedMoney
) {
        public SalesByParticipantDto(String participantFirstName, String participantLastName,
                                     Long numberOfBoughtTickets, BigDecimal sumOfWastedMoney) {
                this(participantFirstName, participantLastName, numberOfBoughtTickets.intValue(), sumOfWastedMoney.doubleValue());
        }
}

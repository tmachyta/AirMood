package task.techtasks.dto.flight;

import java.time.LocalDateTime;
import lombok.Data;
import task.techtasks.model.Flight.FlightStatus;

@Data
public class FlightDto {
    private Long id;
    private FlightStatus flightStatus;
    private String departureCountry;
    private String destinationCountry;
    private int distance;
    private int estimatedFlightTime;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime delayStartedAt;
    private LocalDateTime createdAt;
}

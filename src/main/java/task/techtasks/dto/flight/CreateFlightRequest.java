package task.techtasks.dto.flight;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import task.techtasks.model.Flight.FlightStatus;

@Data
@Accessors(chain = true)
public class CreateFlightRequest {
    private FlightStatus flightStatus;
    private Long airCompanyId;
    private Long airplaneId;
    private String departureCountry;
    private String destinationCountry;
    private int distance;
    private int estimatedFlightTime;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime delayStartedAt;
    private LocalDateTime createdAt;
}

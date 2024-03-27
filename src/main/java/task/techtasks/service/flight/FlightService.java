package task.techtasks.service.flight;

import java.util.List;
import org.springframework.data.domain.Pageable;
import task.techtasks.dto.flight.CreateFlightRequest;
import task.techtasks.dto.flight.FlightDto;
import task.techtasks.dto.flight.UpdateFlightStatusActive;
import task.techtasks.dto.flight.UpdateFlightStatusCompleted;
import task.techtasks.dto.flight.UpdateFlightStatusDelayed;
import task.techtasks.model.Flight.FlightStatus;

public interface FlightService {
    List<FlightDto> getAllFlightsByAirCompanyNameAndStatus(String companyName, FlightStatus status);

    FlightDto save(CreateFlightRequest request);

    FlightDto updateByStatusDelayed(Long id, UpdateFlightStatusDelayed request);

    FlightDto updateByStatusActive(Long id, UpdateFlightStatusActive request);

    FlightDto updateByStatusCompleted(Long id, UpdateFlightStatusCompleted request);

    List<FlightDto> getAllFlightsWithStatusActiveAndStartedAt();

    List<FlightDto> getAllFlightsWithCompletedStatusAndDifference();

    List<FlightDto> findAll(Pageable pageable);

    FlightDto findById(Long id);

    void deleteFlightById(Long id);
}

package task.techtasks.service.flight;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import task.techtasks.dto.flight.CreateFlightRequest;
import task.techtasks.dto.flight.FlightDto;
import task.techtasks.dto.flight.UpdateFlightStatusActive;
import task.techtasks.dto.flight.UpdateFlightStatusCompleted;
import task.techtasks.dto.flight.UpdateFlightStatusDelayed;
import task.techtasks.exception.EntityNotFoundException;
import task.techtasks.mapper.FlightMapper;
import task.techtasks.model.AirCompany;
import task.techtasks.model.Airplane;
import task.techtasks.model.Flight;
import task.techtasks.model.Flight.FlightStatus;
import task.techtasks.repository.AirCompanyRepository;
import task.techtasks.repository.AirplaneRepository;
import task.techtasks.repository.FlightRepository;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {
    private static final int HOURS = 24;
    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;
    private final AirCompanyRepository airCompanyRepository;
    private final AirplaneRepository airplaneRepository;

    @Override
    public List<FlightDto> getAllFlightsByAirCompanyNameAndStatus(String companyName,
                                                                  FlightStatus status) {
        return flightRepository.findFlightsByAirCompanyNameAndFlightStatus(companyName, status)
                .stream()
                .map(flightMapper::toDto)
                .toList();
    }

    @Override
    public FlightDto save(CreateFlightRequest request) {
        Airplane airplane = airplaneRepository.findById(request.getAirplaneId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find airplane by id "
                                + request.getAirplaneId()));
        AirCompany airCompany = airCompanyRepository.findById(request.getAirCompanyId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find air company by id "
                                + request.getAirCompanyId()));
        Flight flight = flightMapper.toModel(request);
        flight.setFlightStatus(FlightStatus.PENDING);
        flight.setAirplane(airplane);
        flight.setAirCompany(airCompany);
        return flightMapper.toDto(flightRepository.save(flight));
    }

    @Override
    public FlightDto updateByStatusDelayed(Long id, UpdateFlightStatusDelayed request) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find flight by id " + id));
        flight.setFlightStatus(FlightStatus.DELAYED);
        flight.setDelayStartedAt(request.getDelayStartedAt());
        return flightMapper.toDto(flightRepository.save(flight));
    }

    @Override
    public FlightDto updateByStatusActive(Long id, UpdateFlightStatusActive request) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find flight by id " + id));
        flight.setFlightStatus(FlightStatus.ACTIVE);
        flight.setStartedAt(request.getStartedAt());
        return flightMapper.toDto(flightRepository.save(flight));
    }

    @Override
    public FlightDto updateByStatusCompleted(Long id, UpdateFlightStatusCompleted request) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find flight by id " + id));
        flight.setFlightStatus(FlightStatus.COMPLETED);
        flight.setEndedAt(request.getEndedAt());
        return flightMapper.toDto(flightRepository.save(flight));
    }

    @Override
    public List<FlightDto> getAllFlightsWithStatusActiveAndStartedAt() {
        LocalDateTime time = LocalDateTime.now().minusHours(HOURS);
        return flightRepository.findFlightsByActiveStatusAndStartedAt(time)
                .stream()
                .map(flightMapper::toDto)
                .toList();
    }

    @Override
    public List<FlightDto> getAllFlightsWithCompletedStatusAndDifference() {
        return flightRepository.findFlightsByCompletedStatusAndDifference()
                .stream()
                .map(flightMapper::toDto)
                .toList();
    }

    @Override
    public List<FlightDto> findAll(Pageable pageable) {
        return flightRepository.findAll(pageable)
                .stream()
                .map(flightMapper::toDto)
                .toList();
    }

    @Override
    public FlightDto findById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find flight by id " + id));
        return flightMapper.toDto(flight);
    }

    @Override
    public void deleteFlightById(Long id) {
        flightRepository.deleteById(id);
    }
}

package task.techtasks.service.flight;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import task.techtasks.dto.flight.FlightDto;
import task.techtasks.dto.flight.UpdateFlightStatusActive;
import task.techtasks.dto.flight.UpdateFlightStatusCompleted;
import task.techtasks.dto.flight.UpdateFlightStatusDelayed;
import task.techtasks.exception.EntityNotFoundException;
import task.techtasks.mapper.FlightMapper;
import task.techtasks.model.Flight;
import task.techtasks.model.Flight.FlightStatus;
import task.techtasks.repository.FlightRepository;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplTest {
    private static final Long FLIGHT_ID = 1L;
    private static final Long NON_EXISTED_FLIGHT_ID = 50L;
    private static final String COMPANY_NAME = "Company1";
    private static final FlightStatus ACTIVE_STATUS = FlightStatus.ACTIVE;
    private static final String DELAY_STARTED_AT = "2024-03-25T16:05:19";
    private static final String PREVIOUS_DELAY_STARTED_AT = "2024-03-24T16:05:19";
    private static final String STARTED_AT = "2024-03-25T10:05:19";
    private static final String PREVIOUS_STARTED_AT = "2024-03-24T09:05:19";
    private static final String ENDED_AT = "2024-03-25T16:05:19";
    private static final String PREVIOUS_ENDED_AT = "2024-03-24T19:05:19";
    private static final FlightStatus DELAY_STATUS = FlightStatus.DELAYED;
    private static final FlightStatus COMPLETED_STATUS = FlightStatus.COMPLETED;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private FlightMapper flightMapper;

    @InjectMocks
    private FlightServiceImpl flightService;

    @Test
    public void testGetAllFlights() {
        Flight flight = new Flight();
        Pageable pageable = PageRequest.of(0, 10);
        List<Flight> flights = List.of(new Flight());
        List<FlightDto> expectedFlights = List.of(new FlightDto());
        Page<Flight> page = new PageImpl<>(flights, pageable, flights.size());

        when(flightRepository.findAll(pageable)).thenReturn(page);

        when(flightMapper.toDto(flight)).thenReturn(new FlightDto());

        List<FlightDto> result = flightService.findAll(pageable);

        Assertions.assertEquals(expectedFlights.size(), result.size());
    }

    @Test
    public void findFlightById() {
        Flight flight = new Flight();
        flight.setId(FLIGHT_ID);
        FlightDto flightDto = new FlightDto();
        flightDto.setId(FLIGHT_ID);

        when(flightRepository.findById(FLIGHT_ID)).thenReturn(Optional.of(flight));

        when(flightMapper.toDto(flight)).thenReturn(flightDto);

        FlightDto result = flightService.findById(FLIGHT_ID);

        Assertions.assertEquals(flight.getId(), result.getId());
    }

    @Test
    public void testFindFlightNonExisted() {
        when(flightRepository.findById(NON_EXISTED_FLIGHT_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> flightService.findById(NON_EXISTED_FLIGHT_ID));
    }

    @Test
    public void testDeleteFlightById() {
        flightService.deleteFlightById(FLIGHT_ID);

        when(flightRepository.findById(FLIGHT_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> flightService.findById(FLIGHT_ID));
    }

    @Test
    public void testGetAllFlightsByAirCompanyNameAndStatus() {
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight());

        List<FlightDto> expectedFlights = flights.stream()
                .map(flightMapper::toDto)
                .toList();

        when(flightRepository.findFlightsByAirCompanyNameAndFlightStatus(
                COMPANY_NAME, ACTIVE_STATUS)).thenReturn(flights);

        List<FlightDto> actualFlights =
                flightService.getAllFlightsByAirCompanyNameAndStatus(COMPANY_NAME, ACTIVE_STATUS);

        Assertions.assertEquals(actualFlights.size(), expectedFlights.size());
    }

    @Test
    public void testGetAllFlightsWithDifference() {
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight());

        List<FlightDto> expectedFlights = flights.stream()
                .map(flightMapper::toDto)
                .toList();

        when(flightRepository.findFlightsByCompletedStatusAndDifference())
                .thenReturn(flights);

        List<FlightDto> actualFlights =
                flightService.getAllFlightsWithCompletedStatusAndDifference();

        Assertions.assertEquals(actualFlights.size(), expectedFlights.size());
    }

    @Test
    public void testUpdateByStatusDelayed() {
        UpdateFlightStatusDelayed request = new UpdateFlightStatusDelayed();
        request.setDelayStartedAt(LocalDateTime.parse(DELAY_STARTED_AT));

        FlightDto expectedResult = new FlightDto();
        expectedResult.setDelayStartedAt(LocalDateTime.parse(DELAY_STARTED_AT));

        Flight flight = new Flight();
        flight.setDelayStartedAt(LocalDateTime.parse(PREVIOUS_DELAY_STARTED_AT));

        when(flightRepository.findById(FLIGHT_ID)).thenReturn(Optional.of(flight));
        flight.setFlightStatus(DELAY_STATUS);
        flight.setDelayStartedAt(LocalDateTime.parse(DELAY_STARTED_AT));

        when(flightRepository.save(flight)).thenReturn(flight);

        when(flightMapper.toDto(flight)).thenReturn(expectedResult);

        FlightDto updatedStatus = flightService.updateByStatusDelayed(FLIGHT_ID, request);

        Assertions.assertEquals(updatedStatus.getDelayStartedAt(),
                expectedResult.getDelayStartedAt());
    }

    @Test
    public void testUpdateByStatusActive() {
        UpdateFlightStatusActive request = new UpdateFlightStatusActive();
        request.setStartedAt(LocalDateTime.parse(STARTED_AT));

        FlightDto expectedResult = new FlightDto();
        expectedResult.setStartedAt(LocalDateTime.parse(STARTED_AT));

        Flight flight = new Flight();
        flight.setStartedAt(LocalDateTime.parse(PREVIOUS_STARTED_AT));

        when(flightRepository.findById(FLIGHT_ID)).thenReturn(Optional.of(flight));
        flight.setFlightStatus(FlightStatus.ACTIVE);
        flight.setStartedAt(LocalDateTime.parse(STARTED_AT));

        when(flightRepository.save(flight)).thenReturn(flight);

        when(flightMapper.toDto(flight)).thenReturn(expectedResult);

        FlightDto updatedStatus = flightService.updateByStatusActive(FLIGHT_ID, request);

        Assertions.assertEquals(updatedStatus.getStartedAt(), expectedResult.getStartedAt());
    }

    @Test
    public void testUpdateByStatusCompleted() {
        UpdateFlightStatusCompleted request = new UpdateFlightStatusCompleted();
        request.setEndedAt(LocalDateTime.parse(ENDED_AT));

        FlightDto expectedResult = new FlightDto();
        expectedResult.setEndedAt(LocalDateTime.parse(ENDED_AT));

        Flight flight = new Flight();
        flight.setEndedAt(LocalDateTime.parse(PREVIOUS_ENDED_AT));

        when(flightRepository.findById(FLIGHT_ID)).thenReturn(Optional.of(flight));
        flight.setFlightStatus(COMPLETED_STATUS);
        flight.setEndedAt(LocalDateTime.parse(ENDED_AT));

        when(flightRepository.save(flight)).thenReturn(flight);

        when(flightMapper.toDto(flight)).thenReturn(expectedResult);

        FlightDto updatedStatus = flightService.updateByStatusCompleted(FLIGHT_ID, request);

        Assertions.assertEquals(updatedStatus.getEndedAt(), expectedResult.getEndedAt());
    }
}

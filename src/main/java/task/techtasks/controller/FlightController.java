package task.techtasks.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import task.techtasks.dto.flight.CreateFlightRequest;
import task.techtasks.dto.flight.FlightDto;
import task.techtasks.dto.flight.UpdateFlightStatusActive;
import task.techtasks.dto.flight.UpdateFlightStatusCompleted;
import task.techtasks.dto.flight.UpdateFlightStatusDelayed;
import task.techtasks.model.Flight.FlightStatus;
import task.techtasks.service.flight.FlightService;

@Tag(name = "Flights management", description = "Endpoints for managing Flights")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/flights")
public class FlightController {
    private final FlightService flightService;

    @GetMapping("/{companyName}/status")
    @Operation(summary = "Get list of Flights",
            description = "Get valid list of Flights by AirCompanyName and Status")
    public List<FlightDto> getFlightByAirCompanyNameAndFlightStatus(
            @PathVariable String companyName,
            @RequestParam FlightStatus status) {
        return flightService.getAllFlightsByAirCompanyNameAndStatus(companyName, status);
    }

    @PostMapping
    @Operation(summary = "Save Flight to repository",
            description = "Save valid Flight to repository with Pending Status")
    public FlightDto create(@RequestBody CreateFlightRequest request) {
        return flightService.save(request);
    }

    @PutMapping("/delayed/{id}")
    @Operation(summary = "Update Flight by ID",
            description = "Update valid Flight Status by ID")
    public FlightDto updateByStatusDelayed(@PathVariable Long id,
                                    @RequestBody UpdateFlightStatusDelayed request) {
        return flightService.updateByStatusDelayed(id, request);
    }

    @PutMapping("/active/{id}")
    @Operation(summary = "Update Flight by ID",
            description = "Update valid Flight Status by ID")
    public FlightDto updateByStatusActive(@PathVariable Long id,
                                    @RequestBody UpdateFlightStatusActive request) {
        return flightService.updateByStatusActive(id, request);
    }

    @PutMapping("/completed/{id}")
    @Operation(summary = "Update Flight by ID",
            description = "Update valid Flight Status by ID")
    public FlightDto updateByStatusCompleted(@PathVariable Long id,
                                    @RequestBody UpdateFlightStatusCompleted request) {
        return flightService.updateByStatusCompleted(id, request);
    }

    @GetMapping("/status/active")
    @Operation(summary = "Get list of Flights",
            description = "Get valid list of Flights "
                    + "with status Active and started more than 24 hours")
    public List<FlightDto> getFlightsWithStatusActiveAndStartedMoreThan() {
        return flightService.getAllFlightsWithStatusActiveAndStartedAt();
    }

    @GetMapping("/status/completed")
    @Operation(summary = "Get list of Flights",
            description = "Get valid list of Flights "
                    + "with status completed and time difference")
    public List<FlightDto> getFlightsWithCompletedStatusAndTimeDifference() {
        return flightService.getAllFlightsWithCompletedStatusAndDifference();
    }

    @GetMapping
    @Operation(summary = "Get list of flights",
            description = "Get valid list of flights")
    List<FlightDto> getAll(@ParameterObject Pageable pageable) {
        return flightService.findAll(pageable);
    }

    @Operation(summary = "Delete flight by ID",
            description = "Soft-delete valid flight by ID")
    @DeleteMapping("/{id}")
    public void deleteFlightById(@PathVariable Long id) {
        flightService.deleteFlightById(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get flight by ID",
            description = "Get valid flight by ID")
    public FlightDto getFlightById(@PathVariable Long id) {
        return flightService.findById(id);
    }
}

package task.techtasks.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import task.techtasks.dto.airplane.AirplaneDto;
import task.techtasks.dto.airplane.CreateAirplaneRequest;
import task.techtasks.dto.airplane.CreateAirplaneWithOutAirCompanyRequest;
import task.techtasks.dto.airplane.UpdateAirplaneCompanyRequest;
import task.techtasks.service.airplane.AirplaneService;

@Tag(name = "Airplanes management", description = "Endpoints for managing Airplanes")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/airplanes")
public class AirplaneController {
    private final AirplaneService airplaneService;

    @PostMapping
    @Operation(summary = "Save airplane to repository",
            description = "Save valid airplane to repository")
    public AirplaneDto create(@RequestBody @Valid CreateAirplaneRequest request) {
        return airplaneService.save(request);
    }

    @PostMapping("/without-company")
    @Operation(summary = "Save airplane to repository",
            description = "Save valid airplane to repository without Air Company")
    public AirplaneDto createWithOutAirCompany(
            @RequestBody @Valid CreateAirplaneWithOutAirCompanyRequest request) {
        return airplaneService.saveWithOutAirCompany(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Airplane by ID",
            description = "Update valid Airplane by ID by adding Air company")
    public AirplaneDto updateById(@PathVariable Long id,
                                  @RequestBody UpdateAirplaneCompanyRequest request) {
        return airplaneService.updateAirplaneCompany(id, request);
    }

    @PutMapping("/{id}/move")
    @Operation(summary = "Move Airplane by ID",
            description = "Move valid Airplane by ID to another Air company")
    public AirplaneDto moveBetweenCompanies(@PathVariable Long id,
                                  @RequestParam Long airCompanyId) {
        return airplaneService.moveAirplaneBetweenCompanies(id, airCompanyId);
    }

    @GetMapping
    @Operation(summary = "Get list of airplanes",
            description = "Get valid list of airplanes")
    public List<AirplaneDto> getAll(@ParameterObject Pageable pageable) {
        return airplaneService.findAll(pageable);
    }

    @Operation(summary = "Delete Airplane by ID",
            description = "Soft-delete valid Airplane by ID")
    @DeleteMapping("/{id}")
    public void deleteAirplaneById(@PathVariable Long id) {
        airplaneService.deleteById(id);
    }

    @Operation(summary = "Get Airplane by ID",
            description = "Get valid Airplane by ID")
    @GetMapping("/{id}")
    public AirplaneDto findById(@PathVariable Long id) {
        return airplaneService.findById(id);
    }
}

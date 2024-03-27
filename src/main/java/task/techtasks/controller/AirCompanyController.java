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
import org.springframework.web.bind.annotation.RestController;
import task.techtasks.dto.aircompany.AirCompanyDto;
import task.techtasks.dto.aircompany.CreateAirCompanyRequest;
import task.techtasks.dto.aircompany.UpdateAirCompanyRequest;
import task.techtasks.service.aircompany.AirCompanyService;

@Tag(name = "AirCompany management", description = "Endpoints for managing Air Companies")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/air-companies")
public class AirCompanyController {
    private final AirCompanyService airCompanyService;

    @PostMapping
    @Operation(summary = "Save AirCompany to repository",
            description = "Save valid AirCompany to repository")
    public AirCompanyDto create(@RequestBody @Valid CreateAirCompanyRequest request) {
        return airCompanyService.save(request);
    }

    @GetMapping
    @Operation(summary = "Get list of AirCompanies",
            description = "Get valid list of AirCompanies")
    public List<AirCompanyDto> getAll(@ParameterObject Pageable pageable) {
        return airCompanyService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get AirCompany by ID",
            description = "Get valid AirCompany by ID")
    public AirCompanyDto getById(@PathVariable Long id) {
        return airCompanyService.findById(id);
    }

    @Operation(summary = "Delete AirCompany by ID",
            description = "Soft-delete valid AirCompany by ID")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        airCompanyService.deleteById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update AirCompany by ID",
            description = "Update valid AirCompany by ID")
    public AirCompanyDto updateById(@PathVariable Long id,
                                    @RequestBody UpdateAirCompanyRequest request) {
        return airCompanyService.updateById(id, request);
    }
}

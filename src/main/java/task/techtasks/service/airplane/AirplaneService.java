package task.techtasks.service.airplane;

import java.util.List;
import org.springframework.data.domain.Pageable;
import task.techtasks.dto.airplane.AirplaneDto;
import task.techtasks.dto.airplane.CreateAirplaneRequest;
import task.techtasks.dto.airplane.CreateAirplaneWithOutAirCompanyRequest;
import task.techtasks.dto.airplane.UpdateAirplaneCompanyRequest;

public interface AirplaneService {
    AirplaneDto save(CreateAirplaneRequest request);

    AirplaneDto saveWithOutAirCompany(CreateAirplaneWithOutAirCompanyRequest request);

    AirplaneDto updateAirplaneCompany(Long id, UpdateAirplaneCompanyRequest request);

    AirplaneDto moveAirplaneBetweenCompanies(Long id, Long airCompanyId);

    List<AirplaneDto> findAll(Pageable pageable);

    void deleteById(Long id);

    AirplaneDto findById(Long id);
}

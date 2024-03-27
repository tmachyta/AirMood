package task.techtasks.service.aircompany;

import java.util.List;
import org.springframework.data.domain.Pageable;
import task.techtasks.dto.aircompany.AirCompanyDto;
import task.techtasks.dto.aircompany.CreateAirCompanyRequest;
import task.techtasks.dto.aircompany.UpdateAirCompanyRequest;

public interface AirCompanyService {
    AirCompanyDto save(CreateAirCompanyRequest request);

    List<AirCompanyDto> getAll(Pageable pageable);

    AirCompanyDto findById(Long id);

    void deleteById(Long id);

    AirCompanyDto updateById(Long id, UpdateAirCompanyRequest request);
}

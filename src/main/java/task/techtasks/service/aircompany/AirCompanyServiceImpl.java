package task.techtasks.service.aircompany;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import task.techtasks.dto.aircompany.AirCompanyDto;
import task.techtasks.dto.aircompany.CreateAirCompanyRequest;
import task.techtasks.dto.aircompany.UpdateAirCompanyRequest;
import task.techtasks.exception.EntityNotFoundException;
import task.techtasks.mapper.AirCompanyMapper;
import task.techtasks.model.AirCompany;
import task.techtasks.repository.AirCompanyRepository;

@Service
@RequiredArgsConstructor
public class AirCompanyServiceImpl implements AirCompanyService {
    private final AirCompanyRepository airCompanyRepository;
    private final AirCompanyMapper airCompanyMapper;

    @Override
    public AirCompanyDto save(CreateAirCompanyRequest request) {
        AirCompany airCompany = airCompanyMapper.toModel(request);
        return airCompanyMapper.toDto(airCompanyRepository.save(airCompany));
    }

    @Override
    public List<AirCompanyDto> getAll(Pageable pageable) {
        return airCompanyRepository.findAll(pageable)
                .stream()
                .map(airCompanyMapper::toDto)
                .toList();
    }

    @Override
    public AirCompanyDto findById(Long id) {
        AirCompany airCompany = airCompanyRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find AirCompany by id " + id));
        return airCompanyMapper.toDto(airCompany);
    }

    @Override
    public void deleteById(Long id) {
        airCompanyRepository.deleteById(id);
    }

    @Override
    public AirCompanyDto updateById(Long id, UpdateAirCompanyRequest request) {
        AirCompany existedAirCompany = airCompanyRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find AirCompany by id " + id));
        existedAirCompany.setName(request.getName());
        existedAirCompany.setType(request.getType());
        return airCompanyMapper.toDto(airCompanyRepository.save(existedAirCompany));
    }
}

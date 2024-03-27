package task.techtasks.service.airplane;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import task.techtasks.dto.airplane.AirplaneDto;
import task.techtasks.dto.airplane.CreateAirplaneRequest;
import task.techtasks.dto.airplane.CreateAirplaneWithOutAirCompanyRequest;
import task.techtasks.dto.airplane.UpdateAirplaneCompanyRequest;
import task.techtasks.exception.EntityNotFoundException;
import task.techtasks.mapper.AirplaneMapper;
import task.techtasks.model.AirCompany;
import task.techtasks.model.Airplane;
import task.techtasks.repository.AirCompanyRepository;
import task.techtasks.repository.AirplaneRepository;

@Service
@RequiredArgsConstructor
public class AirplaneServiceImpl implements AirplaneService {
    private final AirplaneRepository airplaneRepository;
    private final AirplaneMapper airplaneMapper;
    private final AirCompanyRepository airCompanyRepository;

    @Override
    public AirplaneDto save(CreateAirplaneRequest request) {
        AirCompany airCompany = airCompanyRepository.findById(request.getAirCompanyId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find airCompany by id "
                                + request.getAirCompanyId()));

        Airplane airplane = airplaneMapper.toModel(request);
        airplane.setAirCompany(airCompany);
        return airplaneMapper.toDto(airplaneRepository.save(airplane));
    }

    @Override
    public AirplaneDto saveWithOutAirCompany(CreateAirplaneWithOutAirCompanyRequest request) {
        Airplane airPlane = airplaneMapper.toModelWithOutAirCompany(request);
        return airplaneMapper.toDto(airplaneRepository.save(airPlane));
    }

    @Override
    public AirplaneDto updateAirplaneCompany(Long id, UpdateAirplaneCompanyRequest request) {
        Airplane existedAirplane = airplaneRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find airplane by id " + id));
        AirCompany airCompany = airCompanyRepository.findById(request.getAirCompanyId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find airCompany by id "
                                + request.getAirCompanyId()));

        existedAirplane.setAirCompany(airCompany);
        return airplaneMapper.toDto(airplaneRepository.save(existedAirplane));
    }

    @Override
    public AirplaneDto moveAirplaneBetweenCompanies(Long id, Long airCompanyId) {
        Airplane airplane = airplaneRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find airplane by id " + id));
        AirCompany airCompany = airCompanyRepository.findById(airCompanyId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find airCompany by id " + airCompanyId));
        airplane.setAirCompany(airCompany);
        airplaneRepository.save(airplane);
        return airplaneMapper.toDto(airplane);
    }

    @Override
    public List<AirplaneDto> findAll(Pageable pageable) {
        return airplaneRepository.findAll(pageable)
                .stream()
                .map(airplaneMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        airplaneRepository.deleteById(id);
    }

    @Override
    public AirplaneDto findById(Long id) {
        Airplane airplane = airplaneRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find airplane by id " + id));
        return airplaneMapper.toDto(airplane);
    }
}

package task.techtasks.service.airplane;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

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

@ExtendWith(MockitoExtension.class)
class AirplaneServiceImplTest {
    private static final Long PLANE_ID = 1L;
    private static final Long NOT_EXISTED_PLANE_ID = 50L;
    private static final Long AIR_COMPANY_ID = 1L;
    private static final Long NEW_AIR_COMPANY_ID = 2L;

    @Mock
    private AirplaneRepository airplaneRepository;

    @Mock
    private AirplaneMapper airplaneMapper;

    @Mock
    private AirCompanyRepository airCompanyRepository;

    @InjectMocks
    private AirplaneServiceImpl airplaneService;

    @Test
    public void testGetAllAirplanes() {
        Airplane airplane = new Airplane();
        Pageable pageable = PageRequest.of(0, 10);
        List<Airplane> airplanes = List.of(new Airplane());
        List<AirplaneDto> expectedPlanes = List.of(new AirplaneDto());
        Page<Airplane> page = new PageImpl<>(airplanes, pageable, airplanes.size());

        when(airplaneRepository.findAll(pageable)).thenReturn(page);

        when(airplaneMapper.toDto(airplane)).thenReturn(new AirplaneDto());

        List<AirplaneDto> result = airplaneService.findAll(pageable);

        Assertions.assertEquals(expectedPlanes.size(), result.size());
    }

    @Test
    public void findAirplaneById() {
        Airplane airplane = new Airplane();
        airplane.setId(PLANE_ID);
        AirplaneDto airplaneDto = new AirplaneDto();
        airplaneDto.setId(PLANE_ID);

        when(airplaneRepository.findById(PLANE_ID)).thenReturn(Optional.of(airplane));

        when(airplaneMapper.toDto(airplane)).thenReturn(airplaneDto);

        AirplaneDto result = airplaneService.findById(PLANE_ID);

        Assertions.assertEquals(airplane.getId(), result.getId());
    }

    @Test
    public void testFindAirplaneNonExistedId() {
        when(airplaneRepository.findById(NOT_EXISTED_PLANE_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> airplaneService.findById(NOT_EXISTED_PLANE_ID));
    }

    @Test
    public void testDeleteAirplaneById() {
        airplaneService.deleteById(PLANE_ID);

        when(airplaneRepository.findById(PLANE_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> airplaneService.findById(PLANE_ID));
    }

    @Test
    public void testSuccessfulSave() {
        CreateAirplaneRequest request = new CreateAirplaneRequest();
        request.setAirCompanyId(AIR_COMPANY_ID);
        AirCompany airCompany = new AirCompany();
        Airplane airplaneToSave = new Airplane();
        airCompany.setId(AIR_COMPANY_ID);

        when(airCompanyRepository.findById(AIR_COMPANY_ID)).thenReturn(Optional.of(airCompany));

        when(airplaneMapper.toModel(request)).thenReturn(airplaneToSave);

        airplaneToSave.setAirCompany(airCompany);

        when(airplaneRepository.save(airplaneToSave)).thenReturn(airplaneToSave);

        AirplaneDto airplaneDto = new AirplaneDto();

        when(airplaneMapper.toDto(airplaneToSave)).thenReturn(airplaneDto);

        AirplaneDto result = airplaneService.save(request);

        assertNotNull(result);
    }

    @Test
    public void testSuccessfulSaveWithOutAirCompany() {
        CreateAirplaneWithOutAirCompanyRequest request =
                new CreateAirplaneWithOutAirCompanyRequest();
        AirplaneDto airplaneDto = new AirplaneDto();
        Airplane airplaneToSave = new Airplane();

        when(airplaneMapper.toModelWithOutAirCompany(request)).thenReturn(airplaneToSave);

        when(airplaneRepository.save(airplaneToSave)).thenReturn(airplaneToSave);

        when(airplaneMapper.toDto(airplaneToSave)).thenReturn(airplaneDto);

        AirplaneDto result = airplaneService.saveWithOutAirCompany(request);

        assertNotNull(result);
    }

    @Test
    public void testUpdateAirplane() {
        UpdateAirplaneCompanyRequest request = new UpdateAirplaneCompanyRequest();
        request.setAirCompanyId(NEW_AIR_COMPANY_ID);

        Airplane existedAirplane = new Airplane();
        existedAirplane.setId(PLANE_ID);

        AirCompany airCompany = new AirCompany();
        airCompany.setId(NEW_AIR_COMPANY_ID);

        when(airplaneRepository.findById(PLANE_ID)).thenReturn(Optional.of(existedAirplane));

        when(airCompanyRepository.findById(NEW_AIR_COMPANY_ID)).thenReturn(Optional.of(airCompany));

        when(airplaneRepository.save(existedAirplane)).thenReturn(existedAirplane);

        AirplaneDto expectedResult = new AirplaneDto();
        expectedResult.setId(PLANE_ID);
        expectedResult.setAirCompanyId(NEW_AIR_COMPANY_ID);
        when(airplaneMapper.toDto(existedAirplane)).thenReturn(expectedResult);

        AirplaneDto updatedAirplane = airplaneService.updateAirplaneCompany(PLANE_ID, request);

        Assertions.assertEquals(updatedAirplane.getAirCompanyId(),
                expectedResult.getAirCompanyId());
    }

    @Test
    public void testMoveAirplaneBetweenCompanies() {
        Airplane airplane = new Airplane();
        airplane.setId(PLANE_ID);

        AirplaneDto expectedResult = new AirplaneDto();
        expectedResult.setId(PLANE_ID);
        expectedResult.setAirCompanyId(NEW_AIR_COMPANY_ID);

        AirCompany airCompany = new AirCompany();
        airCompany.setId(NEW_AIR_COMPANY_ID);

        when(airplaneRepository.findById(PLANE_ID)).thenReturn(Optional.of(airplane));

        when(airCompanyRepository.findById(NEW_AIR_COMPANY_ID)).thenReturn(Optional.of(airCompany));
        airplane.setAirCompany(airCompany);

        when(airplaneMapper.toDto(airplane)).thenReturn(expectedResult);

        AirplaneDto updatedAirplane =
                airplaneService.moveAirplaneBetweenCompanies(PLANE_ID, NEW_AIR_COMPANY_ID);

        Assertions.assertEquals(updatedAirplane.getAirCompanyId(),
                expectedResult.getAirCompanyId());
    }
}

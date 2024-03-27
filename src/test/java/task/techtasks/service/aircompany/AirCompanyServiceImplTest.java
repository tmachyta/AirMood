package task.techtasks.service.aircompany;

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
import task.techtasks.dto.aircompany.AirCompanyDto;
import task.techtasks.dto.aircompany.CreateAirCompanyRequest;
import task.techtasks.dto.aircompany.UpdateAirCompanyRequest;
import task.techtasks.exception.EntityNotFoundException;
import task.techtasks.mapper.AirCompanyMapper;
import task.techtasks.model.AirCompany;
import task.techtasks.model.AirCompany.AirCompanyType;
import task.techtasks.repository.AirCompanyRepository;

@ExtendWith(MockitoExtension.class)
class AirCompanyServiceImplTest {
    private static final String UPDATED = "Updated";
    private static final AirCompanyType AIR_COMPANY_TYPE = AirCompanyType.LUXURY;
    private static final String OLD_NAME = "Old";
    private static final AirCompanyType AIR_COMPANY_TYPE_PREVIOUS = AirCompanyType.CHARTER;
    private static final Long AIR_COMPANY_ID = 1L;
    private static final Long NON_EXISTED_AIR_COMPANY_ID = 50L;

    @Mock
    private AirCompanyRepository airCompanyRepository;

    @Mock
    private AirCompanyMapper airCompanyMapper;

    @InjectMocks
    private AirCompanyServiceImpl airCompanyService;

    @Test
    public void testSuccessfulSave() {
        CreateAirCompanyRequest request = new CreateAirCompanyRequest();
        AirCompanyDto airCompanyDto = new AirCompanyDto();
        AirCompany airCompanyToSave = new AirCompany();

        when(airCompanyMapper.toModel(request)).thenReturn(airCompanyToSave);

        when(airCompanyRepository.save(airCompanyToSave)).thenReturn(airCompanyToSave);

        when(airCompanyMapper.toDto(airCompanyToSave)).thenReturn(airCompanyDto);

        AirCompanyDto result = airCompanyService.save(request);

        assertNotNull(result);
    }

    @Test
    public void testGetAllAirCompanies() {
        AirCompany airCompany = new AirCompany();
        Pageable pageable = PageRequest.of(0, 10);
        List<AirCompany> airCompanies = List.of(new AirCompany());
        List<AirCompanyDto> expectedCompanies = List.of(new AirCompanyDto());
        Page<AirCompany> page = new PageImpl<>(airCompanies, pageable, airCompanies.size());

        when(airCompanyRepository.findAll(pageable)).thenReturn(page);

        when(airCompanyMapper.toDto(airCompany)).thenReturn(new AirCompanyDto());

        List<AirCompanyDto> result = airCompanyService.getAll(pageable);

        Assertions.assertEquals(expectedCompanies.size(), result.size());
    }

    @Test
    public void findAirCompanyById() {
        AirCompany airCompany = new AirCompany();
        airCompany.setId(AIR_COMPANY_ID);
        AirCompanyDto airCompanyDto = new AirCompanyDto();
        airCompanyDto.setId(AIR_COMPANY_ID);

        when(airCompanyRepository.findById(AIR_COMPANY_ID)).thenReturn(Optional.of(airCompany));

        when(airCompanyMapper.toDto(airCompany)).thenReturn(airCompanyDto);

        AirCompanyDto result = airCompanyService.findById(AIR_COMPANY_ID);

        Assertions.assertEquals(airCompany.getId(), result.getId());
    }

    @Test
    public void testFindAirCompanyNonExistedId() {
        when(airCompanyRepository.findById(NON_EXISTED_AIR_COMPANY_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> airCompanyService.findById(NON_EXISTED_AIR_COMPANY_ID));
    }

    @Test
    public void testDeleteAirCompanyById() {
        airCompanyService.deleteById(AIR_COMPANY_ID);

        when(airCompanyRepository.findById(AIR_COMPANY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> airCompanyService.findById(AIR_COMPANY_ID));
    }

    @Test
    public void testUpdateAirCompany() {
        UpdateAirCompanyRequest request = new UpdateAirCompanyRequest();
        request.setName(UPDATED);
        request.setType(AIR_COMPANY_TYPE);

        AirCompanyDto expectedResult = new AirCompanyDto();
        expectedResult.setName(UPDATED);
        expectedResult.setType(AIR_COMPANY_TYPE);

        AirCompany airCompany = new AirCompany();
        airCompany.setName(OLD_NAME);
        airCompany.setType(AIR_COMPANY_TYPE_PREVIOUS);

        when(airCompanyRepository.findById(AIR_COMPANY_ID)).thenReturn(Optional.of(airCompany));

        when(airCompanyRepository.save(airCompany)).thenReturn(airCompany);

        when(airCompanyMapper.toDto(airCompany)).thenReturn(expectedResult);

        AirCompanyDto updatedAirCompany = airCompanyService.updateById(AIR_COMPANY_ID, request);

        Assertions.assertEquals(updatedAirCompany.getName(), expectedResult.getName());

        Assertions.assertEquals(updatedAirCompany.getType(), expectedResult.getType());
    }
}

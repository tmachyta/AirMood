package task.techtasks.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import task.techtasks.config.MapperConfig;
import task.techtasks.dto.aircompany.AirCompanyDto;
import task.techtasks.dto.aircompany.CreateAirCompanyRequest;
import task.techtasks.dto.aircompany.UpdateAirCompanyRequest;
import task.techtasks.model.AirCompany;

@Mapper(config = MapperConfig.class)
public interface AirCompanyMapper {
    AirCompanyDto toDto(AirCompany airCompany);

    @Mapping(target = "id", ignore = true)
    AirCompany toModel(CreateAirCompanyRequest request);

    @Mapping(target = "id", ignore = true)
    AirCompany toUpdateModel(UpdateAirCompanyRequest request);
}

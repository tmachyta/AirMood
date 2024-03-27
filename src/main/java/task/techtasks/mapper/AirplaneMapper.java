package task.techtasks.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import task.techtasks.config.MapperConfig;
import task.techtasks.dto.airplane.AirplaneDto;
import task.techtasks.dto.airplane.CreateAirplaneRequest;
import task.techtasks.dto.airplane.CreateAirplaneWithOutAirCompanyRequest;
import task.techtasks.dto.airplane.UpdateAirplaneCompanyRequest;
import task.techtasks.model.Airplane;

@Mapper(config = MapperConfig.class)
public interface AirplaneMapper {
    AirplaneDto toDto(Airplane airplane);

    @Mapping(target = "id", ignore = true)
    Airplane toModel(CreateAirplaneRequest request);

    @Mapping(target = "id", ignore = true)
    Airplane toModelWithOutAirCompany(CreateAirplaneWithOutAirCompanyRequest request);

    @Mapping(target = "id", ignore = true)
    Airplane toModelUpdateAirCompany(UpdateAirplaneCompanyRequest request);
}

package task.techtasks.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import task.techtasks.config.MapperConfig;
import task.techtasks.dto.flight.CreateFlightRequest;
import task.techtasks.dto.flight.FlightDto;
import task.techtasks.dto.flight.UpdateFlightStatusActive;
import task.techtasks.dto.flight.UpdateFlightStatusCompleted;
import task.techtasks.dto.flight.UpdateFlightStatusDelayed;
import task.techtasks.model.Flight;

@Mapper(config = MapperConfig.class)
public interface FlightMapper {
    FlightDto toDto(Flight flight);

    @Mapping(target = "id", ignore = true)
    Flight toModel(CreateFlightRequest request);

    @Mapping(target = "id", ignore = true)
    Flight toUpdateDelayed(UpdateFlightStatusDelayed request);

    @Mapping(target = "id", ignore = true)
    Flight toUpdateActive(UpdateFlightStatusActive request);

    @Mapping(target = "id", ignore = true)
    Flight toUpdateCompleted(UpdateFlightStatusCompleted request);
}

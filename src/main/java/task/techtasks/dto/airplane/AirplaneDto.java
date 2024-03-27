package task.techtasks.dto.airplane;

import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;
import task.techtasks.model.Airplane.AirplaneType;

@Data
@Accessors(chain = true)
public class AirplaneDto {
    private Long id;
    private String name;
    private String factorySerialNumber;
    private Long airCompanyId;
    private int numberOfFlights;
    private int flightDistance;
    private int fuelCapacity;
    private AirplaneType type;
    private LocalDate createdAt;
}

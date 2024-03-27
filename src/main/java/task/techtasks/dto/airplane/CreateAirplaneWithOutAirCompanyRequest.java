package task.techtasks.dto.airplane;

import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;
import task.techtasks.model.Airplane;

@Data
@Accessors(chain = true)
public class CreateAirplaneWithOutAirCompanyRequest {
    private String name;
    private String factorySerialNumber;
    private int numberOfFlights;
    private int flightDistance;
    private int fuelCapacity;
    private Airplane.AirplaneType type;
    private LocalDate createdAt;
}

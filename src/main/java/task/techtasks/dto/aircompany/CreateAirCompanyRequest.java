package task.techtasks.dto.aircompany;

import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;
import task.techtasks.model.AirCompany;

@Data
@Accessors(chain = true)
public class CreateAirCompanyRequest {
    private String name;
    private AirCompany.AirCompanyType type;
    private LocalDate foundedAt;
}

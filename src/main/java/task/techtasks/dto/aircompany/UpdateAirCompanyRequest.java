package task.techtasks.dto.aircompany;

import lombok.Data;
import lombok.experimental.Accessors;
import task.techtasks.model.AirCompany.AirCompanyType;

@Data
@Accessors(chain = true)
public class UpdateAirCompanyRequest {
    private String name;
    private AirCompanyType type;
}

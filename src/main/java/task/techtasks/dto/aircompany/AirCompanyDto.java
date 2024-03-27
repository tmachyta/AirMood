package task.techtasks.dto.aircompany;

import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;
import task.techtasks.model.AirCompany.AirCompanyType;

@Data
@Accessors(chain = true)
public class AirCompanyDto {
    private Long id;
    private String name;
    private AirCompanyType type;
    private LocalDate foundedAt;
}

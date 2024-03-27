package task.techtasks.dto.airplane;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateAirplaneCompanyRequest {
    private Long airCompanyId;
}

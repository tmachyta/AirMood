package task.techtasks.dto.flight;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UpdateFlightStatusActive {
    private LocalDateTime startedAt;
}

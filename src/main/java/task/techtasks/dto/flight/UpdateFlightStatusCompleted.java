package task.techtasks.dto.flight;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateFlightStatusCompleted {
    private LocalDateTime endedAt;
}

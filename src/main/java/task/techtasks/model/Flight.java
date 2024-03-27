package task.techtasks.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE flights SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "flights")
@Data
public class Flight {
    public enum FlightStatus {
        ACTIVE,
        COMPLETED,
        DELAYED,
        PENDING
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private FlightStatus flightStatus;
    @ManyToOne
    @JoinColumn(name = "air_company_id")
    private AirCompany airCompany;
    @ManyToOne
    @JoinColumn(name = "airplane_id")
    private Airplane airplane;
    private String departureCountry;
    private String destinationCountry;
    private int distance;
    private int estimatedFlightTime;
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    @Column(name = "ended_at")
    private LocalDateTime endedAt;
    @Column(name = "delay_started_at")
    private LocalDateTime delayStartedAt;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "is_deleted")
    private boolean isDeleted;
}

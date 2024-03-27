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
import java.time.LocalDate;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE airplanes SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "airplanes")
@Data
public class Airplane {
    public enum AirplaneType {
        PASSENGER,
        CARGO,
        PRIVATE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String factorySerialNumber;
    @ManyToOne
    @JoinColumn(name = "air_company_id")
    private AirCompany airCompany;
    private int numberOfFlights;
    private int flightDistance;
    private int fuelCapacity;
    @Enumerated(value = EnumType.STRING)
    private AirplaneType type;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column(name = "is_deleted")
    private boolean isDeleted;
}

package task.techtasks.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE air_companies SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "air_companies")
@Data
public class AirCompany {
    public enum AirCompanyType {
        LOW_COST,
        LUXURY,
        CARGO,
        CHARTER,
        COMMERCIAL,
        REGIONAL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(value = EnumType.STRING)
    private AirCompanyType type;
    @Column(name = "founded_at")
    private LocalDate foundedAt;
    @Column(name = "is_deleted")
    private boolean isDeleted;
}

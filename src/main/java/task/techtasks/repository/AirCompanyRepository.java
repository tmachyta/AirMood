package task.techtasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import task.techtasks.model.AirCompany;

@Repository
public interface AirCompanyRepository extends JpaRepository<AirCompany, Long> {
}

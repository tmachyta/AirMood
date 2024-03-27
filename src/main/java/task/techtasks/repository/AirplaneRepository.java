package task.techtasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import task.techtasks.model.Airplane;

@Repository
public interface AirplaneRepository extends JpaRepository<Airplane, Long> {
}

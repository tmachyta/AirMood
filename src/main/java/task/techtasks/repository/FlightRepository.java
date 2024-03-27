package task.techtasks.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import task.techtasks.model.Flight;
import task.techtasks.model.Flight.FlightStatus;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT DISTINCT fl FROM Flight fl "
            + "JOIN fl.airCompany ac "
            + "WHERE ac.name = :companyName AND fl.flightStatus = :status")
    List<Flight> findFlightsByAirCompanyNameAndFlightStatus(
            @Param("companyName") String companyName,
            @Param("status") FlightStatus status);

    @Query("SELECT DISTINCT fl FROM Flight fl "
            + "WHERE fl.flightStatus = 'ACTIVE' "
            + "AND fl.startedAt < :time")
    List<Flight> findFlightsByActiveStatusAndStartedAt(@Param("time")LocalDateTime time);

    @Query("SELECT DISTINCT fl FROM Flight fl "
            + "WHERE fl.flightStatus = 'COMPLETED' "
            + "AND TIMESTAMPDIFF(day, fl.startedAt, fl.endedAt) > fl.estimatedFlightTime")
    List<Flight> findFlightsByCompletedStatusAndDifference();
}

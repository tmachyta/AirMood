package task.techtasks.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import task.techtasks.dto.flight.CreateFlightRequest;
import task.techtasks.dto.flight.FlightDto;
import task.techtasks.dto.flight.UpdateFlightStatusActive;
import task.techtasks.dto.flight.UpdateFlightStatusCompleted;
import task.techtasks.dto.flight.UpdateFlightStatusDelayed;
import task.techtasks.model.Flight.FlightStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FlightControllerTest {
    protected static MockMvc mockMvc;
    private static final Long DEFAULT_ID = 1L;
    private static final Long SECOND_ID = 2L;
    private static final Long THIRD_ID = 3L;
    private static final Long FOURTH_ID = 4L;
    private static final Long FIFTH_ID = 5L;
    private static final FlightStatus ACTIVE_STATUS = FlightStatus.ACTIVE;
    private static final FlightStatus COMPLETED_STATUS = FlightStatus.COMPLETED;
    private static final FlightStatus DELAYED_STATUS = FlightStatus.DELAYED;
    private static final FlightStatus PENDING_STATUS = FlightStatus.PENDING;
    private static final String DEPARTURE_COUNTRY = "Ukraine";
    private static final String DESTINATION_COUNTRY = "Spain";
    private static final Long AIR_COMPANY_ID = 1L;
    private static final Long AIR_PLANE_ID = 1L;
    private static final int DISTANCE = 2000;
    private static final int ESTIMATED_FLIGHT = 2;
    private static final LocalDateTime STARTED_AT = LocalDateTime.parse("2024-03-25T12:30:00");
    private static final LocalDateTime ENDED_AT = LocalDateTime.parse("2024-03-25T14:30:00");
    private static final LocalDateTime DELAY_STARTED_AT =
            LocalDateTime.parse("2024-03-25T12:40:00");
    private static final LocalDateTime CREATED_AT = LocalDateTime.parse("2024-03-24T12:30:00");
    private static final LocalDateTime NEW_ENDED_AT = LocalDateTime.parse("2024-03-30T14:30");
    private static final String COMPANY_NAME = "Company1";

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/aircompany/add-default-air-companies.sql")
            );
        }
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/airplane/add-default-airplanes.sql")
            );
        }
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/flight/add-default-flights.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/flight/delete-from-flights.sql")
            );
        }
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/airplane/delete-from-airplanes.sql")
            );
        }
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/aircompany/delete-from-air-companies.sql")
            );
        }
    }

    @Test
    @Sql(
            scripts = "classpath:database/flight/delete-test-flight.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Create a new flight")
    void createFlight_ValidRequest_Success() throws Exception {
        CreateFlightRequest request = createFlightRequest(
                PENDING_STATUS,
                DEPARTURE_COUNTRY,
                DESTINATION_COUNTRY,
                AIR_COMPANY_ID,
                AIR_PLANE_ID,
                DISTANCE,
                ESTIMATED_FLIGHT,
                STARTED_AT,
                ENDED_AT,
                DELAY_STARTED_AT,
                CREATED_AT
        );

        FlightDto expected = createExpectedFlightDto(
                DEFAULT_ID,
                PENDING_STATUS,
                DEPARTURE_COUNTRY,
                DESTINATION_COUNTRY,
                AIR_COMPANY_ID,
                AIR_PLANE_ID,
                DISTANCE,
                ESTIMATED_FLIGHT,
                STARTED_AT,
                ENDED_AT,
                DELAY_STARTED_AT,
                CREATED_AT
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/flights")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        FlightDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), FlightDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @Sql(
            scripts = "classpath:database/flight/add-default-flights.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/flight/delete-from-flights.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Update flight status and delayedStartedAt by flight id")
    void updateFlightByStatusDelayed() throws Exception {
        UpdateFlightStatusDelayed request =
                updateFlightStatusDelayed(DELAY_STARTED_AT);

        FlightDto updatedFlight = createExpectedFlightDto(
                DEFAULT_ID,
                DELAYED_STATUS,
                DEPARTURE_COUNTRY,
                DESTINATION_COUNTRY,
                AIR_COMPANY_ID,
                AIR_PLANE_ID,
                DISTANCE,
                ESTIMATED_FLIGHT,
                STARTED_AT,
                ENDED_AT,
                DELAY_STARTED_AT,
                CREATED_AT
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(put("/flights/delayed/{id}", DEFAULT_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        FlightDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), FlightDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(updatedFlight, actual, "id");
    }

    @Test
    @Sql(
            scripts = "classpath:database/flight/add-default-flights.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/flight/delete-from-flights.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Update flight status and startedAt by flight id")
    void updateFlightByStatusActive() throws Exception {
        UpdateFlightStatusActive request =
                updateFlightStatusActive(STARTED_AT);

        FlightDto updatedFlight = createExpectedFlightDto(
                FOURTH_ID,
                ACTIVE_STATUS,
                DEPARTURE_COUNTRY,
                DESTINATION_COUNTRY,
                AIR_COMPANY_ID,
                AIR_PLANE_ID,
                DISTANCE,
                ESTIMATED_FLIGHT,
                STARTED_AT,
                ENDED_AT,
                DELAY_STARTED_AT,
                CREATED_AT
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(put("/flights/active/{id}", 4L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        FlightDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), FlightDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(updatedFlight, actual, "id");
    }

    @Test
    @Sql(
            scripts = "classpath:database/flight/delete-from-flights.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/flight/add-default-flights.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Update flight status and endedAt by flight id")
    void updateFlightByStatusCompleted() throws Exception {
        UpdateFlightStatusCompleted request =
                updateFlightStatusCompleted(ENDED_AT);

        FlightDto updatedFlight = createExpectedFlightDto(
                DEFAULT_ID,
                COMPLETED_STATUS,
                DEPARTURE_COUNTRY,
                DESTINATION_COUNTRY,
                AIR_COMPANY_ID,
                AIR_PLANE_ID,
                DISTANCE,
                ESTIMATED_FLIGHT,
                STARTED_AT,
                ENDED_AT,
                DELAY_STARTED_AT,
                CREATED_AT
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(put("/flights/completed/{id}", DEFAULT_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        FlightDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), FlightDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(updatedFlight, actual, "id");
    }

    @Test
    @Sql(
            scripts = "classpath:database/flight/delete-from-flights.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/flight/add-default-flights.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Get list of flights")
    void getAll_GivenFlightsInCatalog() throws Exception {
        FlightDto firstFlight = createExpectedFlightDto(
                DEFAULT_ID,
                PENDING_STATUS,
                DEPARTURE_COUNTRY,
                DESTINATION_COUNTRY,
                AIR_COMPANY_ID,
                AIR_PLANE_ID,
                DISTANCE,
                ESTIMATED_FLIGHT,
                STARTED_AT,
                ENDED_AT,
                DELAY_STARTED_AT,
                CREATED_AT
        );
        FlightDto secondFlight = createExpectedFlightDto(
                SECOND_ID,
                PENDING_STATUS,
                DEPARTURE_COUNTRY,
                DESTINATION_COUNTRY,
                AIR_COMPANY_ID,
                AIR_PLANE_ID,
                DISTANCE,
                ESTIMATED_FLIGHT,
                STARTED_AT,
                ENDED_AT,
                DELAY_STARTED_AT,
                CREATED_AT
        );

        FlightDto thirdFlight = createExpectedFlightDto(
                THIRD_ID,
                DELAYED_STATUS,
                DEPARTURE_COUNTRY,
                DESTINATION_COUNTRY,
                AIR_COMPANY_ID,
                AIR_PLANE_ID,
                DISTANCE,
                ESTIMATED_FLIGHT,
                STARTED_AT,
                ENDED_AT,
                DELAY_STARTED_AT,
                CREATED_AT
        );

        FlightDto fourthFlight = createExpectedFlightDto(
                FOURTH_ID,
                ACTIVE_STATUS,
                DEPARTURE_COUNTRY,
                DESTINATION_COUNTRY,
                AIR_COMPANY_ID,
                AIR_PLANE_ID,
                DISTANCE,
                ESTIMATED_FLIGHT,
                STARTED_AT,
                ENDED_AT,
                DELAY_STARTED_AT,
                CREATED_AT
        );

        FlightDto fifthFlight = createExpectedFlightDto(
                FIFTH_ID,
                COMPLETED_STATUS,
                DEPARTURE_COUNTRY,
                DESTINATION_COUNTRY,
                AIR_COMPANY_ID,
                AIR_PLANE_ID,
                DISTANCE,
                ESTIMATED_FLIGHT,
                STARTED_AT,
                NEW_ENDED_AT,
                DELAY_STARTED_AT,
                CREATED_AT
        );

        List<FlightDto> expected = new ArrayList<>();
        expected.add(firstFlight);
        expected.add(secondFlight);
        expected.add(thirdFlight);
        expected.add(fourthFlight);
        expected.add(fifthFlight);

        MvcResult result = mockMvc.perform(get("/flights")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        FlightDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), FlightDto[].class);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @Sql(
            scripts = "classpath:database/flight/delete-from-flights.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/flight/add-default-flights.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Get flight by air company and status")
    void testGetFlightByAirCompanyNameAndFlightStatus() throws Exception {
        FlightDto flight = createExpectedFlightDto(
                DEFAULT_ID,
                PENDING_STATUS,
                DEPARTURE_COUNTRY,
                DESTINATION_COUNTRY,
                AIR_COMPANY_ID,
                AIR_PLANE_ID,
                DISTANCE,
                ESTIMATED_FLIGHT,
                STARTED_AT,
                ENDED_AT,
                DELAY_STARTED_AT,
                CREATED_AT
        );

        FlightDto secondFlight = createExpectedFlightDto(
                SECOND_ID,
                PENDING_STATUS,
                DEPARTURE_COUNTRY,
                DESTINATION_COUNTRY,
                AIR_COMPANY_ID,
                AIR_PLANE_ID,
                DISTANCE,
                ESTIMATED_FLIGHT,
                STARTED_AT,
                ENDED_AT,
                DELAY_STARTED_AT,
                CREATED_AT
        );

        List<FlightDto> expected = new ArrayList<>();
        expected.add(flight);
        expected.add(secondFlight);

        MvcResult result = mockMvc.perform(get("/flights/{companyName}/status", COMPANY_NAME)
                        .param("status", String.valueOf(FlightStatus.PENDING))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        FlightDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), FlightDto[].class);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @Sql(
            scripts = "classpath:database/flight/delete-from-flights.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Get flights with status Active and started more than 24 hours")
    void testGetFlightsWithStatusActiveAndStartedMoreThan() throws Exception {
        FlightDto flight = createExpectedFlightDto(
                FOURTH_ID,
                ACTIVE_STATUS,
                DEPARTURE_COUNTRY,
                DESTINATION_COUNTRY,
                AIR_COMPANY_ID,
                AIR_PLANE_ID,
                DISTANCE,
                ESTIMATED_FLIGHT,
                STARTED_AT,
                ENDED_AT,
                DELAY_STARTED_AT,
                CREATED_AT
        );

        List<FlightDto> expected = new ArrayList<>();
        expected.add(flight);

        MvcResult result = mockMvc.perform(get("/flights/status/active")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        FlightDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), FlightDto[].class);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @Sql(
            scripts = "classpath:database/flight/delete-from-flights.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/flight/add-default-flights.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Get flights with status completed and time difference")
    void testGetFlightsWithCompletedStatusAndTimeDifference() throws Exception {
        FlightDto flight = createExpectedFlightDto(
                FIFTH_ID,
                COMPLETED_STATUS,
                DEPARTURE_COUNTRY,
                DESTINATION_COUNTRY,
                AIR_COMPANY_ID,
                AIR_PLANE_ID,
                DISTANCE,
                ESTIMATED_FLIGHT,
                STARTED_AT,
                NEW_ENDED_AT,
                DELAY_STARTED_AT,
                CREATED_AT
        );

        List<FlightDto> expected = new ArrayList<>();
        expected.add(flight);

        MvcResult result = mockMvc.perform(get("/flights/status/completed")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        FlightDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), FlightDto[].class);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @DisplayName("Get flight by id")
    @Sql(
            scripts = "classpath:database/flight/delete-from-flights.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/flight/add-default-flights.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void getFlightById() throws Exception {
        FlightDto expected = createExpectedFlightDto(
                DEFAULT_ID,
                PENDING_STATUS,
                DEPARTURE_COUNTRY,
                DESTINATION_COUNTRY,
                AIR_COMPANY_ID,
                AIR_PLANE_ID,
                DISTANCE,
                ESTIMATED_FLIGHT,
                STARTED_AT,
                ENDED_AT,
                DELAY_STARTED_AT,
                CREATED_AT
        );

        MvcResult result = mockMvc.perform(get("/flights/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        FlightDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), FlightDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Soft-Delete flight by id")
    void testDeleteFlightById() throws Exception {
        MvcResult result = mockMvc.perform(delete("/flights/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    private FlightDto createExpectedFlightDto(
            Long id,
            FlightStatus flightStatus,
            String departureCountry,
            String destinationCountry,
            Long airCompanyId,
            Long airplaneId,
            int distance,
            int estimatedFlightTime,
            LocalDateTime startedAt,
            LocalDateTime endedAt,
            LocalDateTime delayStartedAt,
            LocalDateTime createdAt) {
        return new FlightDto()
                .setId(id)
                .setFlightStatus(flightStatus)
                .setDepartureCountry(departureCountry)
                .setDestinationCountry(destinationCountry)
                .setAirCompanyId(airCompanyId)
                .setAirplaneId(airplaneId)
                .setDistance(distance)
                .setEstimatedFlightTime(estimatedFlightTime)
                .setStartedAt(startedAt)
                .setEndedAt(endedAt)
                .setDelayStartedAt(delayStartedAt)
                .setCreatedAt(createdAt);
    }

    private CreateFlightRequest createFlightRequest(
            FlightStatus flightStatus,
            String departureCountry,
            String destinationCountry,
            Long airCompanyId,
            Long airplaneId,
            int distance,
            int estimatedFlightTime,
            LocalDateTime startedAt,
            LocalDateTime endedAt,
            LocalDateTime delayStartedAt,
            LocalDateTime createdAt
    ) {
        return new CreateFlightRequest()
                .setFlightStatus(flightStatus)
                .setDepartureCountry(departureCountry)
                .setDestinationCountry(destinationCountry)
                .setAirCompanyId(airCompanyId)
                .setAirplaneId(airplaneId)
                .setDistance(distance)
                .setEstimatedFlightTime(estimatedFlightTime)
                .setStartedAt(startedAt)
                .setEndedAt(endedAt)
                .setDelayStartedAt(delayStartedAt)
                .setCreatedAt(createdAt);
    }

    private UpdateFlightStatusActive updateFlightStatusActive(
            LocalDateTime startedAt) {
        return new UpdateFlightStatusActive()
                .setStartedAt(startedAt);
    }

    private UpdateFlightStatusCompleted updateFlightStatusCompleted(
            LocalDateTime endedAt) {
        return new UpdateFlightStatusCompleted()
                .setEndedAt(endedAt);
    }

    private UpdateFlightStatusDelayed updateFlightStatusDelayed(
            LocalDateTime delayStartedAt) {
        return new UpdateFlightStatusDelayed()
                .setDelayStartedAt(delayStartedAt);
    }
}

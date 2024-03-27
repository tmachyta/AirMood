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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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
import task.techtasks.dto.airplane.AirplaneDto;
import task.techtasks.dto.airplane.CreateAirplaneRequest;
import task.techtasks.dto.airplane.CreateAirplaneWithOutAirCompanyRequest;
import task.techtasks.dto.airplane.UpdateAirplaneCompanyRequest;
import task.techtasks.model.Airplane.AirplaneType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AirplaneControllerTest {
    protected static MockMvc mockMvc;
    private static final Long DEFAULT_ID = 1L;
    private static final Long SECOND_ID = 2L;
    private static final Long FIRST_AIR_COMPANY_ID = 1L;
    private static final Long SECOND_AIR_COMPANY_ID = 2L;
    private static final String FIRST_NAME = "Plane1";
    private static final String SECOND_NAME = "Plane2";
    private static final String FIRST_NUMBER = "255NLA";
    private static final String SECOND_NUMBER = "254NLA";
    private static final int NUMBER_FLIGHTS = 10;
    private static final int FLIGHT_DISTANCE = 1000;
    private static final int FUEL_CAPACITY = 150;
    private static final AirplaneType FIRST_TYPE = AirplaneType.CARGO;
    private static final AirplaneType SECOND_TYPE = AirplaneType.PRIVATE;
    private static final LocalDate DATE = LocalDate.parse("2002-11-05");

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
    /*@Sql(
            scripts = "classpath:database/aircompany/add-default-air-companies.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/aircompany/delete-from-air-companies.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )*/
    @Sql(
            scripts = "classpath:database/airplane/delete-test-airplane.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Create a new airplane")
    void createAirPlane_ValidRequest_Success() throws Exception {
        CreateAirplaneRequest request = createAirplaneRequest(
                FIRST_NAME,
                FIRST_NUMBER,
                FIRST_AIR_COMPANY_ID,
                NUMBER_FLIGHTS,
                FLIGHT_DISTANCE,
                FUEL_CAPACITY,
                FIRST_TYPE,
                DATE
        );

        AirplaneDto expected = createExpectedAirCompanyDto(
                DEFAULT_ID,
                FIRST_NAME,
                FIRST_NUMBER,
                FIRST_AIR_COMPANY_ID,
                NUMBER_FLIGHTS,
                FLIGHT_DISTANCE,
                FUEL_CAPACITY,
                FIRST_TYPE,
                DATE
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/airplanes")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AirplaneDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), AirplaneDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Disabled
    @Test
    @DisplayName("Get list of airplanes")
    void getAll_GivenAirPlanesInCatalog() throws Exception {
        AirplaneDto firstAirplane = createExpectedAirCompanyDto(
                DEFAULT_ID,
                FIRST_NAME,
                FIRST_NUMBER,
                FIRST_AIR_COMPANY_ID,
                NUMBER_FLIGHTS,
                FLIGHT_DISTANCE,
                FUEL_CAPACITY,
                FIRST_TYPE,
                DATE
        );
        AirplaneDto secondAirplane = createExpectedAirCompanyDto(
                SECOND_ID,
                SECOND_NAME,
                SECOND_NUMBER,
                FIRST_AIR_COMPANY_ID,
                NUMBER_FLIGHTS,
                FLIGHT_DISTANCE,
                FUEL_CAPACITY,
                SECOND_TYPE,
                DATE
        );

        List<AirplaneDto> expected = new ArrayList<>();
        expected.add(firstAirplane);
        expected.add(secondAirplane);
        MvcResult result = mockMvc.perform(get("/airplanes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AirplaneDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), AirplaneDto[].class);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @Sql(
            scripts = "classpath:database/airplane/delete-test-airplane-without-company.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Create a new airplane without company")
    void createAirplane_WithOut_Air_Company() throws Exception {
        CreateAirplaneWithOutAirCompanyRequest request = createAirplaneWithOutAirCompanyRequest(
                FIRST_NAME,
                FIRST_NUMBER,
                NUMBER_FLIGHTS,
                FLIGHT_DISTANCE,
                FUEL_CAPACITY,
                FIRST_TYPE,
                DATE
        );

        AirplaneDto expected = createExpectedAirCompanyDto(
                DEFAULT_ID,
                FIRST_NAME,
                FIRST_NUMBER,
                null,
                NUMBER_FLIGHTS,
                FLIGHT_DISTANCE,
                FUEL_CAPACITY,
                FIRST_TYPE,
                DATE
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/airplanes/without-company")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AirplaneDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), AirplaneDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Get airplane by id")
    @Sql(
            scripts = "classpath:database/airplane/delete-from-airplanes.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getAirplaneById() throws Exception {
        AirplaneDto expected = createExpectedAirCompanyDto(
                DEFAULT_ID,
                FIRST_NAME,
                FIRST_NUMBER,
                FIRST_AIR_COMPANY_ID,
                NUMBER_FLIGHTS,
                FLIGHT_DISTANCE,
                FUEL_CAPACITY,
                FIRST_TYPE,
                DATE
        );

        MvcResult result = mockMvc.perform(get("/airplanes/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AirplaneDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), AirplaneDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Soft-Delete airplane by id")
    void testDeleteById() throws Exception {
        MvcResult result = mockMvc.perform(delete("/airplanes/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Disabled
    @DisplayName("move airplanes between companies by id")
    void moveAirplanesBetweenCompanies() throws Exception {
        AirplaneDto expected = createExpectedAirCompanyDto(
                DEFAULT_ID,
                FIRST_NAME,
                FIRST_NUMBER,
                SECOND_AIR_COMPANY_ID,
                NUMBER_FLIGHTS,
                FLIGHT_DISTANCE,
                FUEL_CAPACITY,
                FIRST_TYPE,
                DATE
        );

        MvcResult result = mockMvc.perform(put("/airplanes/{id}/move", DEFAULT_ID)
                        .param("airCompanyId", String.valueOf(SECOND_AIR_COMPANY_ID))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AirplaneDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), AirplaneDto.class);

        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @DisplayName("Update airplane by id")
    void updateAirplaneByAddingAirCompanyId() throws Exception {
        UpdateAirplaneCompanyRequest request = updateAirplaneCompanyRequest(
                FIRST_AIR_COMPANY_ID);

        AirplaneDto updatedAirplane = createExpectedAirCompanyDto(
                DEFAULT_ID,
                FIRST_NAME,
                FIRST_NUMBER,
                FIRST_AIR_COMPANY_ID,
                NUMBER_FLIGHTS,
                FLIGHT_DISTANCE,
                FUEL_CAPACITY,
                FIRST_TYPE,
                DATE
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(put("/airplanes/{id}", DEFAULT_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AirplaneDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), AirplaneDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(updatedAirplane, actual, "id");
    }

    private AirplaneDto createExpectedAirCompanyDto(
            Long id,
            String name,
            String factorySerialNumber,
            Long airCompanyId,
            int numberOfFlights,
            int flightDistance,
            int fuelCapacity,
            AirplaneType type,
            LocalDate createdAt) {
        return new AirplaneDto()
                .setId(id)
                .setName(name)
                .setFactorySerialNumber(factorySerialNumber)
                .setAirCompanyId(airCompanyId)
                .setNumberOfFlights(numberOfFlights)
                .setFlightDistance(flightDistance)
                .setFuelCapacity(fuelCapacity)
                .setType(type)
                .setCreatedAt(createdAt);
    }

    private CreateAirplaneRequest createAirplaneRequest(
            String name,
            String factorySerialNumber,
            Long airCompanyId,
            int numberOfFlights,
            int flightDistance,
            int fuelCapacity,
            AirplaneType type,
            LocalDate createdAt
    ) {
        return new CreateAirplaneRequest()
                .setName(name)
                .setFactorySerialNumber(factorySerialNumber)
                .setAirCompanyId(airCompanyId)
                .setNumberOfFlights(numberOfFlights)
                .setFlightDistance(flightDistance)
                .setFuelCapacity(fuelCapacity)
                .setType(type)
                .setCreatedAt(createdAt);
    }

    private CreateAirplaneWithOutAirCompanyRequest createAirplaneWithOutAirCompanyRequest(
            String name,
            String factorySerialNumber,
            int numberOfFlights,
            int flightDistance,
            int fuelCapacity,
            AirplaneType type,
            LocalDate createdAt
    ) {
        return new CreateAirplaneWithOutAirCompanyRequest()
                .setName(name)
                .setFactorySerialNumber(factorySerialNumber)
                .setNumberOfFlights(numberOfFlights)
                .setFlightDistance(flightDistance)
                .setFuelCapacity(fuelCapacity)
                .setType(type)
                .setCreatedAt(createdAt);
    }

    private UpdateAirplaneCompanyRequest updateAirplaneCompanyRequest(Long airCompanyId) {
        return new UpdateAirplaneCompanyRequest().setAirCompanyId(airCompanyId);
    }
}

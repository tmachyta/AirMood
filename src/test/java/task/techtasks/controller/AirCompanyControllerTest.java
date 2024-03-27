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
import task.techtasks.dto.aircompany.AirCompanyDto;
import task.techtasks.dto.aircompany.CreateAirCompanyRequest;
import task.techtasks.dto.aircompany.UpdateAirCompanyRequest;
import task.techtasks.model.AirCompany.AirCompanyType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AirCompanyControllerTest {
    protected static MockMvc mockMvc;
    private static final String FIRST_COMPANY = "Company1";
    private static final String SECOND_COMPANY = "Company2";
    private static final AirCompanyType TYPE = AirCompanyType.LUXURY;
    private static final AirCompanyType UPDATED_TYPE = AirCompanyType.CARGO;
    private static final LocalDate FOUNDED_AT = LocalDate.parse("2002-11-05");
    private static final Long DEFAULT_ID = 1L;
    private static final Long SECOND_ID = 2L;

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
                    new ClassPathResource("database/aircompany/delete-from-air-companies.sql")
            );
        }
    }

    @Test
    @Sql(
            scripts = "classpath:database/aircompany/delete-test-air-company.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Create a new air company")
    void createAirCompany_ValidRequestDto_Success() throws Exception {
        CreateAirCompanyRequest request = createAirCompanyRequest(
                FIRST_COMPANY, TYPE, FOUNDED_AT);

        AirCompanyDto expected = createExpectedAirCompanyDto(
                DEFAULT_ID, FIRST_COMPANY, TYPE, FOUNDED_AT);

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/air-companies")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AirCompanyDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), AirCompanyDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Get list of air companies")
    void getAll_GivenAirCompaniesInCatalog() throws Exception {
        AirCompanyDto firstCompany = createExpectedAirCompanyDto(
                DEFAULT_ID, SECOND_COMPANY, UPDATED_TYPE, FOUNDED_AT);
        AirCompanyDto secondCompany = createExpectedAirCompanyDto(
                SECOND_ID, SECOND_COMPANY, TYPE, FOUNDED_AT);
        List<AirCompanyDto> expected = new ArrayList<>();
        expected.add(firstCompany);
        expected.add(secondCompany);

        MvcResult result = mockMvc.perform(get("/air-companies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AirCompanyDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), AirCompanyDto[].class);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @DisplayName("Get air company by id")
    @Sql(
            scripts = "classpath:database/aircompany/delete-from-air-companies.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getAirCompanyById() throws Exception {
        AirCompanyDto expected = createExpectedAirCompanyDto(
                DEFAULT_ID, FIRST_COMPANY, TYPE, FOUNDED_AT);
        MvcResult result = mockMvc.perform(get("/air-companies/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AirCompanyDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), AirCompanyDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Soft-Delete air company by id")
    void testDeleteById() throws Exception {
        MvcResult result = mockMvc.perform(delete("/air-companies/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("Update air company by id")
    void updateAirCompanyById() throws Exception {
        UpdateAirCompanyRequest request = createUpdateAirCompanyRequest(
                SECOND_COMPANY, UPDATED_TYPE);
        AirCompanyDto updatedAirCompany = createExpectedAirCompanyDto(
                DEFAULT_ID, SECOND_COMPANY, UPDATED_TYPE, FOUNDED_AT);

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(put("/air-companies/{id}", DEFAULT_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AirCompanyDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), AirCompanyDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(updatedAirCompany, actual, "id");
    }

    private AirCompanyDto createExpectedAirCompanyDto(
            Long id, String name, AirCompanyType type, LocalDate foundedAt) {
        return new AirCompanyDto()
                .setId(id)
                .setName(name)
                .setType(type)
                .setFoundedAt(foundedAt);
    }

    private CreateAirCompanyRequest createAirCompanyRequest(
            String name, AirCompanyType type, LocalDate foundedAt) {
        return new CreateAirCompanyRequest()
                .setName(name)
                .setType(type)
                .setFoundedAt(foundedAt);
    }

    private UpdateAirCompanyRequest createUpdateAirCompanyRequest(
            String name, AirCompanyType type) {
        return new UpdateAirCompanyRequest()
                .setName(name)
                .setType(type);
    }
}

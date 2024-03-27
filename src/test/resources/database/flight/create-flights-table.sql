CREATE TABLE IF NOT EXISTS flights (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       flight_status VARCHAR(50) NOT NULL,
    air_company_id BIGINT,
    airplane_id BIGINT,
    departure_country VARCHAR(255) NOT NULL,
    destination_country VARCHAR(255) NOT NULL,
    distance INT NOT NULL,
    estimated_flight_time INT NOT NULL,
    started_at DATETIME,
    ended_at DATETIME,
    delay_started_at DATETIME,
    created_at DATETIME NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    FOREIGN KEY (air_company_id) REFERENCES air_companies(id),
    FOREIGN KEY (airplane_id) REFERENCES airplanes(id)
    );

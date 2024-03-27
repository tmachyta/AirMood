CREATE TABLE IF NOT EXISTS airplanes (
                                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                         name VARCHAR(255) NOT NULL,
    factory_serial_number VARCHAR(255) NOT NULL,
    air_company_id BIGINT,
    number_of_flights INT NOT NULL,
    flight_distance INT NOT NULL,
    fuel_capacity INT NOT NULL,
    type VARCHAR(50) NOT NULL,
    created_at DATE NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    FOREIGN KEY (air_company_id) REFERENCES air_companies(id)
    );

CREATE TABLE IF NOT EXISTS air_companies (
                                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    founded_at DATE NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false
    );

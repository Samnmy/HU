CREATE TABLE venues (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(500),
    city VARCHAR(100),
    capacity INT,
    created_at TIMESTAMP
);

CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    venue_id BIGINT,
    created_at TIMESTAMP,
    CONSTRAINT fk_event_venue FOREIGN KEY (venue_id) REFERENCES venues(id)
);

CREATE TABLE event_categories (
    event_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (event_id, category_id),
    CONSTRAINT fk_event_category_event FOREIGN KEY (event_id) REFERENCES events(id),
    CONSTRAINT fk_event_category_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE INDEX idx_event_venue ON events(venue_id);
CREATE INDEX idx_event_status ON events(status);
CREATE INDEX idx_event_dates ON events(start_date, end_date);
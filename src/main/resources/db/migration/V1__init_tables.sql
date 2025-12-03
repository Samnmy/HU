-- Create venues table
CREATE TABLE IF NOT EXISTS venues (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    location VARCHAR(200) NOT NULL,
    city VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create categories table
CREATE TABLE IF NOT EXISTS categories (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200)
);

-- Create events table
CREATE TABLE IF NOT EXISTS events (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    venue_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    FOREIGN KEY (venue_id) REFERENCES venues(id) ON DELETE CASCADE,
    CHECK (end_date > start_date)
);

-- Create event_categories table
CREATE TABLE IF NOT EXISTS event_categories (
    event_id UUID NOT NULL,
    category_id UUID NOT NULL,
    PRIMARY KEY (event_id, category_id),
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

-- Create users table for authentication
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ATTENDEE',
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default categories
INSERT INTO categories (id, name, description) VALUES
('11111111-1111-1111-1111-111111111111', 'Music', 'Music concerts and festivals'),
('22222222-2222-2222-2222-222222222222', 'Sports', 'Sporting events'),
('33333333-3333-3333-3333-333333333333', 'Theater', 'Theater plays and performances'),
('44444444-4444-4444-4444-444444444444', 'Conference', 'Conferences and seminars'),
('55555555-5555-5555-5555-555555555555', 'Workshop', 'Workshops and training sessions');

-- Insert default venues
INSERT INTO venues (id, name, location, city, capacity) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Madison Square Garden', '4 Pennsylvania Plaza', 'New York', 20000),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Staples Center', '1111 S Figueroa St', 'Los Angeles', 19000),
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Royal Albert Hall', 'Kensington Gore', 'London', 5272);

-- Insert default users
INSERT INTO users (id, username, email, password_hash, role) VALUES
('dddddddd-dddd-dddd-dddd-dddddddddddd', 'admin', 'admin@eventmanagement.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lB5XJdR1qLQKqu', 'ADMIN'),
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'organizer', 'organizer@eventmanagement.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lB5XJdR1qLQKqu', 'ORGANIZER'),
('ffffffff-ffff-ffff-ffff-ffffffffffff', 'attendee', 'attendee@eventmanagement.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lB5XJdR1qLQKqu', 'ATTENDEE');

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_events_venue_id ON events(venue_id);
CREATE INDEX IF NOT EXISTS idx_events_status ON events(status);
CREATE INDEX IF NOT EXISTS idx_events_date_range ON events(start_date, end_date);
CREATE INDEX IF NOT EXISTS idx_venues_city ON venues(city);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
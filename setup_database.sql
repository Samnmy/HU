-- 1. Limpiar datos existentes
DELETE FROM event_categories;
DELETE FROM events;
DELETE FROM venues;
DELETE FROM categories;
DELETE FROM users;

-- 2. Insertar Venues (recintos)
INSERT INTO venues (id, name, location, city, capacity) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Madison Square Garden', '4 Pennsylvania Plaza', 'New York', 20000),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Staples Center', '1111 S Figueroa St', 'Los Angeles', 19000),
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Royal Albert Hall', 'Kensington Gore', 'London', 5272),
('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Sydney Opera House', 'Bennelong Point', 'Sydney', 1500);

-- 3. Insertar Categorías
INSERT INTO categories (id, name, description) VALUES
('11111111-1111-1111-1111-111111111111', 'Music', 'Music concerts and festivals'),
('22222222-2222-2222-2222-222222222222', 'Sports', 'Sporting events'),
('33333333-3333-3333-3333-333333333333', 'Theater', 'Theater plays and performances'),
('44444444-4444-4444-4444-444444444444', 'Conference', 'Conferences and seminars'),
('55555555-5555-5555-5555-555555555555', 'Workshop', 'Workshops and training sessions');

-- 4. Insertar Eventos
INSERT INTO events (id, name, description, start_date, end_date, status, venue_id, created_by) VALUES
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'Rock Festival 2024', 'Annual rock music festival', '2024-06-15T18:00:00', '2024-06-15T23:00:00', 'ACTIVE', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'admin'),
('ffffffff-ffff-ffff-ffff-ffffffffffff', 'Tech Conference', 'Technology innovation conference', '2024-07-20T09:00:00', '2024-07-22T18:00:00', 'ACTIVE', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'admin'),
('aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', 'Basketball Championship', 'National basketball finals', '2024-08-10T20:00:00', '2024-08-10T22:30:00', 'ACTIVE', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 'organizer');

-- 5. Relacionar eventos con categorías
INSERT INTO event_categories (event_id, category_id) VALUES
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '11111111-1111-1111-1111-111111111111'), -- Rock Festival -> Music
('ffffffff-ffff-ffff-ffff-ffffffffffff', '44444444-4444-4444-4444-444444444444'), -- Tech Conference -> Conference
('aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '22222222-2222-2222-2222-222222222222'); -- Basketball -> Sports

-- 6. Crear usuarios con contraseña ENCRIPTADA: 'admin123'
-- La contraseña 'admin123' encriptada con BCrypt es: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lB5XJdR1qLQKqu
INSERT INTO users (id, username, email, password_hash, role) VALUES
('aaaaaaaa-1111-2222-3333-444444444444', 'admin', 'admin@eventmanagement.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lB5XJdR1qLQKqu', 'ADMIN'),
('bbbbbbbb-1111-2222-3333-444444444444', 'organizer', 'organizer@eventmanagement.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lB5XJdR1qLQKqu', 'ORGANIZER'),
('cccccccc-1111-2222-3333-444444444444', 'attendee', 'attendee@eventmanagement.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lB5XJdR1qLQKqu', 'ATTENDEE');

-- 7. Verificar datos
SELECT 'Venues:' as Table_Name, COUNT(*) as Count FROM venues
UNION ALL
SELECT 'Events:', COUNT(*) FROM events
UNION ALL
SELECT 'Categories:', COUNT(*) FROM categories
UNION ALL
SELECT 'Users:', COUNT(*) FROM users
UNION ALL
SELECT 'Event Categories:', COUNT(*) FROM event_categories;
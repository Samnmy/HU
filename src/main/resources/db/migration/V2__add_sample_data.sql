-- Insert sample venues
INSERT INTO venues (name, address, city, capacity, created_at) VALUES
('Auditorio Nacional', 'Av. Paseo de la Reforma 50', 'Ciudad de México', 10000, NOW()),
('Estadio Azteca', 'Calzada de Tlalpan 3465', 'Ciudad de México', 87000, NOW()),
('Teatro de la Ciudad', 'Donceles 36', 'Ciudad de México', 1500, NOW());

-- Insert sample categories
INSERT INTO categories (name) VALUES
('Concierto'),
('Teatro'),
('Deportes'),
('Conferencia'),
('Exposición');

-- Insert sample events
INSERT INTO events (name, description, start_date, end_date, venue_id, created_at) VALUES
('Concierto de Rock', 'Banda internacional de rock', DATEADD('DAY', 7, NOW()), DATEADD('DAY', 7, DATEADD('HOUR', 3, NOW())), 1, NOW()),
('Obra de Teatro Clásico', 'Obra clásica de Shakespeare', DATEADD('DAY', 14, NOW()), DATEADD('DAY', 14, DATEADD('HOUR', 2, NOW())), 3, NOW()),
('Partido de Fútbol', 'Liga MX final', DATEADD('DAY', 21, NOW()), DATEADD('DAY', 21, DATEADD('HOUR', 2, NOW())), 2, NOW());
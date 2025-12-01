-- DATOS DE EJEMPLO PARA VENUES
INSERT INTO venues (name, location, capacity, description) VALUES
('Auditorio Nacional', 'Ciudad de México', 10000, 'El principal recinto de espectáculos en México'),
('Teatro Metropólitan', 'CDMX Centro', 3000, 'Teatro histórico en el centro de la ciudad'),
('Arena Ciudad de México', 'Azcapotzalco', 22000, 'Recinto multipropósito para conciertos y eventos deportivos'),
('Foro Sol', 'Iztacalco', 65000, 'Estadio para conciertos masivos'),
('Palacio de los Deportes', 'Iztacalco', 20000, 'Recinto cubierto para eventos deportivos y conciertos');

-- DATOS DE EJEMPLO PARA CATEGORÍAS
INSERT INTO categories (name, description) VALUES
('Concierto', 'Eventos musicales en vivo'),
('Teatro', 'Obras teatrales y espectáculos escénicos'),
('Deportes', 'Eventos deportivos profesionales'),
('Conferencia', 'Charlas y conferencias educativas'),
('Festival', 'Festivales culturales y musicales'),
('Familiar', 'Eventos aptos para toda la familia');

-- DATOS DE EJEMPLO PARA EVENTOS
INSERT INTO events (title, description, start_date, end_date, price, available_tickets, venue_id) VALUES
('Concierto de Rock Clásico', 'Los mejores éxitos del rock de los 80s y 90s',
 TIMESTAMPADD('DAY', 30, CURRENT_TIMESTAMP),
 TIMESTAMPADD('DAY', 31, CURRENT_TIMESTAMP),
 1200.00, 5000, 1),
('Obra: El Fantasma de la Ópera', 'Clásica obra de teatro musical',
 TIMESTAMPADD('DAY', 15, CURRENT_TIMESTAMP),
 TIMESTAMPADD('DAY', 15, CURRENT_TIMESTAMP),
 800.00, 2000, 2),
('Final de Campeonato de Fútbol', 'Partido final del torneo nacional',
 TIMESTAMPADD('DAY', 10, CURRENT_TIMESTAMP),
 TIMESTAMPADD('DAY', 10, CURRENT_TIMESTAMP),
 1500.00, 15000, 3),
('Conferencia de Tecnología', 'Innovaciones tecnológicas del año',
 TIMESTAMPADD('DAY', 45, CURRENT_TIMESTAMP),
 TIMESTAMPADD('DAY', 46, CURRENT_TIMESTAMP),
 500.00, 1000, 4),
('Festival de Jazz', '3 días de jazz con artistas internacionales',
 TIMESTAMPADD('DAY', 60, CURRENT_TIMESTAMP),
 TIMESTAMPADD('DAY', 62, CURRENT_TIMESTAMP),
 2000.00, 3000, 5);

-- RELACIONAR EVENTOS CON CATEGORÍAS
INSERT INTO event_categories (event_id, category_id) VALUES
(1, 1), -- Concierto de Rock -> Concierto
(1, 6), -- Concierto de Rock -> Familiar
(2, 2), -- Fantasma de la Ópera -> Teatro
(2, 6), -- Fantasma de la Ópera -> Familiar
(3, 3), -- Final de Fútbol -> Deportes
(4, 4), -- Conferencia de Tecnología -> Conferencia
(5, 1), -- Festival de Jazz -> Concierto
(5, 5); -- Festival de Jazz -> Festival

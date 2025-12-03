-- Migration: V2__add_relations.sql
-- Description: Add foreign key constraints and indexes for relationships

-- Add foreign key constraint for events -> venues if not exists
ALTER TABLE events
ADD CONSTRAINT IF NOT EXISTS fk_events_venue
FOREIGN KEY (venue_id)
REFERENCES venues(id)
ON DELETE CASCADE;

-- Add foreign key constraint for event_categories -> events if not exists
ALTER TABLE event_categories
ADD CONSTRAINT IF NOT EXISTS fk_event_categories_event
FOREIGN KEY (event_id)
REFERENCES events(id)
ON DELETE CASCADE;

-- Add foreign key constraint for event_categories -> categories if not exists
ALTER TABLE event_categories
ADD CONSTRAINT IF NOT EXISTS fk_event_categories_category
FOREIGN KEY (category_id)
REFERENCES categories(id)
ON DELETE CASCADE;

-- Add unique constraint for event name if not exists
ALTER TABLE events
ADD CONSTRAINT IF NOT EXISTS uc_events_name
UNIQUE (name);

-- Add unique constraint for venue name if not exists
ALTER TABLE venues
ADD CONSTRAINT IF NOT EXISTS uc_venues_name
UNIQUE (name);

-- Add unique constraint for category name if not exists
ALTER TABLE categories
ADD CONSTRAINT IF NOT EXISTS uc_categories_name
UNIQUE (name);

-- Add unique constraint for username if not exists
ALTER TABLE users
ADD CONSTRAINT IF NOT EXISTS uc_users_username
UNIQUE (username);

-- Add unique constraint for email if not exists
ALTER TABLE users
ADD CONSTRAINT IF NOT EXISTS uc_users_email
UNIQUE (email);

-- Create indexes for performance optimization

-- Index for events by venue_id (for JOIN operations)
CREATE INDEX IF NOT EXISTS idx_events_venue_id
ON events(venue_id);

-- Index for events by status (for filtering)
CREATE INDEX IF NOT EXISTS idx_events_status
ON events(status);

-- Composite index for events by date range (for date queries)
CREATE INDEX IF NOT EXISTS idx_events_date_range
ON events(start_date, end_date);

-- Index for events by created_at (for sorting)
CREATE INDEX IF NOT EXISTS idx_events_created_at
ON events(created_at);

-- Index for venues by city (for filtering)
CREATE INDEX IF NOT EXISTS idx_venues_city
ON venues(city);

-- Index for venues by capacity (for range queries)
CREATE INDEX IF NOT EXISTS idx_venues_capacity
ON venues(capacity);

-- Index for users by username (for authentication)
CREATE INDEX IF NOT EXISTS idx_users_username
ON users(username);

-- Index for users by email (for authentication)
CREATE INDEX IF NOT EXISTS idx_users_email
ON users(email);

-- Index for users by role (for authorization)
CREATE INDEX IF NOT EXISTS idx_users_role
ON users(role);

-- Index for event_categories by event_id (for JOIN operations)
CREATE INDEX IF NOT EXISTS idx_event_categories_event_id
ON event_categories(event_id);

-- Index for event_categories by category_id (for JOIN operations)
CREATE INDEX IF NOT EXISTS idx_event_categories_category_id
ON event_categories(category_id);

-- Add check constraints for data integrity

-- Ensure end_date is after start_date for events
ALTER TABLE events
ADD CONSTRAINT IF NOT EXISTS chk_events_date_range
CHECK (end_date > start_date);

-- Ensure capacity is positive for venues
ALTER TABLE venues
ADD CONSTRAINT IF NOT EXISTS chk_venues_capacity_positive
CHECK (capacity > 0);

-- Ensure email format is valid for users (basic validation)
ALTER TABLE users
ADD CONSTRAINT IF NOT EXISTS chk_users_email_format
CHECK (email LIKE '%_@__%.__%');

-- Ensure role is valid for users
ALTER TABLE users
ADD CONSTRAINT IF NOT EXISTS chk_users_role_valid
CHECK (role IN ('ADMIN', 'ORGANIZER', 'ATTENDEE'));

-- Ensure event status is valid
ALTER TABLE events
ADD CONSTRAINT IF NOT EXISTS chk_events_status_valid
CHECK (status IN ('ACTIVE', 'CANCELLED', 'COMPLETED', 'PENDING'));

-- Add default values

-- Set default created_at to current timestamp for events
ALTER TABLE events
ALTER COLUMN created_at
SET DEFAULT CURRENT_TIMESTAMP;

-- Set default updated_at to current timestamp for events
ALTER TABLE events
ALTER COLUMN updated_at
SET DEFAULT CURRENT_TIMESTAMP;

-- Set default created_at to current timestamp for venues
ALTER TABLE venues
ALTER COLUMN created_at
SET DEFAULT CURRENT_TIMESTAMP;

-- Set default updated_at to current timestamp for venues
ALTER TABLE venues
ALTER COLUMN updated_at
SET DEFAULT CURRENT_TIMESTAMP;

-- Set default created_at to current timestamp for users
ALTER TABLE users
ALTER COLUMN created_at
SET DEFAULT CURRENT_TIMESTAMP;

-- Set default updated_at to current timestamp for users
ALTER TABLE users
ALTER COLUMN updated_at
SET DEFAULT CURRENT_TIMESTAMP;

-- Set default enabled to true for users
ALTER TABLE users
ALTER COLUMN enabled
SET DEFAULT TRUE;

-- Set default role to ATTENDEE for users
ALTER TABLE users
ALTER COLUMN role
SET DEFAULT 'ATTENDEE';

-- Set default status to ACTIVE for events
ALTER TABLE events
ALTER COLUMN status
SET DEFAULT 'ACTIVE';

-- Insert sample data for testing relationships

-- Sample venues if they don't exist
INSERT INTO venues (id, name, location, city, capacity)
SELECT 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Madison Square Garden', '4 Pennsylvania Plaza', 'New York', 20000
WHERE NOT EXISTS (SELECT 1 FROM venues WHERE id = 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa');

INSERT INTO venues (id, name, location, city, capacity)
SELECT 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Staples Center', '1111 S Figueroa St', 'Los Angeles', 19000
WHERE NOT EXISTS (SELECT 1 FROM venues WHERE id = 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb');

INSERT INTO venues (id, name, location, city, capacity)
SELECT 'cccccccc-cccc-cccc-cccc-cccccccccccc', 'Royal Albert Hall', 'Kensington Gore', 'London', 5272
WHERE NOT EXISTS (SELECT 1 FROM venues WHERE id = 'cccccccc-cccc-cccc-cccc-cccccccccccc');

-- Sample categories if they don't exist
INSERT INTO categories (id, name, description)
SELECT '11111111-1111-1111-1111-111111111111', 'Music', 'Music concerts and festivals'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE id = '11111111-1111-1111-1111-111111111111');

INSERT INTO categories (id, name, description)
SELECT '22222222-2222-2222-2222-222222222222', 'Sports', 'Sporting events'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE id = '22222222-2222-2222-2222-222222222222');

INSERT INTO categories (id, name, description)
SELECT '33333333-3333-3333-3333-333333333333', 'Theater', 'Theater plays and performances'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE id = '33333333-3333-3333-3333-333333333333');

-- Sample events if they don't exist
INSERT INTO events (id, name, description, start_date, end_date, status, venue_id, created_by)
SELECT 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'Rock Festival 2024', 'Annual rock music festival', '2024-06-15 18:00:00', '2024-06-15 23:00:00', 'ACTIVE', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'admin'
WHERE NOT EXISTS (SELECT 1 FROM events WHERE id = 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee');

INSERT INTO events (id, name, description, start_date, end_date, status, venue_id, created_by)
SELECT 'ffffffff-ffff-ffff-ffff-ffffffffffff', 'Tech Conference', 'Technology innovation conference', '2024-07-20 09:00:00', '2024-07-22 18:00:00', 'ACTIVE', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'admin'
WHERE NOT EXISTS (SELECT 1 FROM events WHERE id = 'ffffffff-ffff-ffff-ffff-ffffffffffff');

-- Sample event_categories relationships if they don't exist
INSERT INTO event_categories (event_id, category_id)
SELECT 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '11111111-1111-1111-1111-111111111111'
WHERE NOT EXISTS (SELECT 1 FROM event_categories WHERE event_id = 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee' AND category_id = '11111111-1111-1111-1111-111111111111');

INSERT INTO event_categories (event_id, category_id)
SELECT 'ffffffff-ffff-ffff-ffff-ffffffffffff', '33333333-3333-3333-3333-333333333333'
WHERE NOT EXISTS (SELECT 1 FROM event_categories WHERE event_id = 'ffffffff-ffff-ffff-ffff-ffffffffffff' AND category_id = '33333333-3333-3333-3333-333333333333');

-- Verification query to check all relationships are properly set up
SELECT
    'Venues' as table_name,
    COUNT(*) as record_count
FROM venues
UNION ALL
SELECT
    'Events',
    COUNT(*)
FROM events
UNION ALL
SELECT
    'Categories',
    COUNT(*)
FROM categories
UNION ALL
SELECT
    'Event Categories',
    COUNT(*)
FROM event_categories
UNION ALL
SELECT
    'Foreign Key Checks',
    (SELECT COUNT(*) FROM information_schema.table_constraints
     WHERE constraint_type = 'FOREIGN KEY'
     AND table_name IN ('events', 'event_categories'))
UNION ALL
SELECT
    'Index Checks',
    (SELECT COUNT(*) FROM information_schema.indexes
     WHERE table_name IN ('events', 'venues', 'users', 'event_categories')
     AND index_name LIKE 'idx_%');
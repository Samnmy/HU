-- V3 - Security (H2 Safe Final)

-- Create audit_log table (no FK todavía)
CREATE TABLE IF NOT EXISTS audit_log (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    user_id UUID,
    username VARCHAR(50),
    action VARCHAR(100) NOT NULL,
    resource_type VARCHAR(50),
    resource_id UUID,
    details VARCHAR(255),
    ip_address VARCHAR(45),
    user_agent VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create refresh_tokens (no FK yet)
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    user_id UUID NOT NULL,
    token VARCHAR(500) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    revoked_at TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS uc_refresh_tokens_token ON refresh_tokens(token);

-- Create user_sessions (no FK yet)
CREATE TABLE IF NOT EXISTS user_sessions (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    user_id UUID NOT NULL,
    session_token VARCHAR(500) NOT NULL,
    login_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    logout_at TIMESTAMP,
    last_activity_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent VARCHAR(255),
    active BOOLEAN DEFAULT TRUE
);

CREATE UNIQUE INDEX IF NOT EXISTS uc_user_sessions_token ON user_sessions(session_token);

-- Create permissions
CREATE TABLE IF NOT EXISTS permissions (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create role_permissions
CREATE TABLE IF NOT EXISTS role_permissions (
    role VARCHAR(20) NOT NULL,
    permission_id UUID NOT NULL,
    granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (role, permission_id)
);

-- Insert permissions (H2 MERGE to avoid duplicates)
MERGE INTO permissions (id, name, description)
KEY(name)
VALUES
(RANDOM_UUID(), 'EVENT_CREATE', 'Create new events'),
(RANDOM_UUID(), 'EVENT_READ', 'View events'),
(RANDOM_UUID(), 'EVENT_UPDATE', 'Update events'),
(RANDOM_UUID(), 'EVENT_DELETE', 'Delete events'),
(RANDOM_UUID(), 'VENUE_CREATE', 'Create new venues'),
(RANDOM_UUID(), 'VENUE_READ', 'View venues'),
(RANDOM_UUID(), 'VENUE_UPDATE', 'Update venues'),
(RANDOM_UUID(), 'VENUE_DELETE', 'Delete venues'),
(RANDOM_UUID(), 'USER_MANAGE', 'Manage users'),
(RANDOM_UUID(), 'ROLE_MANAGE', 'Manage roles and permissions'),
(RANDOM_UUID(), 'AUDIT_VIEW', 'View audit logs'),
(RANDOM_UUID(), 'REPORT_GENERATE', 'Generate reports');

-- Assign permissions to roles (simple INSERT; duplicates in-memory are ok)
INSERT INTO role_permissions (role, permission_id)
SELECT 'ADMIN', id FROM permissions;

INSERT INTO role_permissions (role, permission_id)
SELECT 'ORGANIZER', id FROM permissions
WHERE name IN (
    'EVENT_CREATE', 'EVENT_READ', 'EVENT_UPDATE', 'EVENT_DELETE',
    'VENUE_READ', 'REPORT_GENERATE'
);

INSERT INTO role_permissions (role, permission_id)
SELECT 'ATTENDEE', id FROM permissions
WHERE name IN ('EVENT_READ', 'VENUE_READ');

-- Add security columns to existing users table (users is created in V1)
ALTER TABLE users ADD COLUMN IF NOT EXISTS last_login_at TIMESTAMP;
ALTER TABLE users ADD COLUMN IF NOT EXISTS failed_login_attempts INT DEFAULT 0;
ALTER TABLE users ADD COLUMN IF NOT EXISTS account_locked_until TIMESTAMP;
ALTER TABLE users ADD COLUMN IF NOT EXISTS password_changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Create password_history
CREATE TABLE IF NOT EXISTS password_history (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    user_id UUID NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===== Add foreign keys AFTER users and permissions exist =====

ALTER TABLE audit_log
    ADD CONSTRAINT IF NOT EXISTS fk_audit_log_user FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE refresh_tokens
    ADD CONSTRAINT IF NOT EXISTS fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE user_sessions
    ADD CONSTRAINT IF NOT EXISTS fk_user_sessions_user FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE role_permissions
    ADD CONSTRAINT IF NOT EXISTS fk_role_permissions_permission FOREIGN KEY (permission_id) REFERENCES permissions(id);

ALTER TABLE password_history
    ADD CONSTRAINT IF NOT EXISTS fk_password_history_user FOREIGN KEY (user_id) REFERENCES users(id);

-- Insert default users (MERGE so it doesn't fail if already present)
MERGE INTO users (id, username, email, password_hash, role, enabled)
KEY(username)
VALUES
('aaaaaaaa-1111-2222-3333-aaaaaaaaaaaa', 'admin', 'admin@event.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lB5XJdR1qLQKqu', 'ADMIN', TRUE),
('bbbbbbbb-1111-2222-3333-bbbbbbbbbbbb', 'organizer', 'organizer@event.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lB5XJdR1qLQKqu', 'ORGANIZER', TRUE),
('cccccccc-1111-2222-3333-cccccccccccc', 'attendee', 'attendee@event.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lB5XJdR1qLQKqu', 'ATTENDEE', TRUE),
('dddddddd-1111-2222-3333-dddddddddddd', 'auditor', 'auditor@event.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lB5XJdR1qLQKqu', 'ADMIN', TRUE);

-- Some audit logs
INSERT INTO audit_log (id, user_id, username, action, resource_type, resource_id, details)
VALUES
(RANDOM_UUID(), 'aaaaaaaa-1111-2222-3333-aaaaaaaaaaaa', 'admin', 'USER_CREATED', 'USER', 'aaaaaaaa-1111-2222-3333-aaaaaaaaaaaa', 'Initial admin user created'),
(RANDOM_UUID(), 'aaaaaaaa-1111-2222-3333-aaaaaaaaaaaa', 'admin', 'USER_CREATED', 'USER', 'bbbbbbbb-1111-2222-3333-bbbbbbbbbbbb', 'Organizer user created'),
(RANDOM_UUID(), 'aaaaaaaa-1111-2222-3333-aaaaaaaaaaaa', 'admin', 'USER_CREATED', 'USER', 'cccccccc-1111-2222-3333-cccccccccccc', 'Attendee user created');

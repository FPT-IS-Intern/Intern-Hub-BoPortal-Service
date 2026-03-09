CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS ih_bo_portal.bo_admin_user (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    failed_attempt INTEGER DEFAULT 0,
    locked_until TIMESTAMP,
    last_login_at TIMESTAMP,
    role_codes TEXT,
    permission_codes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ih_bo_portal.bo_refresh_token (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES ih_bo_portal.bo_admin_user (id),
    token_hash VARCHAR(128) UNIQUE NOT NULL,
    token_jti VARCHAR(64),
    device_id VARCHAR(255),
    expires_at TIMESTAMP NOT NULL,
    revoked_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_bo_admin_user_username ON ih_bo_portal.bo_admin_user (username);
CREATE INDEX IF NOT EXISTS idx_bo_refresh_token_user_id ON ih_bo_portal.bo_refresh_token (user_id);
CREATE INDEX IF NOT EXISTS idx_bo_refresh_token_expires_at ON ih_bo_portal.bo_refresh_token (expires_at);


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


-- Default BO super admin account (idempotent)
INSERT INTO ih_bo_portal.bo_admin_user (
    username,
    password_hash,
    display_name,
    status,
    failed_attempt,
    locked_until,
    role_codes,
    permission_codes
)
VALUES (
    'admin',
    crypt('123qwe!@#4', gen_salt('bf', 12)),
    'System Administrator',
    'ACTIVE',
    0,
    NULL,
    'ROLE_SUPER_ADMIN',
    COALESCE(
        (
            SELECT string_agg(DISTINCT pm.permission_code, ',' ORDER BY pm.permission_code)
            FROM ih_bo_portal.portal_menu pm
            WHERE pm.permission_code IS NOT NULL
              AND pm.permission_code <> ''
        ),
        ''
    )
)
ON CONFLICT (username)
DO UPDATE SET
    password_hash = EXCLUDED.password_hash,
    display_name = EXCLUDED.display_name,
    status = 'ACTIVE',
    failed_attempt = 0,
    locked_until = NULL,
    role_codes = EXCLUDED.role_codes,
    permission_codes = EXCLUDED.permission_codes,
    updated_at = CURRENT_TIMESTAMP;

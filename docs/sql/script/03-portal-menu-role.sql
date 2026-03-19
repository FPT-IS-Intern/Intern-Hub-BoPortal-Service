CREATE TABLE IF NOT EXISTS ih_bo_portal.portal_menu_role (
    id BIGSERIAL PRIMARY KEY,
    menu_id INTEGER NOT NULL,
    role_code VARCHAR(100) NOT NULL,
    CONSTRAINT fk_portal_menu_role_menu
        FOREIGN KEY (menu_id) REFERENCES ih_bo_portal.portal_menu (id) ON DELETE CASCADE,
    CONSTRAINT uk_portal_menu_role UNIQUE (menu_id, role_code)
);

CREATE INDEX IF NOT EXISTS idx_portal_menu_role_menu_id
    ON ih_bo_portal.portal_menu_role (menu_id);

CREATE INDEX IF NOT EXISTS idx_portal_menu_role_role_code
    ON ih_bo_portal.portal_menu_role (role_code);

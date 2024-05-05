CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE FUNCTION generate_permission_id()
    RETURNS TEXT AS $$
DECLARE
    part1 UUID;
    part2 TEXT;
    today DATE;
BEGIN
    part1 := (SELECT substring(uuid_generate_v4()::text from 1 for 8));  -- Similar to UUID.randomUUID().toString().split("-")[0]
    today := CURRENT_DATE;
    part2 := to_char(today, 'YYYY-MM-DD');  -- Formata a data como YYYY-MM-DD
    RETURN part1 || '-PERM-' || replace(part2, '-', '');  -- Concatena as partes para formar o ID
END;
$$ LANGUAGE plpgsql;

CREATE TABLE role
(
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE authority
(
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE role_authority
(
    permission_id VARCHAR(255) DEFAULT generate_permission_id() NOT NULL UNIQUE,
    role_id       UUID NOT NULL,
    authority_id  UUID NOT NULL,
    associated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id       VARCHAR(255) NOT NULL,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES role (id),
    CONSTRAINT fk_authority FOREIGN KEY (authority_id) REFERENCES authority (id),
    CONSTRAINT pk_role_authority PRIMARY KEY (role_id, authority_id)
);

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE OR REPLACE FUNCTION generate_permission_id()
    RETURNS TEXT AS $$
DECLARE
    part1 TEXT;
    part2 TEXT;
    today DATE;
BEGIN
    part1 := substring(uuid_generate_v4()::text from 1 for 8);
    today := CURRENT_DATE;
    part2 := to_char(today, 'YYYY-MM-DD');
    RETURN part1 || '-PERM-' || part2;
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
    permission_id TEXT DEFAULT generate_permission_id() NOT NULL UNIQUE,
    role_id       UUID NOT NULL,
    authority_id  UUID NOT NULL,
    user_id       VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES role (id),
    CONSTRAINT fk_authority FOREIGN KEY (authority_id) REFERENCES authority (id),
    CONSTRAINT pk_role_authority PRIMARY KEY (role_id, authority_id)
);

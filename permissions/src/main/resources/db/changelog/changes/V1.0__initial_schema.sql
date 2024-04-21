CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE role
(
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE authority
(
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE role_authority
(
    role_id       UUID NOT NULL,
    authority_id  UUID NOT NULL,
    associated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id       VARCHAR(255) NOT NULL,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES role (id),
    CONSTRAINT fk_authority FOREIGN KEY (authority_id) REFERENCES authority (id),
    CONSTRAINT pk_role_authority PRIMARY KEY (role_id, authority_id)
);

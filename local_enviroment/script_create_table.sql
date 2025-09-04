CREATE
EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE roles
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) UNIQUE NOT NULL,
    description TEXT
);

CREATE TABLE users
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(100)        NOT NULL,
    last_name    VARCHAR(100)        NOT NULL,
    email        VARCHAR(150) UNIQUE NOT NULL,
    born_date    DATE,
    address      VARCHAR(255),
    phone_number VARCHAR(20),
    base_salary  NUMERIC(15, 2),
    identity_document VARCHAR(50) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    idrol        INTEGER REFERENCES roles (id)
);
INSERT INTO roles (name, description)
VALUES ('Admin', 'Administrator with full access'),
       ('User', 'Regular user with limited access'),
       ('adviser', 'adviser user with limited access');

INSERT INTO users(name, last_name, email, identity_document, password_hash, idrol)
VALUES ('admin', 'admin', 'admin@admin.co','123456789','$2a$10$zw0UH7NmCcVZKNPZsfGtp..oLBAPmq/aLp5XBpJqe5Z/2nvQYIwFa',1);


--------------------------------------//////////////////////////////////////////////////--------------------------------
CREATE TABLE permisos
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) UNIQUE NOT NULL,
    description TEXT
);

INSERT INTO permisos (name, description)
VALUES ('CREATE_USER', 'Creaste access'),
       ('CREATE_LOAN_APPLICATION', 'Create loan applications'),
         ('READ_LOAN_APPLICATION', 'Read loan applications'),
         ('CHANGE_LOAN_APPLICATION', 'Approve or decline loan applications');

CREATE TABLE role_permisos (
    role_id INTEGER REFERENCES roles(id),
    permiso_id INTEGER REFERENCES permisos(id),
    PRIMARY KEY (role_id, permiso_id)
);

-- Admin y adviser tienen todos los permisos
INSERT INTO role_permisos (role_id, permiso_id)
SELECT r.id, p.id
FROM roles r, permisos p
WHERE r.name IN ('Admin', 'adviser');

-- User solo tiene CREATE_LOAN_APPLICATION
INSERT INTO role_permisos (role_id, permiso_id)
SELECT r.id, p.id
FROM roles r, permisos p
WHERE r.name = 'User' AND p.name = 'CREATE_LOAN_APPLICATION';

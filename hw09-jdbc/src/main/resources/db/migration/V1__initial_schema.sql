CREATE TABLE client
(
    id   BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(50)
);

create table manager
(
    no   BIGSERIAL NOT NULL PRIMARY KEY,
    label VARCHAR(50),
    param1 VARCHAR(50)
);
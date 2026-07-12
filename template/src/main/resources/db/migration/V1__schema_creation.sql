CREATE TABLE IF NOT EXISTS shedlock
(
    name       VARCHAR(64) PRIMARY KEY,
    lock_until TIMESTAMP    NOT NULL,
    locked_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    locked_by  VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS samples
(
    id                     BIGSERIAL PRIMARY KEY,
    name                   VARCHAR(255)             NOT NULL UNIQUE,
    tracking_code          VARCHAR(64)              NOT NULL UNIQUE,
    status                 INTEGER                  NOT NULL,
    creation_date          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    last_modification_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
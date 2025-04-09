CREATE TABLE telegram_users
(
    id          BIGSERIAL PRIMARY KEY,
    chat_id     VARCHAR not null,
    username    VARCHAR not null,
    is_deleted  BOOLEAN                  DEFAULT false,
    created_by  VARCHAR NOT NULL         DEFAULT '',
    modified_by VARCHAR NOT NULL         DEFAULT '',
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    modified_at TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL
);

CREATE INDEX telegram_users_chat_id_idx ON telegram_users (chat_id);
CREATE INDEX telegram_users_username_idx ON telegram_users (username);

CREATE TABLE subscriptions
(
    id               BIGSERIAL PRIMARY KEY,
    telegram_user_id BIGINT REFERENCES telegram_users (id),
    service_name     VARCHAR NOT NULL,
    amount           DECIMAL NOT NULL,
    currency         VARCHAR                  DEFAULT NULL,
    billing_date     DATE    NOT NULL,
    is_deleted       BOOLEAN                  DEFAULT false,
    created_by       VARCHAR NOT NULL         DEFAULT '',
    modified_by      VARCHAR NOT NULL         DEFAULT '',
    created_at       TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    modified_at      TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL
);

CREATE INDEX subscriptions_telegram_user_id_idx ON subscriptions (telegram_user_id);
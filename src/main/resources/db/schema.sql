-- Loyalty & Promo Code Module Schema

CREATE TABLE IF NOT EXISTS loyalty_tier_config (
    tier_id         SERIAL          PRIMARY KEY,
    tier_name       VARCHAR(50)     NOT NULL UNIQUE,
    min_points      INTEGER         NOT NULL DEFAULT 0,
    max_points      INTEGER         NOT NULL,
    earn_multiplier DECIMAL(4,2)    NOT NULL DEFAULT 1.00,
    expiry_days     INTEGER         NOT NULL DEFAULT 365
);

CREATE TABLE IF NOT EXISTS loyalty_account (
    account_id          SERIAL          PRIMARY KEY,
    user_id             INTEGER         NOT NULL UNIQUE,
    total_points        INTEGER         NOT NULL DEFAULT 0,
    redeemable_points   INTEGER         NOT NULL DEFAULT 0,
    tier                VARCHAR(50)     NOT NULL DEFAULT 'BRONZE',
    last_updated        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version             INTEGER         NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS loyalty_transaction (
    txn_id          SERIAL          PRIMARY KEY,
    account_id      INTEGER         NOT NULL REFERENCES loyalty_account(account_id),
    booking_id      INTEGER,
    txn_type        VARCHAR(20)     NOT NULL,
    points_delta    INTEGER         NOT NULL,
    description     VARCHAR(255),
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS loyalty_redemption (
    redemption_id   SERIAL          PRIMARY KEY,
    account_id      INTEGER         NOT NULL REFERENCES loyalty_account(account_id),
    booking_id      INTEGER,
    points_used     INTEGER         NOT NULL,
    discount_given  DECIMAL(10,2)   NOT NULL,
    redeemed_at     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS coupon (
    coupon_id           SERIAL          PRIMARY KEY,
    coupon_code         VARCHAR(50)     NOT NULL UNIQUE,
    discount_type       VARCHAR(20)     NOT NULL,
    discount_amount     DECIMAL(10,2)   NOT NULL,
    min_booking_amount  DECIMAL(10,2)   NOT NULL DEFAULT 0,
    valid_from          DATE            NOT NULL,
    valid_to            DATE            NOT NULL,
    usage_limit         INTEGER         NOT NULL DEFAULT 1,
    coupon_status       VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS coupon_applicability (
    applicability_id    SERIAL      PRIMARY KEY,
    coupon_id           INTEGER     NOT NULL REFERENCES coupon(coupon_id),
    movie_id            INTEGER,
    hosting_event_id    INTEGER
);

CREATE TABLE IF NOT EXISTS coupon_usage (
    usage_id    SERIAL      PRIMARY KEY,
    coupon_id   INTEGER     NOT NULL REFERENCES coupon(coupon_id),
    user_id     INTEGER     NOT NULL,
    booking_id  INTEGER     NOT NULL,
    used_at     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for common queries
CREATE INDEX IF NOT EXISTS idx_loyalty_account_user_id ON loyalty_account(user_id);
CREATE INDEX IF NOT EXISTS idx_loyalty_txn_account_id ON loyalty_transaction(account_id);
CREATE INDEX IF NOT EXISTS idx_loyalty_txn_booking_id ON loyalty_transaction(booking_id);
CREATE INDEX IF NOT EXISTS idx_loyalty_redemption_account_id ON loyalty_redemption(account_id);
CREATE INDEX IF NOT EXISTS idx_loyalty_redemption_booking_id ON loyalty_redemption(booking_id);
CREATE INDEX IF NOT EXISTS idx_coupon_code ON coupon(coupon_code);
CREATE INDEX IF NOT EXISTS idx_coupon_usage_coupon_id ON coupon_usage(coupon_id);
CREATE INDEX IF NOT EXISTS idx_coupon_usage_user_id ON coupon_usage(user_id);
CREATE INDEX IF NOT EXISTS idx_coupon_usage_booking_id ON coupon_usage(booking_id);
CREATE INDEX IF NOT EXISTS idx_coupon_applicability_coupon_id ON coupon_applicability(coupon_id);

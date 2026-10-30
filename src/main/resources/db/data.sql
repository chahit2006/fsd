-- Seed Data for Loyalty & Promo Code Module

-- Tier configuration
INSERT INTO loyalty_tier_config (tier_name, min_points, max_points, earn_multiplier, expiry_days)
VALUES ('BRONZE', 0, 499, 1.00, 365)
ON CONFLICT (tier_name) DO NOTHING;

INSERT INTO loyalty_tier_config (tier_name, min_points, max_points, earn_multiplier, expiry_days)
VALUES ('SILVER', 500, 1999, 1.25, 365)
ON CONFLICT (tier_name) DO NOTHING;

INSERT INTO loyalty_tier_config (tier_name, min_points, max_points, earn_multiplier, expiry_days)
VALUES ('GOLD', 2000, 4999, 1.50, 540)
ON CONFLICT (tier_name) DO NOTHING;

INSERT INTO loyalty_tier_config (tier_name, min_points, max_points, earn_multiplier, expiry_days)
VALUES ('PLATINUM', 5000, 999999, 2.00, 730)
ON CONFLICT (tier_name) DO NOTHING;

-- Sample loyalty accounts (referencing external user IDs)
INSERT INTO loyalty_account (user_id, total_points, redeemable_points, tier)
VALUES (1, 1200, 800, 'SILVER')
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO loyalty_account (user_id, total_points, redeemable_points, tier)
VALUES (2, 350, 350, 'BRONZE')
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO loyalty_account (user_id, total_points, redeemable_points, tier)
VALUES (3, 5200, 3000, 'PLATINUM')
ON CONFLICT (user_id) DO NOTHING;

-- Sample coupons
INSERT INTO coupon (coupon_code, discount_type, discount_amount, min_booking_amount, valid_from, valid_to, usage_limit, coupon_status)
VALUES ('WELCOME50', 'PERCENTAGE', 50.00, 200.00, '2024-01-01', '2027-12-31', 1, 'ACTIVE')
ON CONFLICT (coupon_code) DO NOTHING;

INSERT INTO coupon (coupon_code, discount_type, discount_amount, min_booking_amount, valid_from, valid_to, usage_limit, coupon_status)
VALUES ('FLAT100', 'FLAT', 100.00, 500.00, '2024-01-01', '2027-12-31', 5, 'ACTIVE')
ON CONFLICT (coupon_code) DO NOTHING;

INSERT INTO coupon (coupon_code, discount_type, discount_amount, min_booking_amount, valid_from, valid_to, usage_limit, coupon_status)
VALUES ('EXPIRED10', 'PERCENTAGE', 10.00, 100.00, '2023-01-01', '2023-12-31', 10, 'EXPIRED')
ON CONFLICT (coupon_code) DO NOTHING;

-- Sample coupon applicability (WELCOME50 applies to movie 1 and event 1)
INSERT INTO coupon_applicability (coupon_id, movie_id, hosting_event_id)
SELECT c.coupon_id, 1, NULL FROM coupon c WHERE c.coupon_code = 'WELCOME50'
AND NOT EXISTS (SELECT 1 FROM coupon_applicability ca WHERE ca.coupon_id = c.coupon_id AND ca.movie_id = 1);

INSERT INTO coupon_applicability (coupon_id, movie_id, hosting_event_id)
SELECT c.coupon_id, NULL, 1 FROM coupon c WHERE c.coupon_code = 'WELCOME50'
AND NOT EXISTS (SELECT 1 FROM coupon_applicability ca WHERE ca.coupon_id = c.coupon_id AND ca.hosting_event_id = 1);

-- Sample loyalty transactions
INSERT INTO loyalty_transaction (account_id, booking_id, txn_type, points_delta, description, created_at)
VALUES (1, 101, 'EARN', 50, 'Points earned for booking #101', CURRENT_TIMESTAMP - INTERVAL '30 days');

INSERT INTO loyalty_transaction (account_id, booking_id, txn_type, points_delta, description, created_at)
VALUES (1, 102, 'EARN', 75, 'Points earned for booking #102', CURRENT_TIMESTAMP - INTERVAL '15 days');

INSERT INTO loyalty_transaction (account_id, booking_id, txn_type, points_delta, description, created_at)
VALUES (3, 201, 'REDEEM', -200, 'Points redeemed for booking #201', CURRENT_TIMESTAMP - INTERVAL '5 days');

-- Sample coupon usage
INSERT INTO coupon_usage (coupon_id, user_id, booking_id, used_at)
SELECT c.coupon_id, 1, 101, CURRENT_TIMESTAMP - INTERVAL '30 days'
FROM coupon c WHERE c.coupon_code = 'FLAT100';

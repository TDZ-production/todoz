ALTER TABLE user_subscription RENAME COLUMN created TO created_at;
ALTER TABLE feedback ADD created_at DATETIME;
ALTER TABLE password_reset_token ADD created_at DATETIME;
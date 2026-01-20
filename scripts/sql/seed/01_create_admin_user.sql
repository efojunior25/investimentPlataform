INSERT INTO tb_users (user_id, username, email, password, role, created_at, updated_at)
VALUES (
           UUID(),
           'Administrator',
           'admin@example.com',
           '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
           'ADMIN',
           NOW(),
           NOW()
       );
INSERT INTO settings (id, keepLoggedIn, distanceUnit) VALUES (1, 0, 0)
INSERT INTO user (id, role, email, password, name, SETTINGS_ID) VALUES (1, 'USER_ROLE', 'user@user', '$2a$10$wUO4XRXR.sN5woV6v9iFoumweRiWTMNUN2NqU6MiTH4NZSKG1lEZC', 'User User', 1) -- pass: user

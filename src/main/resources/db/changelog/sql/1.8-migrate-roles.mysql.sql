UPDATE users u
JOIN roles r ON u.role = r.name
SET u.role_id = r.role_id;

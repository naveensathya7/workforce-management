UPDATE users 
SET role_id = roles.role_id 
FROM roles 
WHERE users.role = roles.name;

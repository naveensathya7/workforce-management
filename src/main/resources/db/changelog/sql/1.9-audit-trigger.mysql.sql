DROP TRIGGER IF EXISTS trg_audit_users_delete;
CREATE TRIGGER trg_audit_users_delete
AFTER DELETE ON users
FOR EACH ROW
INSERT INTO audit_logs (action, details, performed_by, timestamp, execution_time_ms)
VALUES ('DB_DELETE_USER', JSON_OBJECT('user_id', OLD.user_id, 'username', OLD.username, 'role_id', OLD.role_id), CURRENT_USER(), CURRENT_TIMESTAMP(), 0);

DROP TRIGGER IF EXISTS trg_audit_users_update;
CREATE TRIGGER trg_audit_users_update
AFTER UPDATE ON users
FOR EACH ROW
INSERT INTO audit_logs (action, details, performed_by, timestamp, execution_time_ms)
VALUES ('DB_UPDATE_USER', JSON_OBJECT('old_role', OLD.role_id, 'new_role', NEW.role_id), CURRENT_USER(), CURRENT_TIMESTAMP(), 0);

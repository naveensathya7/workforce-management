CREATE OR REPLACE FUNCTION audit_users_changes()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO audit_logs (action, details, performed_by, timestamp, execution_time_ms)
        VALUES ('DB_DELETE_USER', row_to_json(OLD)::text, current_user, current_timestamp, 0);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO audit_logs (action, details, performed_by, timestamp, execution_time_ms)
        VALUES ('DB_UPDATE_USER', 
                'OLD: ' || row_to_json(OLD)::text || ' NEW: ' || row_to_json(NEW)::text, 
                current_user, current_timestamp, 0);
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_audit_users ON users;
CREATE TRIGGER trg_audit_users
AFTER UPDATE OR DELETE ON users
FOR EACH ROW EXECUTE FUNCTION audit_users_changes();

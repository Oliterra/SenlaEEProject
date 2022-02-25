CREATE FUNCTION courier_status_checking()
RETURNS trigger AS $BODY$
BEGIN
IF new.status = "active" OR new.status = "inactive"
THEN RETURN NEW;
ELSE RETURN NULL;
END IF;
END;
$BODY$
LANGUAGE 'plpgsql';
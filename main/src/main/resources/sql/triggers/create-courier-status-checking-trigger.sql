CREATE TRIGGER courier_status_checking
BEFORE INSERT OR UPDATE
ON Couriers
FOR EACH ROW
EXECUTE PROCEDURE courier_status_checking();
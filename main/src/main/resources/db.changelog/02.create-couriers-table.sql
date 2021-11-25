CREATE TABLE couriers (
    id SERIAL,
    first_name VARCHAR(15) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    phone VARCHAR(13) NOT NULL,
    CONSTRAINT couriers_phone_check CHECK(phone != ''),
	CONSTRAINT couriers_phone_key UNIQUE (phone),
	CONSTRAINT couriers_pkey PRIMARY KEY (id)
);
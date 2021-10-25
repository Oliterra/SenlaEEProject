CREATE TABLE Couriers (
    CourierId SERIAL,
    FirstName VARCHAR(15) NOT NULL,
    LastName VARCHAR(20) NOT NULL,
    Phone VARCHAR(13) NOT NULL,
    CONSTRAINT couriers_phone_check CHECK(Phone != ''),
	CONSTRAINT couriers_phone_key UNIQUE (Phone),
	CONSTRAINT couriers_pkey PRIMARY KEY (CourierId)
);

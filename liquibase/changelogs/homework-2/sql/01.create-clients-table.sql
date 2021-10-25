CREATE TABLE Clients (
     ClientId SERIAL,
     FirstName VARCHAR(15) NOT NULL,
     LastName VARCHAR(20) NOT NULL,
     Phone VARCHAR(13) NOT NULL,
     Email VARCHAR(30) NOT NULL,
     Address VARCHAR(255) NOT NULL,
     CONSTRAINT clients_check CHECK((Email != '') AND (Phone != '') AND (Address != '')),
     CONSTRAINT clients_email_key UNIQUE (Email),
     CONSTRAINT clients_phone_key UNIQUE (Phone),
     CONSTRAINT clients_pkey PRIMARY KEY (ClientId)
);
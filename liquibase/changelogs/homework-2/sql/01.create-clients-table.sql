CREATE TABLE clients (
     id SERIAL,
     first_name VARCHAR(15) NOT NULL,
     last_name VARCHAR(20) NOT NULL,
     phone VARCHAR(13) NOT NULL,
     email VARCHAR(30) NOT NULL,
     address VARCHAR(255) NOT NULL,
     CONSTRAINT clients_check CHECK((email != '') AND (phone != '') AND (address != '')),
     CONSTRAINT clients_email_key UNIQUE (email),
     CONSTRAINT clients_phone_key UNIQUE (phone),
     CONSTRAINT clients_pkey PRIMARY KEY (id)
);
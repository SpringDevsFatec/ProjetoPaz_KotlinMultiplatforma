/* Inserindo enderço de fornecedores */
INSERT INTO supplier_address (cep, street, number, complement, quartier, status) VALUES ('12345678', 'Rua A', '104', 'casa', 'Bairro A', 1);
INSERT INTO supplier_address (cep, street, number, complement, quartier, status) VALUES ('12345678', 'Rua B', '104', 'casa', 'Bairro B', 1);
INSERT INTO supplier_address (cep, street, number, complement, quartier, status) VALUES ('12345678', 'Rua C', '104', 'casa', 'Bairro C', 1);

/* Inserindo fornecedores */
INSERT INTO supplier (name, contact_name, phone, email, active, address_id) VALUES ('Fornecedor A', 'For', '12123412345', 'forn@gmail.com', 1, 1);
INSERT INTO supplier (name, contact_name, phone, email, active, address_id) VALUES ('Fornecedor B', 'Frn', '12123412345', 'forn@gmail.com', 0, 2);
INSERT INTO supplier (name, contact_name, phone, email, active, address_id) VALUES ('Fornecedor C', 'Fon', '12123412345', 'forn@gmail.com', 1, 3);
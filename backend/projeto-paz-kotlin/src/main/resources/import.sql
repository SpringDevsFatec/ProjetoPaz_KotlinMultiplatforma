/* Inserindo telefone de forncedores */
INSERT INTO supplier_cellphone (country_number, ddd1, ddd2, cellphone1, cellphone2) VALUES ('12', '12', '12', '123451234', '123451234');
INSERT INTO supplier_cellphone (country_number, ddd1, ddd2, cellphone1, cellphone2) VALUES ('12', '12', '12', '123451234', '123451234');
INSERT INTO supplier_cellphone (country_number, ddd1, ddd2, cellphone1, cellphone2) VALUES ('12', '12', '12', '123451234', '123451234');

/* Inserindo enderço de fornecedores */
INSERT INTO supplier_address (cep, street, number, complement, quartier, status) VALUES ('12345678', 'Rua A', '104', 'casa', 'Bairro A', 1);
INSERT INTO supplier_address (cep, street, number, complement, quartier, status) VALUES ('12345678', 'Rua B', '104', 'casa', 'Bairro B', 1);
INSERT INTO supplier_address (cep, street, number, complement, quartier, status) VALUES ('12345678', 'Rua C', '104', 'casa', 'Bairro C', 1);

/* Inserindo fornecedores */
INSERT INTO supplier (name, contact_name, email, active, cnpj, type, observation, occupation, create_user, update_user, phone_id, address_id) VALUES ('Fornecedor A', 'For', 'forn@gmail.com', 1, '123', 'tipo A', 'obs A', 'ocupação A', 1, 1, 1, 1);
INSERT INTO supplier (name, contact_name, email, active, cnpj, type, observation, occupation, create_user, update_user, phone_id, address_id) VALUES ('Fornecedor B', 'Frn', 'forn@gmail.com', 0, '123', 'tipo B', 'obs B', 'ocupação B', 1, 1, 2, 2);
INSERT INTO supplier (name, contact_name, email, active, cnpj, type, observation, occupation, create_user, update_user, phone_id, address_id) VALUES ('Fornecedor C', 'Fon', 'forn@gmail.com', 1, '123', 'tipo C', 'obs C', 'ocupação C', 1, 1, 3, 3);
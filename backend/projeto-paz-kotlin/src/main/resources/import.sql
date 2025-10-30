/* supplier cellphones */
INSERT INTO supplier_cellphone (country_number, ddd1, ddd2, cellphone1, cellphone2) VALUES ('12', '12', '12', '123451234', '123451234');
INSERT INTO supplier_cellphone (country_number, ddd1, ddd2, cellphone1, cellphone2) VALUES ('12', '12', '12', '123451234', '123451234');
INSERT INTO supplier_cellphone (country_number, ddd1, ddd2, cellphone1, cellphone2) VALUES ('12', '12', '12', '123451234', '123451234');

/* supplier addresses */
INSERT INTO supplier_address (cep, street, number, complement, quartier, status) VALUES ('12345678', 'Rua A', '104', 'casa', 'Bairro A', 1);
INSERT INTO supplier_address (cep, street, number, complement, quartier, status) VALUES ('12345678', 'Rua B', '104', 'casa', 'Bairro B', 1);
INSERT INTO supplier_address (cep, street, number, complement, quartier, status) VALUES ('12345678', 'Rua C', '104', 'casa', 'Bairro C', 1);

/* suppliers */
INSERT INTO supplier (name, contact_name, email, active, cnpj, type, observation, occupation, create_user, update_user, phone_id, address_id) VALUES ('Fornecedor A', 'For', 'forn@gmail.com', 1, '123', 'tipo A', 'obs A', 'ocupação A', 1, null, 1, 1);
INSERT INTO supplier (name, contact_name, email, active, cnpj, type, observation, occupation, create_user, update_user, phone_id, address_id) VALUES ('Fornecedor B', 'Frn', 'forn@gmail.com', 0, '123', 'tipo B', 'obs B', 'ocupação B', 1, null, 2, 2);
INSERT INTO supplier (name, contact_name, email, active, cnpj, type, observation, occupation, create_user, update_user, phone_id, address_id) VALUES ('Fornecedor C', 'Fon', 'forn@gmail.com', 1, '123', 'tipo C', 'obs C', 'ocupação C', 1, null, 3, 3);

/* categories */
INSERT INTO category(name, type, image_url, image_alt_text, description, active, create_user, update_user) VALUES ('Categoria A', 'Bebida', 'url', 'bebida', 'descricao', 1, 1, null);
INSERT INTO category(name, type, image_url, image_alt_text, description, active, create_user, update_user) VALUES ('Categoria B', 'Alimento', 'url', 'alimento', 'descricao', 1, 1, null);
INSERT INTO category(name, type, image_url, image_alt_text, description, active, create_user, update_user) VALUES ('Categoria C', 'Roupa', 'url', 'roupa', 'descricao', 1, 1, null);
INSERT INTO category(name, type, image_url, image_alt_text, description, active, create_user, update_user) VALUES ('Categoria D', 'Roupa', 'url', 'roupa', 'descricao', 0, 1, null);

/* stocks */
INSERT INTO stock(quantity, maturity, fabrication) VALUES(2, '2025/10/24', '2025/12/24');
INSERT INTO stock(quantity, maturity, fabrication) VALUES(2, '2025/10/24', '2025/12/24');
INSERT INTO stock(quantity, maturity, fabrication) VALUES(2, '2025/10/24', '2025/12/24');
INSERT INTO stock(quantity, maturity, fabrication) VALUES(2, '2025/10/24', '2025/12/24');

/* products */
INSERT INTO product(name, cost_price, sale_price, description, is_favorite, is_donation, active, supplier_id, stock_id) VALUES('Produto A', 10, 15, 'descricao', 1, 0, 1, 1, 1);
INSERT INTO product(name, cost_price, sale_price, description, is_favorite, is_donation, active, supplier_id, stock_id) VALUES('Produto B', 12, 15, 'descricao', 1, 0, 1, 2, 2);
INSERT INTO product(name, cost_price, sale_price, description, is_favorite, is_donation, active, supplier_id, stock_id) VALUES('Produto C', 14, 24, 'descricao', 1, 0, 0, 3, 3);
INSERT INTO product(name, cost_price, sale_price, description, is_favorite, is_donation, active, supplier_id, stock_id) VALUES('Produto D', 14, 24, 'descricao', 1, 0, 0, 1, 4);

/* products and categories */
INSERT INTO product_category(product_id, category_id) VALUES(1, 1);
INSERT INTO product_category(product_id, category_id) VALUES(2, 2);
INSERT INTO product_category(product_id, category_id) VALUES(3, 3);

/* products images */
INSERT INTO product_image(url, active, alt_text, is_favorite, product_id) VALUES ('URL DA IMAGEM', 1, 'texto alternativo', 1, 1);
INSERT INTO product_image(url, active, alt_text, is_favorite, product_id) VALUES ('URL DA IMAGEM', 1, 'texto alternativo', 1, 2);
INSERT INTO product_image(url, active, alt_text, is_favorite, product_id) VALUES ('URL DA IMAGEM', 1, 'texto alternativo', 1, 3);
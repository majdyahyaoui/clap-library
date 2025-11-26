-- Insert initial authors
INSERT INTO authors (first_name, last_name) VALUES
('Victor', 'Hugo'),
('Alexandre', 'Dumas'),
('Jules', 'Verne'),
('Émile', 'Zola');

-- Insert initial books
INSERT INTO books (title, price, publication_date, author_id) VALUES
('Les Misérables', 12.50, '1862-04-03', (SELECT id FROM authors WHERE last_name = 'Hugo' AND first_name = 'Victor')),
('Notre-Dame de Paris', 10.99, '1831-03-16', (SELECT id FROM authors WHERE last_name = 'Hugo' AND first_name = 'Victor')),
('Le Comte de Monte-Cristo', 15.00, '1844-08-28', (SELECT id FROM authors WHERE last_name = 'Dumas' AND first_name = 'Alexandre')),
('Les Trois Mousquetaires', 13.50, '1844-03-01', (SELECT id FROM authors WHERE last_name = 'Dumas' AND first_name = 'Alexandre')),
('Vingt Mille Lieues sous les mers', 11.00, '1870-01-01', (SELECT id FROM authors WHERE last_name = 'Verne' AND first_name = 'Jules')),
('Le Tour du monde en quatre-vingts jours', 9.50, '1873-01-01', (SELECT id FROM authors WHERE last_name = 'Verne' AND first_name = 'Jules')),
('Germinal', 14.00, '1885-03-01', (SELECT id FROM authors WHERE last_name = 'Zola' AND first_name = 'Émile'));

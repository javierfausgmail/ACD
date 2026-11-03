ALTER TABLE libros ADD COLUMN autor VARCHAR(120);

UPDATE libros SET autor = 'Pendiente' WHERE autor IS NULL;

ALTER TABLE libros ALTER COLUMN autor SET NOT NULL;

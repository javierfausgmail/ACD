ALTER TABLE libros ADD COLUMN estanteria VARCHAR(30);

UPDATE libros SET estanteria = 'Sin asignar' WHERE estanteria IS NULL;

ALTER TABLE libros ALTER COLUMN estanteria SET NOT NULL;

ALTER TABLE empleados ADD COLUMN email VARCHAR(150) NULL;

-- Dirección provisional única para cada empleado que ya existía.
UPDATE empleados
SET email = CONCAT('empleado', id, '@plantilla.example.test')
WHERE email IS NULL;

ALTER TABLE empleados MODIFY COLUMN email VARCHAR(150) NOT NULL;
ALTER TABLE empleados ADD CONSTRAINT uk_empleados_email UNIQUE (email);

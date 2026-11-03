ALTER TABLE empleados ADD COLUMN departamento_id BIGINT NULL;

-- Decisión del caso: los empleados heredados pertenecían a Docencia.
UPDATE empleados
SET departamento_id = (SELECT id FROM departamentos WHERE nombre = 'Docencia')
WHERE departamento_id IS NULL;

ALTER TABLE empleados MODIFY COLUMN departamento_id BIGINT NOT NULL;
CREATE INDEX idx_empleados_departamento ON empleados (departamento_id);
ALTER TABLE empleados
ADD CONSTRAINT fk_empleados_departamento
FOREIGN KEY (departamento_id) REFERENCES departamentos (id);

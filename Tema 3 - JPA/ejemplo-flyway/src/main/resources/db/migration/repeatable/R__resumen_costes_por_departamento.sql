CREATE OR REPLACE VIEW resumen_costes_departamento AS
SELECT d.id, d.nombre, COUNT(e.id) AS numero_empleados,
       COALESCE(SUM(e.salario), 0) AS coste_salarial_anual
FROM departamentos d
LEFT JOIN empleados e ON e.departamento_id = d.id
GROUP BY d.id, d.nombre;

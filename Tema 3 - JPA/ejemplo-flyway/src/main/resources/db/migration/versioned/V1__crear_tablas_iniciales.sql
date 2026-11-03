CREATE TABLE departamentos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL,
    CONSTRAINT pk_departamentos PRIMARY KEY (id),
    CONSTRAINT uk_departamentos_nombre UNIQUE (nombre)
);

CREATE TABLE empleados (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    salario DECIMAL(10, 2) NOT NULL,
    CONSTRAINT pk_empleados PRIMARY KEY (id),
    CONSTRAINT ck_empleados_salario CHECK (salario >= 0)
);

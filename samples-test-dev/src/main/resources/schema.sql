--Primero se deben borrar todas las tablas (de detalle a maestro) y lugo anyadirlas (de maestro a detalle)
--(en este caso en cada una de las aplicaciones (tkrun y descuento) se usa solo una tabla, por lo que no hace falta)

--Para giis.demo.tkrun:

--Eliminar tablas anteriores
drop table Carreras;
drop table Inscritos;
drop table Atleta;
drop table Categoria;

--Crear nuevas tablas
create table Carrera (
    id int primary key not null, 
    nombre varchar not null, 
    tipo ENUM('urbana', 'tierra') not null,
    descr varchar(32), 
    inicio_inscripcion date not null, 
    fin_inscripcion date not null, 
    precio_inscripcion float not null, 
    fecha date not null, 
    check(inicio<=fin), 
    check(fin<fecha)
);

create table Atleta (
    email varchar primary key not null, 
    nombre varchar, 
    apellido varchar, 
    nacimiento date not null,
    sexo ('male', 'female')
);

create table Inscritos(
    email varchar foreign key references Atleta(email), 
    id_carrera varchar foreign key references Carrera(id), 
    primary key (email, id_carrera), 
    estado ENUM('preinscrito', 'inscrito'), 
    dorsal int
);

create table Categoria(
    id int primary key not null,
    id_carrera foreign key references Carrera(id),
    edad_inicio int,
    edad_final int,
    sexo value ('male', 'female'),
    nombre varchar
);
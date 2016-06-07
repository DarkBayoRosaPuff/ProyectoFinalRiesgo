\connect prestamo

CREATE TABLE usuario(
id_usuario SERIAL PRIMARY KEY,
nombre VARCHAR(255) NOT NULL,
contrasena VARCHAR(255) NOT NULL,
correo VARCHAR(255) UNIQUE NOT NULL,
telefono VARCHAR(15)
/* Borré los CONSTRAINTS porque me mandaban error */
/* CONSTRAINT correocorrecto CHECK (correo::text ~ '^[w!#$%&''*+/=?`{|}~^-]+(.[w!#$%&''*+/=?`{|}~^-]+)*@[w-]+(.[w-]+)*$'::text) */
);

CREATE TABLE publicacion(
id_dueno INTEGER REFERENCES usuario(id_usuario),
resena TEXT,
titulo VARCHAR(255) NOT NULL,
autor VARCHAR(255) NOT NULL,
edicion VARCHAR(255), 
isbn TEXT, 
anio INTEGER,
evaluacion_del_contenido INTEGER,
foto TEXT, 
editorial VARCHAR(255), 
evaluacion_de_redaccion INTEGER,
id_publicacion SERIAL PRIMARY KEY,
elegido INTEGER REFERENCES usuario(id_usuario), 
finalizado BOOLEAN NOT NULL,
fecha DATE NOT NULL,
lugar_de_intercambio TEXT
);

CREATE TABLE es_candidato(
id_candidato INTEGER REFERENCES usuario(id_usuario),
id_publicacion INTEGER REFERENCES publicacion(id_publicacion)
);


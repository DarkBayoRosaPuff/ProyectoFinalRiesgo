CREATE DATABASE proyectoFinal;

\connect proyectoFinal

CREATE TABLE usuario(
id_usuario SERIAL PRIMARY KEY,
nombre VARCHAR(255) NOT NULL,
contrasena VARCHAR(255) NOT NULL,
correo VARCHAR(255) UNIQUE NOT NULL,
telefono VARCHAR(15) /* Lo puse como varchar porque no creo que deba guardarse como 
	          número. Al menos eso dice el internet */
);

CREATE TABLE libro(
id_dueno INTEGER REFERENCES usuario(id_usuario),
resena TEXT,
titulo VARCHAR(255) NOT NULL,
autor VARCHAR(255) NOT NULL,
edicion VARCHAR(255), /* Igual creo que es más fácil guardar la edición como 
                         texto (?) */
id_libro SERIAL PRIMARY KEY,
isbn TEXT, /* Mismo caso del teléfono */
anio DATE,
evaluacion_del_contenido INTEGER,
foto TEXT, 
editorial VARCHAR(255), 
evaluacion_de_redaccion INTEGER
);

CREATE TABLE publicacion(
id_publicacion SERIAL PRIMARY KEY,
id_libro INTEGER REFERENCES libro(id_libro),
elegido INTEGER REFERENCES usuario(id_usuario), /* El usuario que recibió el 
       	       		  		       	  libro */
finalizado BOOLEAN NOT NULL, /* Nos dice si el préstamo ya terminó */ 
fecha DATE NOT NULL,
lugar_de_intercambio TEXT
);

/* Nos dice si un usuario es candidato a que le presten el libro de la 
   publicación */
CREATE TABLE es_candidato(
id_candidato INTEGER REFERENCES usuario(id_usuario),
id_publicacion INTEGER REFERENCES publicacion(id_publicacion)
);


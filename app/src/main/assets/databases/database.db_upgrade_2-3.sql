-- Generated by SQLite Compare utility

-- The script can be used to migrate database
-- C:\Users\fokof_000\Downloads\database.db schema
-- to the schema of database
-- C:\Users\fokof_000\StudioProjects\ProgramaDeRehabilitacionDelLenguaje\app\src\main\assets\databases\database.db

BEGIN TRANSACTION;


-- Creating table imagen from scratch (simple ALTER TABLE is not enough)

CREATE TABLE temp_imagen_534011718
(
    _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    img_nombre TEXT(255),
    txt_nombre TEXT(255)
);

-- Copying rows from original table to the new table

INSERT INTO temp_imagen_534011718 (_id,img_nombre,txt_nombre) SELECT _id,img_nombre,NULL AS txt_nombre FROM imagen;

-- Droping the original table and renaming the temporary table

DROP TABLE imagen;
ALTER TABLE temp_imagen_534011718 RENAME TO imagen;

COMMIT TRANSACTION;

-- Creating table nivel from scratch (simple ALTER TABLE is not enough)

CREATE TABLE temp_nivel_534011718
(
    _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    niv_num INTEGER,
    niv_term BOOLEAN,
    cat_id INTEGER NOT NULL CONSTRAINT cat_id REFERENCES categoria(_id)
);

-- Copying rows from original table to the new table

INSERT INTO temp_nivel_534011718 (_id,niv_num,cat_id,niv_term) SELECT _id,niv_num,cat_id,0 AS niv_term FROM nivel;

-- Droping the original table and renaming the temporary table

DROP TABLE nivel;
ALTER TABLE temp_nivel_534011718 RENAME TO nivel;

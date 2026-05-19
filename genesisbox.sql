CREATE DATABASE IF NOT EXISTS genesisbox;
USE genesisbox;

CREATE TABLE IF NOT EXISTS world (

    id INT PRIMARY KEY AUTO_INCREMENT,
    worldRows INT,
    worldCols INT,
    day INT,
    year INT,
    hour INT,
    minute INT,
    second INT
);

CREATE TABLE IF NOT EXISTS tiles (

    id INT PRIMARY KEY AUTO_INCREMENT,
    x INT,
    y INT,
    type INT,
    variant INT
);

CREATE TABLE IF NOT EXISTS entities (

    id INT PRIMARY KEY AUTO_INCREMENT,
    entityType VARCHAR(50),
    x INT,
    y INT,
    slot INT,
    health INT,
    maxHealth INT,
    alive BOOLEAN,
    energy INT,
    hunger INT,
    speed INT,
    attackStat INT,
    intelligence INT,
    capacity INT,
    sex VARCHAR(20),
    foodType VARCHAR(20),
    habitat INT,
    amount INT,
    maxAmount INT,
    depleted BOOLEAN,
    regenRate INT,
    regenTimer INT,
    regenInterval INT,
    customName VARCHAR(100),
    custom1 DOUBLE,
    custom2 DOUBLE,
    custom3 DOUBLE
);

select * from entities;
select * from world;
select * from tiles;
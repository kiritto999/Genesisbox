-- =====================================================
-- RESET COMPLETO
-- =====================================================

DROP DATABASE IF EXISTS genesisbox;

CREATE DATABASE genesisbox;

USE genesisbox;

-- =====================================================
-- WORLD
-- =====================================================

CREATE TABLE world (

    id INT PRIMARY KEY AUTO_INCREMENT,

    worldRows INT,
    worldCols INT,

    worldName VARCHAR(100),

    seed BIGINT,

    difficulty INT DEFAULT 1,

    day INT,
    year INT,

    hour INT,
    minute INT,
    second INT
);

-- =====================================================
-- TILES
-- =====================================================

CREATE TABLE tiles (

    id INT PRIMARY KEY AUTO_INCREMENT,

    x INT,
    y INT,
	variant INT DEFAULT 0,
    tileId INT,

    biome INT DEFAULT 0,

    discovered BOOLEAN DEFAULT TRUE
);

-- =====================================================
-- ENTITIES
-- =====================================================

CREATE TABLE entities (

    id INT PRIMARY KEY AUTO_INCREMENT,

    -- =========================
    -- COMMON
    -- =========================

    entityType VARCHAR(50),

    x INT,
    y INT,

    slot INT DEFAULT 0,

    health INT DEFAULT 0,
    maxHealth INT DEFAULT 0,

    alive BOOLEAN DEFAULT TRUE,

    customName VARCHAR(100),

    -- =========================
    -- ANIMALS
    -- =========================

    energy INT DEFAULT 0,
    hunger INT DEFAULT 0,

    speed INT DEFAULT 0,
    attackStat INT DEFAULT 0,
    intelligence INT DEFAULT 0,

    capacity INT DEFAULT 0,

    sex VARCHAR(20),

    foodType VARCHAR(20),

    habitat INT DEFAULT 0,

    ageDays INT DEFAULT 0,

    stage VARCHAR(30),

    reproTimer DOUBLE DEFAULT 0,

    -- =========================
    -- RESOURCES
    -- =========================

    amount INT DEFAULT 0,

    maxAmount INT DEFAULT 0,

    depleted BOOLEAN DEFAULT FALSE,

    regenRate INT DEFAULT 0,

    regenTimer INT DEFAULT 0,

    regenInterval INT DEFAULT 0,

    -- =========================
    -- EXTRA CUSTOM VALUES
    -- =========================

    custom1 DOUBLE DEFAULT 0,
    custom2 DOUBLE DEFAULT 0,
    custom3 DOUBLE DEFAULT 0
);

-- =====================================================
-- DEBUG
-- =====================================================

SELECT * FROM world;
SELECT * FROM tiles;
SELECT * FROM entities;


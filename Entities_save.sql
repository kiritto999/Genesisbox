
CREATE DATABASE IF NOT EXISTS game_db;
USE game_db;

CREATE TABLE IF NOT EXISTS entities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50),
    sub_type VARCHAR(50),
    x INT,
    y INT,
    is_alive BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS animals (
    entity_id INT PRIMARY KEY,
    health DOUBLE,
    capacity INT,
    FOREIGN KEY (entity_id) REFERENCES entities(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS resources (
    entity_id INT PRIMARY KEY,
    resource_type VARCHAR(50),
    FOREIGN KEY (entity_id) REFERENCES entities(id) ON DELETE CASCADE
);

DELIMITER //

CREATE PROCEDURE sp_insert_entity(
    IN p_type VARCHAR(50),
    IN p_subtype VARCHAR(50),
    IN p_x INT,
    IN p_y INT,
    IN p_alive BOOLEAN
)
BEGIN
    INSERT INTO entities(type, sub_type, x, y, is_alive)
    VALUES(p_type, p_subtype, p_x, p_y, p_alive);
END //

CREATE PROCEDURE sp_insert_animal(
    IN p_entity_id INT,
    IN p_health DOUBLE,
    IN p_capacity INT
)
BEGIN
    INSERT INTO animals(entity_id, health, capacity)
    VALUES(p_entity_id, p_health, p_capacity);
END //

CREATE PROCEDURE sp_insert_resource(
    IN p_entity_id INT,
    IN p_type VARCHAR(50)
)
BEGIN
    INSERT INTO resources(entity_id, resource_type)
    VALUES(p_entity_id, p_type);
END //

CREATE PROCEDURE sp_update_entity(
    IN p_id INT,
    IN p_x INT,
    IN p_y INT,
    IN p_alive BOOLEAN
)
BEGIN
    UPDATE entities
    SET x = p_x,
        y = p_y,
        is_alive = p_alive
    WHERE id = p_id;
END //

CREATE PROCEDURE sp_delete_entity(
    IN p_id INT
)
BEGIN
    DELETE FROM entities WHERE id = p_id;
END //

DELIMITER ;

CREATE OR REPLACE VIEW vw_all_entities AS
SELECT 
    e.id,
    e.type,
    e.sub_type,
    e.x,
    e.y,
    e.is_alive,
    a.health,
    a.capacity,
    r.resource_type
FROM entities e
LEFT JOIN animals a ON e.id = a.entity_id
LEFT JOIN resources r ON e.id = r.entity_id;
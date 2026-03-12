CREATE TABLE project_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    status VARCHAR(50) DEFAULT 'ACTIVE'
);

-- Insert initial data
INSERT INTO project_data (name, description, status) VALUES ('Project Alpha', 'Initial secret project', 'ACTIVE');
INSERT INTO project_data (name, description, status) VALUES ('Project Beta', 'Experimental wing', 'STABLE');

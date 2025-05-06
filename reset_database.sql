DROP DATABASE IF EXISTS job_portal;
CREATE DATABASE job_portal;
USE job_portal;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(150) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(20) CHECK (role IN ('JOB_SEEKER', 'EMPLOYER', 'ADMIN')),
    enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE companies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    industry VARCHAR(100) NOT NULL,
    location VARCHAR(150) NOT NULL,
    website VARCHAR(255) NOT NULL,
    contact_email VARCHAR(150) NOT NULL,
    contact_phone VARCHAR(50) NOT NULL,
    logo_url TEXT,
    user_id INT NOT NULL,
    size VARCHAR(50),
    active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE jobs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employer_id INT,
    company_id INT,
    title VARCHAR(255),
    description TEXT,
    category VARCHAR(100),
    salary DECIMAL(10,2),
    location VARCHAR(150),
    experience INT,
    job_type VARCHAR(50),
    posted_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employer_id) REFERENCES users(id),
    FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE TABLE applications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    job_id INT,
    job_seeker_id INT,
    resume VARCHAR(255),
    cover_letter TEXT,
    status VARCHAR(50) CHECK (status IN ('Pending', 'Reviewed', 'Shortlisted', 'Rejected', 'Hired')),
    applied_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (job_id) REFERENCES jobs(id),
    FOREIGN KEY (job_seeker_id) REFERENCES users(id)
);

-- Insert test users with simple passwords
INSERT INTO users (name, email, password, role, enabled) VALUES
('Admin User', 'admin@jobportal.com', 'password123', 'ADMIN', TRUE),
('Tech Corp', 'techcorp@example.com', 'password123', 'EMPLOYER', TRUE),
('Software Solutions', 'softwaresol@example.com', 'password123', 'EMPLOYER', TRUE),
('John Doe', 'john.doe@example.com', 'password123', 'JOB_SEEKER', TRUE),
('Jane Smith', 'jane.smith@example.com', 'password123', 'JOB_SEEKER', TRUE);

-- Insert test companies
INSERT INTO companies (name, description, industry, location, website, contact_email, contact_phone, logo_url, user_id, size, active) VALUES
('Tech Corp', 'A leading technology company specializing in software development', 'Technology', 'New York, NY', 'https://techcorp.example.com', 'contact@techcorp.example.com', '555-123-4567', 'https://example.com/logos/techcorp.png', 2, 'MEDIUM', TRUE),
('Software Solutions', 'Providing innovative software solutions for businesses', 'Software', 'San Francisco, CA', 'https://softwaresol.example.com', 'info@softwaresol.example.com', '555-987-6543', 'https://example.com/logos/softwaresol.png', 3, 'SMALL', TRUE),
('Digital Innovations', 'Pioneering digital transformation for enterprises', 'Digital', 'Boston, MA', 'https://digitalinnovations.example.com', 'hello@digitalinnovations.example.com', '555-456-7890', 'https://example.com/logos/digitalinnovations.png', 2, 'LARGE', TRUE),
('Cloud Systems', 'Cloud infrastructure and services provider', 'Cloud Computing', 'Seattle, WA', 'https://cloudsystems.example.com', 'support@cloudsystems.example.com', '555-789-0123', 'https://example.com/logos/cloudsystems.png', 3, 'ENTERPRISE', TRUE);

-- Insert test jobs
INSERT INTO jobs (employer_id, company_id, title, description, category, salary, location, experience, job_type) VALUES
(2, 1, 'Senior Java Developer', 'Looking for an experienced Java developer with Spring Boot experience', 'IT', 120000.00, 'New York', 5, 'Full-time'),
(2, 1, 'Frontend Developer', 'React.js developer needed for our web applications', 'IT', 90000.00, 'Remote', 3, 'Full-time'),
(3, 2, 'DevOps Engineer', 'AWS and Docker experience required', 'IT', 110000.00, 'San Francisco', 4, 'Full-time'),
(3, 2, 'Product Manager', 'Lead product development for our SaaS platform', 'Management', 130000.00, 'Boston', 6, 'Full-time');

-- Insert test applications
INSERT INTO applications (job_id, job_seeker_id, resume, cover_letter, status) VALUES
(1, 4, 'john_doe_resume.pdf', 'I am interested in the Senior Java Developer position', 'Pending'),
(2, 4, 'john_doe_resume.pdf', 'I have experience with React.js', 'Reviewed'),
(3, 5, 'jane_smith_resume.pdf', 'I have AWS certification and Docker experience', 'Shortlisted'),
(4, 5, 'jane_smith_resume.pdf', 'I have managed multiple SaaS products', 'Pending'); 
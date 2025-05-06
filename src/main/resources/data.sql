-- Insert sample users
INSERT INTO users (name, email, password, role, enabled) VALUES
('Admin User', 'admin@jobportal.com', 'admin123', 'ADMIN', true),
('John Smith', 'employer@techcorp.com', 'employer123', 'EMPLOYER', true),
('Sarah Johnson', 'employer@digital.com', 'employer123', 'EMPLOYER', true),
('Mike Wilson', 'jobseeker1@gmail.com', 'seeker123', 'JOB_SEEKER', true),
('Emily Brown', 'jobseeker2@gmail.com', 'seeker123', 'JOB_SEEKER', true);

-- Insert sample companies
INSERT INTO companies (name, description, industry, location, website, contact_email, contact_phone, logo_url, user_id, size, active) VALUES
('Tech Corp', 'Leading technology solutions provider', 'Technology', 'New York, NY', 'https://techcorp.com', 'contact@techcorp.com', '555-0101', 'https://example.com/logos/techcorp.png', 2, 'LARGE', true),
('Digital Innovations', 'Digital transformation company', 'IT Services', 'San Francisco, CA', 'https://digitalinno.com', 'info@digitalinno.com', '555-0102', 'https://example.com/logos/digital.png', 3, 'MEDIUM', true);

-- Insert sample jobs
INSERT INTO jobs (title, description, location, type, experience_level, salary, posted_date, active, company_id) VALUES
('Senior Software Engineer', 'Lead development of enterprise applications', 'New York, NY', 'FULL_TIME', 'SENIOR', 120000.00, CURRENT_TIMESTAMP, true, 1),
('Frontend Developer', 'Create responsive web applications', 'Remote', 'FULL_TIME', 'MID_LEVEL', 90000.00, CURRENT_TIMESTAMP, true, 1),
('DevOps Engineer', 'Manage cloud infrastructure', 'San Francisco, CA', 'FULL_TIME', 'SENIOR', 130000.00, CURRENT_TIMESTAMP, true, 2),
('UI/UX Designer', 'Design user interfaces', 'Remote', 'CONTRACT', 'MID_LEVEL', 80000.00, CURRENT_TIMESTAMP, true, 2),
('Backend Developer', 'Develop server-side applications', 'Remote', 'PART_TIME', 'MID_LEVEL', 100000.00, CURRENT_TIMESTAMP, false, 1),
('Mobile Developer', 'Build cross-platform mobile apps', 'New York, NY', 'INTERNSHIP', 'JUNIOR', 110000.00, CURRENT_TIMESTAMP, true, 2);

-- Insert sample applications
INSERT INTO applications (job_id, user_id, status) VALUES
(1, 4, 'PENDING'),
(2, 4, 'UNDER_REVIEW'),
(3, 5, 'PENDING'),
(4, 5, 'ACCEPTED'); 
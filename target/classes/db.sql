INSERT INTO employees(id,name,email,department,designation,country,currency,salary,status,joined) VALUES
('EMP-1001','Aarav Sharma','aarav.sharma@example.com','Engineering','Senior Engineer','India','INR',1800000,'Active','2022-04-11'),
('EMP-1002','Maya Patel','maya.patel@example.com','Human Resources','HR Business Partner','India','INR',1450000,'Active','2021-08-02'),
('EMP-1003','Liam Carter','liam.carter@example.com','Finance','Finance Analyst','United Kingdom','GBP',52000,'Active','2023-01-16'),
('EMP-1004','Sofia Rossi','sofia.rossi@example.com','Engineering','Software Engineer','Italy','EUR',61000,'Inactive','2020-10-19'),
('EMP-1005','Noah Kim','noah.kim@example.com','Operations','Operations Lead','Singapore','SGD',88000,'Active','2019-05-27');
INSERT INTO countries(code,name,currency,symbol,rate_to_inr,updated) VALUES
('IN','India','INR','₹',1.0000,'2026-10-01'),('US','United States','USD','$',83.5000,'2026-10-01'),
('GB','United Kingdom','GBP','£',109.2000,'2026-10-01'),('IT','Italy','EUR','€',91.4000,'2026-10-01'),
('SG','Singapore','SGD','S$',62.1000,'2026-10-01'),('AE','United Arab Emirates','AED','د.إ',22.7000,'2026-10-01');
INSERT INTO audit_events(username,action,detail) VALUES ('system','APPLICATION_STARTED','Seed data loaded from db.sql'),('system','DATABASE_INITIALIZED','H2 in-memory database initialized from schema.sql');

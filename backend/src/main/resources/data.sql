INSERT INTO patients
(first_name, last_name, date_of_birth, email, phone, address)
VALUES
    ('John', 'Smith', '1985-04-12', 'john.smith@example.com',
     '416-555-1001', '100 King Street, Toronto, ON'),

    ('Maria', 'Garcia', '1990-09-22', 'maria.garcia@example.com',
     '416-555-1002', '200 Queen Street, Toronto, ON'),

    ('Daniel', 'Lee', '1978-01-18', 'daniel.lee@example.com',
     '416-555-1003', '300 Yonge Street, Toronto, ON')
    ON CONFLICT (email) DO NOTHING;
INSERT INTO patients
(first_name, last_name, date_of_birth, email, phone, address)
VALUES
    ('John', 'Smith', '1985-04-12', 'john.smith@example.com',
     '416-555-1001', '100 King Street, Toronto, ON'),

    ('Maria', 'Garcia', '1990-09-22', 'maria.garcia@example.com',
     '416-555-1002', '200 Queen Street, Toronto, ON'),

    ('Daniel', 'Lee', '1978-01-18', 'daniel.lee@example.com',
     '416-555-1003', '300 Yonge Street, Toronto, ON'),

    ('Emily', 'Wilson', '1995-06-14', 'emily.wilson@example.com',
     '416-555-1004', '400 Bloor Street, Toronto, ON'),

    ('Michael', 'Brown', '1982-11-03', 'michael.brown@example.com',
     '416-555-1005', '500 Dundas Street, Toronto, ON'),

    ('Sarah', 'Taylor', '1988-02-27', 'sarah.taylor@example.com',
     '416-555-1006', '600 College Street, Toronto, ON'),

    ('David', 'Anderson', '1975-08-09', 'david.anderson@example.com',
     '416-555-1007', '700 Bay Street, Toronto, ON'),

    ('Priya', 'Patel', '1992-12-19', 'priya.patel@example.com',
     '416-555-1008', '800 Front Street, Toronto, ON'),

    ('James', 'Martin', '1980-05-31', 'james.martin@example.com',
     '416-555-1009', '900 Spadina Avenue, Toronto, ON'),

    ('Sophia', 'Chen', '1997-10-11', 'sophia.chen@example.com',
     '416-555-1010', '1000 Harbour Street, Toronto, ON')

    ON CONFLICT (email) DO NOTHING;
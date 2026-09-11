CREATE TABLE IF NOT EXISTS patients (
                                        patient_id BIGSERIAL PRIMARY KEY,
                                        first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    email VARCHAR(200) UNIQUE,
    phone VARCHAR(40),
    address VARCHAR(300)
    );

CREATE INDEX IF NOT EXISTS idx_patients_last_name
    ON patients(last_name);

CREATE INDEX IF NOT EXISTS idx_patients_first_name
    ON patients(first_name);
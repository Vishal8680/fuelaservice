-- Create tables
CREATE TABLE tax_jurisdiction (
  id BIGSERIAL PRIMARY KEY,
  code VARCHAR(32) NOT NULL,
  name VARCHAR(255) NOT NULL,
  base_rate NUMERIC(10,6) NOT NULL,
  effective_date DATE NOT NULL
);

CREATE UNIQUE INDEX ux_tax_jurisdiction_code_effective ON tax_jurisdiction(code, effective_date);

CREATE TABLE fuel_tax_calculation (
  id BIGSERIAL PRIMARY KEY,
  shipment_id VARCHAR(64) NOT NULL,
  jurisdiction_id BIGINT NOT NULL REFERENCES tax_jurisdiction(id),
  fuel_quantity NUMERIC(18,6) NOT NULL,
  price_per_gallon NUMERIC(18,6) NOT NULL,
  calculated_tax NUMERIC(18,6) NOT NULL,
  calculated_on TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_shipment ON fuel_tax_calculation(shipment_id);

-- Sample data
INSERT INTO tax_jurisdiction(code, name, base_rate, effective_date) VALUES ('CA', 'California', 0.0875, '2025-01-01');
INSERT INTO tax_jurisdiction(code, name, base_rate, effective_date) VALUES ('TX', 'Texas', 0.0625, '2025-01-01');
INSERT INTO tax_jurisdiction(code, name, base_rate, effective_date) VALUES ('NY', 'New York', 0.0890, '2025-01-01');

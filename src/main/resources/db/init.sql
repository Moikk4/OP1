CREATE TABLE IF NOT EXISTS unit (
  unit_id INT PRIMARY KEY AUTO_INCREMENT,
  category VARCHAR(20) NOT NULL,
  code VARCHAR(20) NOT NULL UNIQUE,
  sort_order INT NOT NULL
);

CREATE TABLE IF NOT EXISTS unit_translation (
  unit_id INT NOT NULL,
  lang VARCHAR(10) NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  PRIMARY KEY (unit_id, lang),
  CONSTRAINT fk_unit_translation_unit FOREIGN KEY (unit_id) REFERENCES unit(unit_id)
);

CREATE TABLE IF NOT EXISTS conversion_history (
  id INT PRIMARY KEY AUTO_INCREMENT,
  record_value DOUBLE NOT NULL,
  from_unit VARCHAR(20) NOT NULL,
  to_unit VARCHAR(20) NOT NULL,
  result_value DOUBLE NOT NULL,
  language VARCHAR(20) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

MERGE INTO unit (category, code, sort_order) KEY(code) VALUES
('temperature', 'C', 1),
('temperature', 'F', 2),
('temperature', 'K', 3);

MERGE INTO unit_translation (unit_id, lang, name, description) KEY(unit_id, lang) VALUES
(1, 'en', 'Celsius', 'Temperature in Celsius'),
(1, 'fi', 'Celsius', 'Lämpötila celsiuksena'),
(1, 'sv', 'Celsius', 'Temperatur i Celsius'),
(1, 'ja', 'セルシウス', 'セルシウス温度'),
(1, 'ar', 'درجة مئوية', 'درجة الحرارة بالمئوية'),

(2, 'en', 'Fahrenheit', 'Temperature in Fahrenheit'),
(2, 'fi', 'Fahrenheit', 'Lämpötila fahrenheiteinä'),
(2, 'sv', 'Fahrenheit', 'Temperatur i Fahrenheit'),
(2, 'ja', '華氏', '華氏温度'),
(2, 'ar', 'فهرنهايت', 'درجة الحرارة بالفهرنهايت'),

(3, 'en', 'Kelvin', 'Temperature in Kelvin'),
(3, 'fi', 'Kelvin', 'Lämpötila kelvineinä'),
(3, 'sv', 'Kelvin', 'Temperatur i Kelvin'),
(3, 'ja', 'ケルビン', 'ケルビン温度'),
(3, 'ar', 'كلفن', 'درجة الحرارة بالكلفن');

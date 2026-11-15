-- Database Localization using Translation Table Approach
-- This script creates the database schema for the Temperature Converter application
-- with full localization support for English, Finnish, Swedish, Japanese and Arabic

-- ============================================================================
-- MEASUREMENT TABLES (speed, distance, time tracking)
-- ============================================================================

-- Measurements: stores speed/distance/time calculations
CREATE TABLE IF NOT EXISTS measurements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    speed_value DOUBLE,
    distance_value DOUBLE,
    time_value DOUBLE,
    speed_unit_id INT,
    distance_unit_id INT,
    time_unit_id INT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- TEMPERATURE CONVERSION RECORDS
-- ============================================================================

-- Temperature conversion history
CREATE TABLE IF NOT EXISTS temp_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    input_value DOUBLE NOT NULL,
    from_unit VARCHAR(20) NOT NULL,
    to_unit VARCHAR(20) NOT NULL,
    result_value DOUBLE NOT NULL,
    language VARCHAR(10),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- UNIT/LOOKUP DATA (Translation Table Approach)
-- ============================================================================

-- Base table: language-neutral unit information
CREATE TABLE IF NOT EXISTS units (
    id INT AUTO_INCREMENT PRIMARY KEY,
    unit_key VARCHAR(50) NOT NULL UNIQUE,  -- e.g., 'CELSIUS', 'KM_PER_HOUR'
    symbol VARCHAR(10) NOT NULL,            -- e.g., '°C', 'km/h'
    unit_type VARCHAR(20) NOT NULL          -- 'TEMPERATURE', 'SPEED', 'DISTANCE', 'TIME'
);

-- Translation table: localized names and descriptions
CREATE TABLE IF NOT EXISTS unit_translations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    unit_id INT NOT NULL,
    lang VARCHAR(10) NOT NULL,              -- 'en', 'fi', 'sv', 'ja', 'ar'
    name VARCHAR(100) NOT NULL,             -- Localized name
    description VARCHAR(500),               -- Optional localized description
    FOREIGN KEY (unit_id) REFERENCES units(id) ON DELETE CASCADE,
    UNIQUE (unit_id, lang)
);

-- ============================================================================
-- SEED DATA: Temperature Units
-- ============================================================================

-- Insert base temperature units
MERGE INTO units (unit_key, symbol, unit_type) KEY(unit_key) VALUES
    ('CELSIUS', '°C', 'TEMPERATURE'),
    ('FAHRENHEIT', '°F', 'TEMPERATURE'),
    ('KELVIN', 'K', 'TEMPERATURE');

-- Celsius translations
MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'en', 'Celsius', 'Metric temperature scale, water freezes at 0°C and boils at 100°C'
FROM units u WHERE u.unit_key = 'CELSIUS';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'fi', 'Celsius', 'Metrinen lämpötila-asteikko, vesi jäätyy 0°C ja kiehuu 100°C'
FROM units u WHERE u.unit_key = 'CELSIUS';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'sv', 'Celsius', 'Metrisk temperaturskala, vatten fryser vid 0°C och kokar vid 100°C'
FROM units u WHERE u.unit_key = 'CELSIUS';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'ja', 'セルシウス', '摂氏温度目盛、水は0°Cで凍り100°Cで沸騰する'
FROM units u WHERE u.unit_key = 'CELSIUS';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'ar', 'سيلسيوس', 'مقياس درجة الحرارة المترية، يتجمد الماء عند 0 درجة مئوية ويغلي عند 100 درجة مئوية'
FROM units u WHERE u.unit_key = 'CELSIUS';

-- Fahrenheit translations
MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'en', 'Fahrenheit', 'Imperial temperature scale, water freezes at 32°F and boils at 212°F'
FROM units u WHERE u.unit_key = 'FAHRENHEIT';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'fi', 'Fahrenheit', 'Brittiläinen lämpötila-asteikko, vesi jäätyy 32°F ja kiehuu 212°F'
FROM units u WHERE u.unit_key = 'FAHRENHEIT';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'sv', 'Fahrenheit', 'Brittisk temperaturskala, vatten fryser vid 32°F och kokar vid 212°F'
FROM units u WHERE u.unit_key = 'FAHRENHEIT';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'ja', 'ファーレンハイト', '華氏温度目盛、水は32°Fで凍り212°Fで沸騰する'
FROM units u WHERE u.unit_key = 'FAHRENHEIT';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'ar', 'فهرنهايت', 'مقياس درجة الحرارة الإمبراطوري، يتجمد الماء عند 32 درجة فهرنهايت ويغلي عند 212 درجة فهرنهايت'
FROM units u WHERE u.unit_key = 'FAHRENHEIT';

-- Kelvin translations
MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'en', 'Kelvin', 'Absolute temperature scale, 0K is absolute zero'
FROM units u WHERE u.unit_key = 'KELVIN';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'fi', 'Kelvin', 'Absoluuttinen lämpötila-asteikko, 0K on absoluuttinen nollapiste'
FROM units u WHERE u.unit_key = 'KELVIN';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'sv', 'Kelvin', 'Absolut temperaturskala, 0K är absolut nollpunkt'
FROM units u WHERE u.unit_key = 'KELVIN';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'ja', 'ケルビン', '絶対温度目盛、0Kは絶対零度'
FROM units u WHERE u.unit_key = 'KELVIN';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'ar', 'كلفن', 'مقياس درجة الحرارة المطلقة، 0 كلفن هو الصفر المطلق'
FROM units u WHERE u.unit_key = 'KELVIN';

-- ============================================================================
-- SEED DATA: Speed, Distance, and Time Units (for measurements table)
-- ============================================================================

-- Speed units
MERGE INTO units (unit_key, symbol, unit_type) KEY(unit_key) VALUES
    ('KM_PER_HOUR', 'km/h', 'SPEED'),
    ('MILES_PER_HOUR', 'mph', 'SPEED'),
    ('METERS_PER_SECOND', 'm/s', 'SPEED');

-- Distance units
MERGE INTO units (unit_key, symbol, unit_type) KEY(unit_key) VALUES
    ('KILOMETER', 'km', 'DISTANCE'),
    ('MILE', 'mi', 'DISTANCE'),
    ('METER', 'm', 'DISTANCE');

-- Time units
MERGE INTO units (unit_key, symbol, unit_type) KEY(unit_key) VALUES
    ('HOUR', 'h', 'TIME'),
    ('MINUTE', 'min', 'TIME'),
    ('SECOND', 's', 'TIME');

-- Speed unit translations (km/h)
MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'en', 'Kilometers per hour', 'Speed in kilometers per hour'
FROM units u WHERE u.unit_key = 'KM_PER_HOUR';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'fi', 'Kilometriä tunnissa', 'Nopeus kilometreinä tunnissa'
FROM units u WHERE u.unit_key = 'KM_PER_HOUR';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'sv', 'Kilometer per timme', 'Hastighet i kilometer per timme'
FROM units u WHERE u.unit_key = 'KM_PER_HOUR';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'ja', '時速キロメートル', '1時間あたりのキロメートル数'
FROM units u WHERE u.unit_key = 'KM_PER_HOUR';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'ar', 'كيلومتر في الساعة', 'السرعة بالكيلومترات في الساعة'
FROM units u WHERE u.unit_key = 'KM_PER_HOUR';

-- Distance unit translations (kilometer)
MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'en', 'Kilometer', 'Metric unit of distance'
FROM units u WHERE u.unit_key = 'KILOMETER';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'fi', 'Kilometri', 'Metrinen pituusyksikkö'
FROM units u WHERE u.unit_key = 'KILOMETER';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'sv', 'Kilometer', 'Metrisk enhet för avstånd'
FROM units u WHERE u.unit_key = 'KILOMETER';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'ja', 'キロメートル', 'メートル法の距離単位'
FROM units u WHERE u.unit_key = 'KILOMETER';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'ar', 'كيلومتر', 'وحدة قياس المسافة المترية'
FROM units u WHERE u.unit_key = 'KILOMETER';

-- Time unit translations (hour)
MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'en', 'Hour', 'Time unit: 60 minutes'
FROM units u WHERE u.unit_key = 'HOUR';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'fi', 'Tunti', 'Aikayksikkö: 60 minuuttia'
FROM units u WHERE u.unit_key = 'HOUR';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'sv', 'Timme', 'Tidsenhet: 60 minuter'
FROM units u WHERE u.unit_key = 'HOUR';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'ja', '時間', '時間単位：60分'
FROM units u WHERE u.unit_key = 'HOUR';

MERGE INTO unit_translations (unit_id, lang, name, description) KEY(unit_id, lang)
SELECT u.id, 'ar', 'ساعة', 'وحدة الوقت: 60 دقيقة'
FROM units u WHERE u.unit_key = 'HOUR';

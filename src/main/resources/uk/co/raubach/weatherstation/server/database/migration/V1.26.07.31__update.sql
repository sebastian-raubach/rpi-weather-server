ALTER TABLE `measurements`
    ADD COLUMN `created_year`  SMALLINT AS (YEAR(created)) STORED,
    ADD COLUMN `created_month` TINYINT AS (MONTH(created)) STORED,
    ADD COLUMN `created_day`   TINYINT AS (DAY(created))   STORED;

ALTER TABLE `measurements`
    ADD INDEX idx_year_month_day (created_year, created_month, created_day),
    ADD INDEX idx_month_day (created_month, created_day);

CREATE TABLE IF NOT EXISTS `lux_climatology` (
    month           TINYINT NOT NULL,
    day             TINYINT NOT NULL,
    slot_time       TIME NOT NULL,             -- e.g. 12:35:00
    p95_lux         DECIMAL(10,2) NOT NULL,    -- raw pooled 95th percentile
    p95_lux_smooth  DECIMAL(10,2) NOT NULL,    -- after Savitzky-Golay smoothing
    sample_count    INT NOT NULL,     -- how many readings fed this bucket
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (month, day, slot_time)
);
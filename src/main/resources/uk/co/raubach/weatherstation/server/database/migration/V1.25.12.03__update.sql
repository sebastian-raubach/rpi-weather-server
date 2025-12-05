ALTER TABLE `measurements`
    ADD COLUMN `loft_temp` decimal(6,2) NULL DEFAULT NULL AFTER `pi_temp`,
    ADD COLUMN `loft_humidity` decimal(6,2) NULL DEFAULT NULL AFTER `pi_temp`;

ALTER TABLE `aggregated`
    ADD COLUMN `min_loft_temp` decimal(6,2) NULL AFTER `min_lux`,
    ADD COLUMN `max_loft_temp` decimal(6,2) NULL AFTER `max_lux`,
    ADD COLUMN `avg_loft_temp` decimal(6,2) NULL AFTER `avg_lux`,
    ADD COLUMN `std_loft_temp` decimal(6,2) NULL AFTER `std_lux`,
    ADD COLUMN `min_loft_humidity` decimal(6,2) NULL AFTER `min_lux`,
    ADD COLUMN `max_loft_humidity` decimal(6,2) NULL AFTER `max_lux`,
    ADD COLUMN `avg_loft_humidity` decimal(6,2) NULL AFTER `avg_lux`,
    ADD COLUMN `std_loft_humidity` decimal(6,2) NULL AFTER `std_lux`;

ALTER TABLE `aggregated_year_month`
    ADD COLUMN avg_loft_temp decimal(6,2) NULL AFTER `avg_ground_temp`,
    ADD COLUMN avg_loft_humidity decimal(6,2) NULL AFTER `avg_ground_temp`;
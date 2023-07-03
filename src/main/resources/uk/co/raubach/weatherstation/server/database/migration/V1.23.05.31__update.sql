ALTER TABLE `measurements`
    ADD COLUMN `lux` decimal(8,2) NULL DEFAULT 0 AFTER `pi_temp`;

ALTER TABLE `aggregated`
    ADD COLUMN `min_lux` decimal(8, 2) NULL AFTER `min_pi_temp`,
    ADD COLUMN `max_lux` decimal(8, 2) NULL AFTER `max_pi_temp`,
    ADD COLUMN `avg_lux` decimal(8, 2) NULL AFTER `avg_pi_temp`,
    ADD COLUMN `std_lux` decimal(8, 2) NULL AFTER `std_pi_temp`;

ALTER TABLE `aggregated`
ADD COLUMN `min_pi_temp` decimal(6, 2) NULL AFTER `min_ground_temp`,
ADD COLUMN `max_pi_temp` decimal(6, 2) NULL AFTER `max_ground_temp`,
ADD COLUMN `avg_pi_temp` decimal(6, 2) NULL AFTER `avg_ground_temp`,
ADD COLUMN `std_pi_temp` decimal(6, 2) NULL AFTER `std_ground_temp`;
ALTER TABLE `measurements`
ADD COLUMN `uploaded_wu` tinyint(1) NOT NULL DEFAULT 0 AFTER `pi_temp`;
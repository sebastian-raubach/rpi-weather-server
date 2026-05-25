ALTER TABLE `aggregated_year_month`
    ADD COLUMN max_ambient_temp decimal(6,2) NULL AFTER `avg_ambient_temp`,
    ADD COLUMN min_ambient_temp decimal(6,2) NULL AFTER `avg_ambient_temp`;
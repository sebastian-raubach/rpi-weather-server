DROP TABLE IF EXISTS `aggregated_year_month`;
CREATE TABLE `aggregated_year_month`  (
  `avg_ambient_temp` decimal(6, 2) NULL DEFAULT NULL,
  `avg_ground_temp` decimal(6, 2) NULL DEFAULT NULL,
  `avg_lux` decimal(8, 2) NULL DEFAULT NULL,
  `avg_pressure` decimal(6, 2) NULL DEFAULT NULL,
  `avg_humidity` decimal(6, 2) NULL DEFAULT NULL,
  `avg_wind_speed` decimal(6, 2) NULL DEFAULT NULL,
  `avg_wind_gust` decimal(6, 2) NULL DEFAULT NULL,
  `sum_rainfall` decimal(6, 2) NULL DEFAULT NULL,
  `year` smallint NOT NULL,
  `month` smallint NOT NULL,
  PRIMARY KEY (`year`, `month`) USING BTREE
);
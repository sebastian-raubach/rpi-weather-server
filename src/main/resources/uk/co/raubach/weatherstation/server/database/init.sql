DROP TABLE IF EXISTS `measurements`;
CREATE TABLE IF NOT EXISTS `measurements` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ambient_temp` decimal(6,2) DEFAULT NULL,
  `ground_temp` decimal(6,2) DEFAULT NULL,
  `pressure` decimal(6,2) DEFAULT NULL,
  `humidity` decimal(6,2) DEFAULT NULL,
  `wind_average` decimal(6,2) DEFAULT NULL,
  `wind_speed` decimal(6,2) DEFAULT NULL,
  `wind_gust` decimal(6,2) DEFAULT NULL,
  `rainfall` decimal(6,2) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
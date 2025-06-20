/*
 * This file is generated by jOOQ.
 */
package uk.co.raubach.weatherstation.server.database.codegen.tables.pojos;


import java.io.Serializable;
import java.math.BigDecimal;


// @formatter:off
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AggregatedYearMonth implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal avgAmbientTemp;
    private BigDecimal avgGroundTemp;
    private BigDecimal avgLux;
    private BigDecimal avgPressure;
    private BigDecimal avgHumidity;
    private BigDecimal avgWindSpeed;
    private BigDecimal avgWindGust;
    private BigDecimal sumRainfall;
    private Short      year;
    private Short      month;

    public AggregatedYearMonth() {}

    public AggregatedYearMonth(AggregatedYearMonth value) {
        this.avgAmbientTemp = value.avgAmbientTemp;
        this.avgGroundTemp = value.avgGroundTemp;
        this.avgLux = value.avgLux;
        this.avgPressure = value.avgPressure;
        this.avgHumidity = value.avgHumidity;
        this.avgWindSpeed = value.avgWindSpeed;
        this.avgWindGust = value.avgWindGust;
        this.sumRainfall = value.sumRainfall;
        this.year = value.year;
        this.month = value.month;
    }

    public AggregatedYearMonth(
        BigDecimal avgAmbientTemp,
        BigDecimal avgGroundTemp,
        BigDecimal avgLux,
        BigDecimal avgPressure,
        BigDecimal avgHumidity,
        BigDecimal avgWindSpeed,
        BigDecimal avgWindGust,
        BigDecimal sumRainfall,
        Short      year,
        Short      month
    ) {
        this.avgAmbientTemp = avgAmbientTemp;
        this.avgGroundTemp = avgGroundTemp;
        this.avgLux = avgLux;
        this.avgPressure = avgPressure;
        this.avgHumidity = avgHumidity;
        this.avgWindSpeed = avgWindSpeed;
        this.avgWindGust = avgWindGust;
        this.sumRainfall = sumRainfall;
        this.year = year;
        this.month = month;
    }

    /**
     * Getter for
     * <code>weatherstation_db.aggregated_year_month.avg_ambient_temp</code>.
     */
    public BigDecimal getAvgAmbientTemp() {
        return this.avgAmbientTemp;
    }

    /**
     * Setter for
     * <code>weatherstation_db.aggregated_year_month.avg_ambient_temp</code>.
     */
    public void setAvgAmbientTemp(BigDecimal avgAmbientTemp) {
        this.avgAmbientTemp = avgAmbientTemp;
    }

    /**
     * Getter for
     * <code>weatherstation_db.aggregated_year_month.avg_ground_temp</code>.
     */
    public BigDecimal getAvgGroundTemp() {
        return this.avgGroundTemp;
    }

    /**
     * Setter for
     * <code>weatherstation_db.aggregated_year_month.avg_ground_temp</code>.
     */
    public void setAvgGroundTemp(BigDecimal avgGroundTemp) {
        this.avgGroundTemp = avgGroundTemp;
    }

    /**
     * Getter for <code>weatherstation_db.aggregated_year_month.avg_lux</code>.
     */
    public BigDecimal getAvgLux() {
        return this.avgLux;
    }

    /**
     * Setter for <code>weatherstation_db.aggregated_year_month.avg_lux</code>.
     */
    public void setAvgLux(BigDecimal avgLux) {
        this.avgLux = avgLux;
    }

    /**
     * Getter for
     * <code>weatherstation_db.aggregated_year_month.avg_pressure</code>.
     */
    public BigDecimal getAvgPressure() {
        return this.avgPressure;
    }

    /**
     * Setter for
     * <code>weatherstation_db.aggregated_year_month.avg_pressure</code>.
     */
    public void setAvgPressure(BigDecimal avgPressure) {
        this.avgPressure = avgPressure;
    }

    /**
     * Getter for
     * <code>weatherstation_db.aggregated_year_month.avg_humidity</code>.
     */
    public BigDecimal getAvgHumidity() {
        return this.avgHumidity;
    }

    /**
     * Setter for
     * <code>weatherstation_db.aggregated_year_month.avg_humidity</code>.
     */
    public void setAvgHumidity(BigDecimal avgHumidity) {
        this.avgHumidity = avgHumidity;
    }

    /**
     * Getter for
     * <code>weatherstation_db.aggregated_year_month.avg_wind_speed</code>.
     */
    public BigDecimal getAvgWindSpeed() {
        return this.avgWindSpeed;
    }

    /**
     * Setter for
     * <code>weatherstation_db.aggregated_year_month.avg_wind_speed</code>.
     */
    public void setAvgWindSpeed(BigDecimal avgWindSpeed) {
        this.avgWindSpeed = avgWindSpeed;
    }

    /**
     * Getter for
     * <code>weatherstation_db.aggregated_year_month.avg_wind_gust</code>.
     */
    public BigDecimal getAvgWindGust() {
        return this.avgWindGust;
    }

    /**
     * Setter for
     * <code>weatherstation_db.aggregated_year_month.avg_wind_gust</code>.
     */
    public void setAvgWindGust(BigDecimal avgWindGust) {
        this.avgWindGust = avgWindGust;
    }

    /**
     * Getter for
     * <code>weatherstation_db.aggregated_year_month.sum_rainfall</code>.
     */
    public BigDecimal getSumRainfall() {
        return this.sumRainfall;
    }

    /**
     * Setter for
     * <code>weatherstation_db.aggregated_year_month.sum_rainfall</code>.
     */
    public void setSumRainfall(BigDecimal sumRainfall) {
        this.sumRainfall = sumRainfall;
    }

    /**
     * Getter for <code>weatherstation_db.aggregated_year_month.year</code>.
     */
    public Short getYear() {
        return this.year;
    }

    /**
     * Setter for <code>weatherstation_db.aggregated_year_month.year</code>.
     */
    public void setYear(Short year) {
        this.year = year;
    }

    /**
     * Getter for <code>weatherstation_db.aggregated_year_month.month</code>.
     */
    public Short getMonth() {
        return this.month;
    }

    /**
     * Setter for <code>weatherstation_db.aggregated_year_month.month</code>.
     */
    public void setMonth(Short month) {
        this.month = month;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("AggregatedYearMonth (");

        sb.append(avgAmbientTemp);
        sb.append(", ").append(avgGroundTemp);
        sb.append(", ").append(avgLux);
        sb.append(", ").append(avgPressure);
        sb.append(", ").append(avgHumidity);
        sb.append(", ").append(avgWindSpeed);
        sb.append(", ").append(avgWindGust);
        sb.append(", ").append(sumRainfall);
        sb.append(", ").append(year);
        sb.append(", ").append(month);

        sb.append(")");
        return sb.toString();
    }
    // @formatter:on
}

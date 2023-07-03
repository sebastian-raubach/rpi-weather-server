/*
 * This file is generated by jOOQ.
 */
package uk.co.raubach.weatherstation.server.database.codegen.tables;


import java.math.BigDecimal;
import java.sql.Date;

import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import uk.co.raubach.weatherstation.server.database.codegen.WeatherstationDb;
import uk.co.raubach.weatherstation.server.database.codegen.tables.records.AggregatedRecord;


// @formatter:off
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Aggregated extends TableImpl<AggregatedRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>weatherstation_db.aggregated</code>
     */
    public static final Aggregated AGGREGATED = new Aggregated();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AggregatedRecord> getRecordType() {
        return AggregatedRecord.class;
    }

    /**
     * The column <code>weatherstation_db.aggregated.min_ambient_temp</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MIN_AMBIENT_TEMP = createField(DSL.name("min_ambient_temp"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.min_ground_temp</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MIN_GROUND_TEMP = createField(DSL.name("min_ground_temp"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.min_pi_temp</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MIN_PI_TEMP = createField(DSL.name("min_pi_temp"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.min_lux</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MIN_LUX = createField(DSL.name("min_lux"), SQLDataType.DECIMAL(8, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.min_pressure</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MIN_PRESSURE = createField(DSL.name("min_pressure"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.min_humidity</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MIN_HUMIDITY = createField(DSL.name("min_humidity"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.min_wind_average</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MIN_WIND_AVERAGE = createField(DSL.name("min_wind_average"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.min_wind_speed</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MIN_WIND_SPEED = createField(DSL.name("min_wind_speed"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.min_wind_gust</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MIN_WIND_GUST = createField(DSL.name("min_wind_gust"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.max_ambient_temp</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MAX_AMBIENT_TEMP = createField(DSL.name("max_ambient_temp"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.max_ground_temp</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MAX_GROUND_TEMP = createField(DSL.name("max_ground_temp"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.max_pi_temp</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MAX_PI_TEMP = createField(DSL.name("max_pi_temp"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.max_lux</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MAX_LUX = createField(DSL.name("max_lux"), SQLDataType.DECIMAL(8, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.max_pressure</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MAX_PRESSURE = createField(DSL.name("max_pressure"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.max_humidity</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MAX_HUMIDITY = createField(DSL.name("max_humidity"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.max_wind_average</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MAX_WIND_AVERAGE = createField(DSL.name("max_wind_average"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.max_wind_speed</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MAX_WIND_SPEED = createField(DSL.name("max_wind_speed"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.max_wind_gust</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MAX_WIND_GUST = createField(DSL.name("max_wind_gust"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.avg_ambient_temp</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> AVG_AMBIENT_TEMP = createField(DSL.name("avg_ambient_temp"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.avg_ground_temp</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> AVG_GROUND_TEMP = createField(DSL.name("avg_ground_temp"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.avg_pi_temp</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> AVG_PI_TEMP = createField(DSL.name("avg_pi_temp"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.avg_lux</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> AVG_LUX = createField(DSL.name("avg_lux"), SQLDataType.DECIMAL(8, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.avg_pressure</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> AVG_PRESSURE = createField(DSL.name("avg_pressure"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.avg_humidity</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> AVG_HUMIDITY = createField(DSL.name("avg_humidity"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.avg_wind_average</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> AVG_WIND_AVERAGE = createField(DSL.name("avg_wind_average"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.avg_wind_speed</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> AVG_WIND_SPEED = createField(DSL.name("avg_wind_speed"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.avg_wind_gust</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> AVG_WIND_GUST = createField(DSL.name("avg_wind_gust"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.std_ambient_temp</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> STD_AMBIENT_TEMP = createField(DSL.name("std_ambient_temp"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.std_ground_temp</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> STD_GROUND_TEMP = createField(DSL.name("std_ground_temp"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.std_pi_temp</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> STD_PI_TEMP = createField(DSL.name("std_pi_temp"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.std_lux</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> STD_LUX = createField(DSL.name("std_lux"), SQLDataType.DECIMAL(8, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.std_pressure</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> STD_PRESSURE = createField(DSL.name("std_pressure"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.std_humidity</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> STD_HUMIDITY = createField(DSL.name("std_humidity"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.std_wind_average</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> STD_WIND_AVERAGE = createField(DSL.name("std_wind_average"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.std_wind_speed</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> STD_WIND_SPEED = createField(DSL.name("std_wind_speed"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.std_wind_gust</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> STD_WIND_GUST = createField(DSL.name("std_wind_gust"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.sum_rainfall</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> SUM_RAINFALL = createField(DSL.name("sum_rainfall"), SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.date</code>.
     */
    public final TableField<AggregatedRecord, Date> DATE = createField(DSL.name("date"), SQLDataType.DATE.nullable(false), this, "");

    private Aggregated(Name alias, Table<AggregatedRecord> aliased) {
        this(alias, aliased, null);
    }

    private Aggregated(Name alias, Table<AggregatedRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>weatherstation_db.aggregated</code> table
     * reference
     */
    public Aggregated(String alias) {
        this(DSL.name(alias), AGGREGATED);
    }

    /**
     * Create an aliased <code>weatherstation_db.aggregated</code> table
     * reference
     */
    public Aggregated(Name alias) {
        this(alias, AGGREGATED);
    }

    /**
     * Create a <code>weatherstation_db.aggregated</code> table reference
     */
    public Aggregated() {
        this(DSL.name("aggregated"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : WeatherstationDb.WEATHERSTATION_DB;
    }

    @Override
    public UniqueKey<AggregatedRecord> getPrimaryKey() {
        return Internal.createUniqueKey(Aggregated.AGGREGATED, DSL.name("KEY_aggregated_PRIMARY"), new TableField[] { Aggregated.AGGREGATED.DATE }, true);
    }

    @Override
    public Aggregated as(String alias) {
        return new Aggregated(DSL.name(alias), this);
    }

    @Override
    public Aggregated as(Name alias) {
        return new Aggregated(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Aggregated rename(String name) {
        return new Aggregated(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Aggregated rename(Name name) {
        return new Aggregated(name, null);
    }
    // @formatter:on
}

/*
 * This file is generated by jOOQ.
 */
package uk.co.raubach.weatherstation.server.database.codegen.tables;


import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;

import uk.co.raubach.weatherstation.server.database.codegen.WeatherstationDb;
import uk.co.raubach.weatherstation.server.database.codegen.tables.records.AggregatedRecord;


// @formatter:off
/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Aggregated extends TableImpl<AggregatedRecord> {

    private static final long serialVersionUID = 415033339;

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
    public final TableField<AggregatedRecord, BigDecimal> MIN_AMBIENT_TEMP = createField("min_ambient_temp", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.min_ground_temp</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MIN_GROUND_TEMP = createField("min_ground_temp", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.min_pressure</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MIN_PRESSURE = createField("min_pressure", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.min_humidity</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MIN_HUMIDITY = createField("min_humidity", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.min_wind_average</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MIN_WIND_AVERAGE = createField("min_wind_average", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.min_wind_speed</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MIN_WIND_SPEED = createField("min_wind_speed", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.min_wind_gust</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MIN_WIND_GUST = createField("min_wind_gust", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.max_ambient_temp</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MAX_AMBIENT_TEMP = createField("max_ambient_temp", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.max_ground_temp</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MAX_GROUND_TEMP = createField("max_ground_temp", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.max_pressure</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MAX_PRESSURE = createField("max_pressure", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.max_humidity</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MAX_HUMIDITY = createField("max_humidity", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.max_wind_average</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MAX_WIND_AVERAGE = createField("max_wind_average", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.max_wind_speed</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MAX_WIND_SPEED = createField("max_wind_speed", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.max_wind_gust</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> MAX_WIND_GUST = createField("max_wind_gust", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.avg_ambient_temp</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> AVG_AMBIENT_TEMP = createField("avg_ambient_temp", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.avg_ground_temp</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> AVG_GROUND_TEMP = createField("avg_ground_temp", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.avg_pressure</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> AVG_PRESSURE = createField("avg_pressure", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.avg_humidity</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> AVG_HUMIDITY = createField("avg_humidity", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.avg_wind_average</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> AVG_WIND_AVERAGE = createField("avg_wind_average", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.avg_wind_speed</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> AVG_WIND_SPEED = createField("avg_wind_speed", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.avg_wind_gust</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> AVG_WIND_GUST = createField("avg_wind_gust", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.std_ambient_temp</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> STD_AMBIENT_TEMP = createField("std_ambient_temp", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.std_ground_temp</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> STD_GROUND_TEMP = createField("std_ground_temp", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.std_pressure</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> STD_PRESSURE = createField("std_pressure", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.std_humidity</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> STD_HUMIDITY = createField("std_humidity", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.std_wind_average</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> STD_WIND_AVERAGE = createField("std_wind_average", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.std_wind_speed</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> STD_WIND_SPEED = createField("std_wind_speed", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.std_wind_gust</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> STD_WIND_GUST = createField("std_wind_gust", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.sum_rainfall</code>.
     */
    public final TableField<AggregatedRecord, BigDecimal> SUM_RAINFALL = createField("sum_rainfall", org.jooq.impl.SQLDataType.DECIMAL(6, 2), this, "");

    /**
     * The column <code>weatherstation_db.aggregated.date</code>.
     */
    public final TableField<AggregatedRecord, Date> DATE = createField("date", org.jooq.impl.SQLDataType.DATE.nullable(false), this, "");

    /**
     * Create a <code>weatherstation_db.aggregated</code> table reference
     */
    public Aggregated() {
        this(DSL.name("aggregated"), null);
    }

    /**
     * Create an aliased <code>weatherstation_db.aggregated</code> table reference
     */
    public Aggregated(String alias) {
        this(DSL.name(alias), AGGREGATED);
    }

    /**
     * Create an aliased <code>weatherstation_db.aggregated</code> table reference
     */
    public Aggregated(Name alias) {
        this(alias, AGGREGATED);
    }

    private Aggregated(Name alias, Table<AggregatedRecord> aliased) {
        this(alias, aliased, null);
    }

    private Aggregated(Name alias, Table<AggregatedRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return WeatherstationDb.WEATHERSTATION_DB;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<AggregatedRecord> getPrimaryKey() {
        return Internal.createUniqueKey(uk.co.raubach.weatherstation.server.database.codegen.tables.Aggregated.AGGREGATED, "KEY_aggregated_PRIMARY", uk.co.raubach.weatherstation.server.database.codegen.tables.Aggregated.AGGREGATED.DATE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<AggregatedRecord>> getKeys() {
        return Arrays.<UniqueKey<AggregatedRecord>>asList(
              Internal.createUniqueKey(uk.co.raubach.weatherstation.server.database.codegen.tables.Aggregated.AGGREGATED, "KEY_aggregated_PRIMARY", uk.co.raubach.weatherstation.server.database.codegen.tables.Aggregated.AGGREGATED.DATE)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Aggregated as(String alias) {
        return new Aggregated(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
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
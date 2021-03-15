/*
 * This file is generated by jOOQ.
 */
package uk.co.raubach.weatherstation.server.database.codegen.tables;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import uk.co.raubach.weatherstation.server.database.codegen.WeatherstationDb;
import uk.co.raubach.weatherstation.server.database.codegen.tables.records.ViewStatsDailyRecord;


// @formatter:off
/**
 * VIEW
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ViewStatsDaily extends TableImpl<ViewStatsDailyRecord> {

    private static final long serialVersionUID = 489571149;

    /**
     * The reference instance of <code>weatherstation_db.view_stats_daily</code>
     */
    public static final ViewStatsDaily VIEW_STATS_DAILY = new ViewStatsDaily();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ViewStatsDailyRecord> getRecordType() {
        return ViewStatsDailyRecord.class;
    }

    /**
     * The column <code>weatherstation_db.view_stats_daily.agrType</code>.
     */
    public final TableField<ViewStatsDailyRecord, String> AGRTYPE = createField("agrType", org.jooq.impl.SQLDataType.VARCHAR(4).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>weatherstation_db.view_stats_daily.ambient_temp</code>.
     */
    public final TableField<ViewStatsDailyRecord, Double> AMBIENT_TEMP = createField("ambient_temp", org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>weatherstation_db.view_stats_daily.ground_temp</code>.
     */
    public final TableField<ViewStatsDailyRecord, Double> GROUND_TEMP = createField("ground_temp", org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>weatherstation_db.view_stats_daily.pressure</code>.
     */
    public final TableField<ViewStatsDailyRecord, Double> PRESSURE = createField("pressure", org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>weatherstation_db.view_stats_daily.humidity</code>.
     */
    public final TableField<ViewStatsDailyRecord, Double> HUMIDITY = createField("humidity", org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>weatherstation_db.view_stats_daily.wind_average</code>.
     */
    public final TableField<ViewStatsDailyRecord, Double> WIND_AVERAGE = createField("wind_average", org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>weatherstation_db.view_stats_daily.wind_speed</code>.
     */
    public final TableField<ViewStatsDailyRecord, Double> WIND_SPEED = createField("wind_speed", org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>weatherstation_db.view_stats_daily.wind_gust</code>.
     */
    public final TableField<ViewStatsDailyRecord, Double> WIND_GUST = createField("wind_gust", org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>weatherstation_db.view_stats_daily.rainfall</code>.
     */
    public final TableField<ViewStatsDailyRecord, Double> RAINFALL = createField("rainfall", org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>weatherstation_db.view_stats_daily.date</code>.
     */
    public final TableField<ViewStatsDailyRecord, String> DATE = createField("date", org.jooq.impl.SQLDataType.VARCHAR(10), this, "");

    /**
     * Create a <code>weatherstation_db.view_stats_daily</code> table reference
     */
    public ViewStatsDaily() {
        this(DSL.name("view_stats_daily"), null);
    }

    /**
     * Create an aliased <code>weatherstation_db.view_stats_daily</code> table reference
     */
    public ViewStatsDaily(String alias) {
        this(DSL.name(alias), VIEW_STATS_DAILY);
    }

    /**
     * Create an aliased <code>weatherstation_db.view_stats_daily</code> table reference
     */
    public ViewStatsDaily(Name alias) {
        this(alias, VIEW_STATS_DAILY);
    }

    private ViewStatsDaily(Name alias, Table<ViewStatsDailyRecord> aliased) {
        this(alias, aliased, null);
    }

    private ViewStatsDaily(Name alias, Table<ViewStatsDailyRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("VIEW"));
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
    public ViewStatsDaily as(String alias) {
        return new ViewStatsDaily(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ViewStatsDaily as(Name alias) {
        return new ViewStatsDaily(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ViewStatsDaily rename(String name) {
        return new ViewStatsDaily(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ViewStatsDaily rename(Name name) {
        return new ViewStatsDaily(name, null);
    }
// @formatter:on
}

/*
 * This file is generated by jOOQ.
 */
package uk.co.raubach.weatherstation.server.database.codegen;


import javax.annotation.Generated;

import org.jooq.Catalog;
import org.jooq.impl.SchemaImpl;


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
public class WeatherstationDb extends SchemaImpl {

    private static final long serialVersionUID = -1238155986;

    /**
     * The reference instance of <code>weatherstation_db</code>
     */
    public static final WeatherstationDb WEATHERSTATION_DB = new WeatherstationDb();

    /**
     * No further instances allowed
     */
    private WeatherstationDb() {
        super("weatherstation_db", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }
// @formatter:on
}

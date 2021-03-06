/*
 * This file is generated by jOOQ.
 */
package co.lopun.core.model.gen;


import co.lopun.core.model.gen.tables.Shop;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class JooqExample extends SchemaImpl {

    private static final long serialVersionUID = 124600696;

    /**
     * The reference instance of <code>jooq_example</code>
     */
    public static final JooqExample JOOQ_EXAMPLE = new JooqExample();

    /**
     * The table <code>jooq_example.shop</code>.
     */
    public final Shop SHOP = Shop.SHOP;

    /**
     * No further instances allowed
     */
    private JooqExample() {
        super("jooq_example", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.<Table<?>>asList(
            Shop.SHOP);
    }
}

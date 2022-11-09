package com.urise.webapp.sql;

import java.sql.SQLException;

public interface DataBaseConsumer<P> {
    public void accept( P p) throws SQLException;
}

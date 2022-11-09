package com.urise.webapp.sql;

import java.sql.SQLException;

public interface DataBaseFunction<T, K> {
    public K apply(T ps) throws SQLException;
}

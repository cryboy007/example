package com.github.cryboy007.jsql;

import net.sf.jsqlparser.util.validation.Validation;
import net.sf.jsqlparser.util.validation.ValidationError;
import net.sf.jsqlparser.util.validation.feature.DatabaseType;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName ValidationExample
 * @Author tao.he
 * @Since 2022/5/20 16:14
 *
 * 4.0版本才有
 */
public class ValidationExample {
    public static void main(String[] args) {
        //validDatabaseType();


    }

    private static void validDatabaseType() {
        String sql = "DROP INDEX IF EXISTS idx_tab2_id;";

// validate statement if it's valid for all given databases.
        Validation validation = new Validation(Arrays.asList(DatabaseType.SQLSERVER, DatabaseType.MARIADB,
                DatabaseType.POSTGRESQL, DatabaseType.H2), sql);
        List<ValidationError> errors = validation.validate();

    }
}

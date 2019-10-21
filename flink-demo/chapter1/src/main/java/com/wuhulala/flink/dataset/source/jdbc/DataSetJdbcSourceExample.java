
package com.wuhulala.flink.dataset.source.jdbc;

import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.io.jdbc.JDBCInputFormat;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.types.Row;

/**
 * jdbc 源
 *
 * @author wuhulala
 */
public class DataSetJdbcSourceExample {

    private static String driverClass = "com.mysql.jdbc.Driver";
    private static String dbUrl = "jdbc:mysql://mysqlhost:3306/csdn_analyzer";
    private static String userName = "root";
    private static String passWord = "";

    public static void main(String[] args) throws Exception {
        // set up the batch execution environment
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        TypeInformation<?>[] fieldTypes = new TypeInformation<?>[] {
                BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.INT_TYPE_INFO
        };
        RowTypeInfo rowTypeInfo = new RowTypeInfo(fieldTypes);


        JDBCInputFormat jdbcInputFormat = JDBCInputFormat.buildJDBCInputFormat()
                .setDrivername(driverClass)
                .setDBUrl(dbUrl)
                .setUsername(userName)
                .setPassword(passWord)
                .setQuery("select input_date,read_num from csdn_pv_2018 limit 10")
                .setRowTypeInfo(rowTypeInfo)
                .finish();
        DataSource<Row> input = env.createInput(jdbcInputFormat);
        input.print();

    }
}

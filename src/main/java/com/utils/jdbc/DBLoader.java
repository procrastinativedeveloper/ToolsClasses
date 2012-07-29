package com.utils.jdbc;

import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.AmbiguousTableNameException;
import org.dbunit.dataset.*;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Load data to db. Use DBUnit.
 *
 * http://koziolekweb.pl/2012/06/05/prosty-helper-do-ladowania-danych/
 *
 * USAGE
        @BeforeClass
        protected void setUp() throws Exception {
        forDB("org.hsqldb.jdbcDriver", "jdbc:hsqldb:file:src/test/resources/db/unit-testing-jpa;shutdown=true",
        "sa", "").cleanInsert(dataSet(table("AIO", //
        cols("name", "pass","regtime"), //
        row("aaa", "aaa", date(new Date())), //
        row("bbb", "aaa", date(new Date())), //
        row("ccc", "aaa", date(new Date())) //
        )));
        }
 *
 * @author bartlomiejk
 *
 */
public class DBLoader {

    /**
     * DBUnit date format.
     */
    private static final SimpleDateFormat DB_UNIT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * DBUnit time format.
     */
    private static final SimpleDateFormat DB_UNIT_TIME_FORMAT = new SimpleDateFormat("hh:mm:ss");

    /**
     * DBUnit timestamp format.
     */
    private static final SimpleDateFormat DB_UNIT_TIMESTAMP_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd hh:mm:ss.SSSSSSSSS");

    /**
     * Simple tester.
     */
    private JdbcDatabaseTester databaseTester;

    /**
     * Create loader.
     *
     * @param databaseTester
     */
    private DBLoader(JdbcDatabaseTester databaseTester) {
        this.databaseTester = databaseTester;
    }

    /**
     * Deete data than insert.
     *
     * @param dataSet
     *            data.
     * @throws Exception
     */
    public void cleanInsert(IDataSet dataSet) throws Exception {
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(), dataSet);
    }

    /**
     * Create columns names array.
     *
     * @param names
     * @return
     */
    public static String[] cols(String... names) {
        return names;
    }

    /**
     * Create {@link IDataSet} from {@link ITable}.
     *
     * @param tables
     * @return
     * @throws AmbiguousTableNameException
     */
    public static IDataSet dataSet(ITable... tables) throws AmbiguousTableNameException {
        return new DefaultDataSet(tables);
    }

    /**
     * Create DBUnit-valid date {@link String} from {@link Date}.
     *
     * @param date
     * @return
     */
    public static String date(Date date) {
        return DB_UNIT_DATE_FORMAT.format(date);
    }

    /**
     * Create loader.
     *
     * @param driverClassName
     * @param url
     * @param username
     * @param password
     * @return
     * @throws ClassNotFoundException
     */
    public static DBLoader forDB(String driverClassName, String url, String username, String password)
            throws ClassNotFoundException {
        return new DBLoader(new JdbcDatabaseTester(driverClassName, url, username, password));
    }

    /**
     * Load from flat XML.
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public static IDataSet fromFlatXml(String fileName) throws Exception {
        return new FlatXmlDataSetBuilder().build(new File(fileName));
    }

    /**
     * Load from XML with DTD.
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public static IDataSet fromXls(String fileName) throws Exception {
        return new XlsDataSet(new FileInputStream(new File(fileName)));
    }

    /**
     * Load from xls.
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public static IDataSet fromXml(String fileName) throws Exception {
        return new XmlDataSet(new FileInputStream(new File(fileName)));
    }

    /**
     * Map data to row.
     *
     * @param rowData
     * @return
     */
    public static String[] row(String... rowData) {
        return rowData;
    }

    /**
     * Create {@link ITable} with name, columns and data.
     *
     * @param name
     * @param cols
     * @param rows
     * @return
     * @throws DataSetException
     */
    public static ITable table(String name, String[] cols, String[]... rows) throws DataSetException {
        return addRows(defineTable(name, cols), rows);
    }

    /**
     * Create DBUnit-valid time {@link String} from {@link Date}.
     *
     * @param date
     * @return
     */
    public static String time(Date date) {
        return DB_UNIT_TIME_FORMAT.format(date);
    }

    /**
     * Create DBUnit-valid timestamp {@link String} from {@link Date}.
     *
     * @param date
     * @return
     */
    public static String timestamp(Date date) {
        return DB_UNIT_TIMESTAMP_FORMAT.format(date);
    }

    /**
     * Add rows to tabel.
     *
     * @param table
     * @param rows
     * @return
     * @throws DataSetException
     */
    private static ITable addRows(DefaultTable table, String[]... rows) throws DataSetException {
        for (String[] row : rows) {
            table.addRow(row);
        }
        return table;
    }

    /**
     * Create table.
     *
     * @param name
     * @param cols
     * @return
     */
    private static DefaultTable defineTable(String name, String[] cols) {
        return new DefaultTable(name, makeColumns(cols));
    }

    /**
     * Map columns names to {@link Column} array.
     *
     * @param cols
     * @return
     */
    private static Column[] makeColumns(String[] cols) {
        Column[] columns = new Column[cols.length];
        for (int i = 0; i < cols.length; i++) {
            columns[i] = new Column(cols[i], DataType.UNKNOWN);
        }
        return columns;
    }
}

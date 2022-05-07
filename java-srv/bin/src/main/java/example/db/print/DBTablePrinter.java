/*
Database Table Printer
Copyright (C) 2014  Hami Galip Torun

Email: hamitorun@e-fabrika.net
Project Home: https://github.com/htorun/dbtableprinter

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/*
This is my first Java program that does something more or less
useful. It is part of my effort to learn Java, how to use
an IDE (IntelliJ IDEA 13.1.15 in this case), how to apply an
open source license and how to use Git and GitHub (https://github.com)
for version control and publishing an open source software.

Hami
 */

package example.db.print;

import java.sql.*;

import org.apache.log4j.Logger;
/**
 * Just a utility to print rows from a given DB table or a
 * <code>ResultSet</code> to standard out, formatted to look
 * like a table with rows and columns with borders.
 *
 * <p>Stack Overflow website
 * (<a target="_blank" href="http://stackoverflow.com">stackoverflow.com</a>)
 * was the primary source of inspiration and help to put this
 * code together. Especially the questions and answers of
 * the following people were very useful:</p>
 *
 */
public class DBTablePrinter {

    private static final Logger logger = Logger.getLogger(DBTablePrinter.class);
    /**
     * Default maximum number of rows to query and print.
     */
    private static final int DEFAULT_MAX_ROWS = 10;

    /**
     * Default maximum width for text columns
     * (like a <code>VARCHAR</code>) column.
     */
    private static final int DEFAULT_MAX_TEXT_COL_WIDTH = 150;

    /**
     * Overloaded method that prints rows from table <code>tableName</code>
     * to standard out using the given database connection
     * <code>conn</code>. Total number of rows will be limited to
     * {@link #DEFAULT_MAX_ROWS} and
     * {@link #DEFAULT_MAX_TEXT_COL_WIDTH} will be used to limit
     * the width of text columns (like a <code>VARCHAR</code> column).
     *
     * @param conn Database connection object (java.sql.Connection)
     * @param tableName Name of the database table
     */
    public static void printTable(Connection conn, String tableName, example.db.print.output.IOutput output){
        printTable(conn, tableName, DEFAULT_MAX_ROWS, DEFAULT_MAX_TEXT_COL_WIDTH, output);
    }

    /**
     * Overloaded method that prints rows from table <code>tableName</code>
     * to standard out using the given database connection
     * <code>conn</code>. Total number of rows will be limited to
     * <code>maxRows</code> and
     * {@link #DEFAULT_MAX_TEXT_COL_WIDTH} will be used to limit
     * the width of text columns (like a <code>VARCHAR</code> column).
     *
     * @param conn Database connection object (java.sql.Connection)
     * @param tableName Name of the database table
     * @param maxRows Number of max. rows to query and print
     */
    public static void printTable(Connection conn, String tableName, int maxRows, example.db.print.output.IOutput output) {
        printTable(conn, tableName, maxRows, DEFAULT_MAX_TEXT_COL_WIDTH, output);
    }

    /**
     * Overloaded method that prints rows from table <code>tableName</code>
     * to standard out using the given database connection
     * <code>conn</code>. Total number of rows will be limited to
     * <code>maxRows</code> and
     * <code>maxStringColWidth</code> will be used to limit
     * the width of text columns (like a <code>VARCHAR</code> column).
     *
     * @param conn Database connection object (java.sql.Connection)
     * @param tableName Name of the database table
     * @param maxRows Number of max. rows to query and print
     * @param maxStringColWidth Max. width of text columns
     */
    public static void printTable(Connection conn, String tableName, int maxRows, int maxStringColWidth, example.db.print.output.IOutput output) {
        if (conn == null) {
            logger.error("No connection to database (Connection is null)!");
            return;
        }
        if (tableName == null) {
            logger.error("No table name (tableName is null)!");
            return;
        }
        if (tableName.length() == 0) {
            logger.error("Empty table name!");
            return;
        }
        if (maxRows < 1) {
            logger.warn("Invalid max. rows number. Using default!");
            maxRows = DEFAULT_MAX_ROWS;
        }

        Statement stmt = null;
        ResultSet rs = null;
        try {
            if (conn.isClosed()) {
                logger.error("Connection is closed!");
                return;
            }

            String sqlSelectAll = "SELECT * FROM " + tableName + " LIMIT " + maxRows;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlSelectAll);

            DBResultsetPrint.printResultSet(rs, maxStringColWidth, output);

        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignore) {
                // ignore
            }
        }
    }

    /**
     * Overloaded method to print rows of a <a target="_blank"
     * href="http://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html">
     * ResultSet</a> to standard out using {@link #DEFAULT_MAX_TEXT_COL_WIDTH}
     * to limit the width of text columns.
     *
     * @param rs The <code>ResultSet</code> to print
     */
    public static void printResultSet(ResultSet rs, example.db.print.output.IOutput output) {
        DBResultsetPrint.printResultSet(rs, DEFAULT_MAX_TEXT_COL_WIDTH, output);
    }
}

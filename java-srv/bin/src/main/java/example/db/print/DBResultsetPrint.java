package example.db.print;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.apache.log4j.Logger;

public class DBResultsetPrint {
  private static List<Column> columns;
  private static List<String> tableNames;
  private static example.db.print.output.IOutput output = new example.db.print.output.ToConsole();
  private static final Logger logger = Logger.getLogger(DBResultsetPrint.class);

  /**
   * Default maximum width for text columns
   * (like a <code>VARCHAR</code>) column.
   */
  private static final int DEFAULT_MAX_TEXT_COL_WIDTH = 150;

  /**
   * Overloaded method to print rows of a <a target="_blank"
   * href="http://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html">
   * ResultSet</a> to standard out using <code>maxStringColWidth</code>
   * to limit the width of text columns.
   *
   * @param rs The <code>ResultSet</code> to print
   * @param maxStringColWidth Max. width of text columns
   */
  public static void printResultSet(ResultSet rs, int maxStringColWidth, example.db.print.output.IOutput media) {
      try {
          if (rs == null) {
              logger.error("Result set is null!");
              return;
          }
          if (rs.isClosed()) {
              logger.error("Result Set is closed!");
              return;
          }
          if (maxStringColWidth < 1) {
              logger.warn("Invalid max. varchar column width. Using default!");
              maxStringColWidth = DEFAULT_MAX_TEXT_COL_WIDTH;
          }
          if (media == null) {
              logger.warn("Results will go to console");
          } else {
            output = media;
          }
          // Get the meta data object of this ResultSet.
          ResultSetMetaData rsmd;
          rsmd = rs.getMetaData();

          // Total number of columns in this ResultSet
          int columnCount = rsmd.getColumnCount();

          // List of Column objects to store each columns of the ResultSet
          // and the String representation of their values.
          columns = new ArrayList<>(columnCount);

          // List of table names. Can be more than one if it is a joined
          // table query
          tableNames = new ArrayList<>(columnCount);

          addColumns(columnCount, rsmd);

          // Go through each row, get values of each column and adjust
          // column widths.
          int rowCount = 0;
          while (rs.next()) {

              // NOTE: columnIndex for rs.getXXX methods STARTS AT 1 NOT 0
              for (int i = 0; i < columnCount; i++) {
                  Column c = columns.get(i);
                  ECatagory category = c.getTypeCategory();
                  category.addValue(rs, c, maxStringColWidth, i);
              } // END of for loop columnCount
              rowCount++;

          } // END of while (rs.next)

          /*
          At this point we have gone through meta data, get the
          columns and created all Column objects, iterated over the
          ResultSet rows, populated the column values and adjusted
          the column widths.

          We cannot start printing just yet because we have to prepare
          a row separator String.
           */

          // For the fun of it, I will use StringBuilder
          StringBuilder strToPrint = new StringBuilder();
          StringBuilder rowSeparator = new StringBuilder();

          /*
          Prepare column labels to print as well as the row separator.
          It should look something like this:
          +--------+------------+------------+-----------+  (row separator)
          | EMP_NO | BIRTH_DATE | FIRST_NAME | LAST_NAME |  (labels row)
          +--------+------------+------------+-----------+  (row separator)
           */

          // Iterate over columns
          for (Column c : columns) {
              int width = c.getWidth();

            // Center the column label
              String toPrint;
              String name = c.getLabel();
              int diff = width - name.length();

              if ((diff%2) == 1) {
                  // diff is not divisible by 2, add 1 to width (and diff)
                  // so that we can have equal padding to the left and right
                  // of the column label.
                  width++;
                  diff++;
                  c.setWidth(width);
              }

              int paddingSize = diff/2; // InteliJ says casting to int is redundant.

              // Cool String repeater code thanks to user102008 at stackoverflow.com
              // (http://tinyurl.com/7x9qtyg) "Simple way to repeat a string in java"
              String padding = new String(new char[paddingSize]).replace("\0", " ");

              toPrint = "| " + padding + name + padding + " ";
            // END centering the column label

              strToPrint.append(toPrint);

              rowSeparator.append("+");
              rowSeparator.append(new String(new char[width + 2]).replace("\0", "-"));
          }

          printCaption(rowCount);

          printColumnLabels(strToPrint, rowSeparator);
          /*
              Hopefully this should have printed something like this:
              +--------+------------+------------+-----------+--------+-------------+
              | EMP_NO | BIRTH_DATE | FIRST_NAME | LAST_NAME | GENDER |  HIRE_DATE  |
              +--------+------------+------------+-----------+--------+-------------+
              |  10001 | 1953-09-02 | Georgi     | Facello   | M      |  1986-06-26 |
              +--------+------------+------------+-----------+--------+-------------+
              |  10002 | 1964-06-02 | Bezalel    | Simmel    | F      |  1985-11-21 |
              +--------+------------+------------+-----------+--------+-------------+
           */

           printRows(rowCount, rowSeparator);

      } catch (SQLException e) {
          logger.error("SQL exception");
      }
  }
  private static void addColumns(int columnCount, ResultSetMetaData rsmd) throws SQLException {

    // Get the columns and their meta data.
    // NOTE: columnIndex for rsmd.getXXX methods STARTS AT 1 NOT 0
    for (int i = 1; i <= columnCount; i++) {
        Column c = new Column(rsmd.getColumnLabel(i),
                rsmd.getColumnType(i), rsmd.getColumnTypeName(i));
        columns.add(c);

        addTableName(rsmd.getTableName(i));
    }
  }
  private static void addTableName(String name) {
    if (!tableNames.contains(name)) {
        tableNames.add(name);
    }
  }
  private static void printCaption(int rowCount) {
    StringJoiner sj = new StringJoiner(", ");
    for (String name : tableNames) {
        sj.add(name);
    }

    String info = "Printing " + rowCount;
    info += rowCount > 1 ? " rows from " : " row from ";
    info += tableNames.size() > 1 ? "tables " : "table ";
    info += sj.toString();

    output.println(info);
  }
  private static void printColumnLabels(StringBuilder strToPrint, StringBuilder rowSeparator)
  {
    String lineSeparator = System.getProperty("line.separator");

    // Is this really necessary ??
    lineSeparator = lineSeparator == null ? "\n" : lineSeparator;

    rowSeparator.append("+").append(lineSeparator);

    strToPrint.append("|").append(lineSeparator);
    strToPrint.insert(0, rowSeparator);
    strToPrint.append(rowSeparator);

    // Print out the formatted column labels
    output.print(strToPrint.toString());
  }
  private static void printRows(int rowCount, StringBuilder rowSeparator){
    String format;

    // Print out the rows
    for (int i = 0; i < rowCount; i++) {
        for (Column c : columns) {

            // This should form a format string like: "%-60s"
            format = String.format("| %%%s%ds ", c.getJustifyFlag(), c.getWidth());
            output.print(
                    String.format(format, c.getValue(i))
            );
        }

        output.println("|");
        output.print(rowSeparator.toString());
    }

    output.println("");
  }
}

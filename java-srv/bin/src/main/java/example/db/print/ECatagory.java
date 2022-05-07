package example.db.print;

import java.sql.*;

public enum ECatagory {
  OTHER(){
    @Override
    public void addValue(ResultSet rs, Column c, int maxStringColWidth, int i) throws SQLException {
      String value = "(" + c.getTypeName() + ")";
      c.adjustWidth(value);
    }
  },
  STRING(){
    @Override
    public void addValue(ResultSet rs, Column c, int maxStringColWidth, int i) throws SQLException {
      String value = rs.getString(i+1) == null ? "NULL" : rs.getString(i+1);
      // Left justify the text columns
      c.justifyLeft();

      // and apply the width limit
      if (value.length() > maxStringColWidth) {
          value = value.substring(0, maxStringColWidth - 3) + "...";
      }
      c.adjustWidth(value);
    }
  },
  INTEGER(){
    @Override
    public void addValue(ResultSet rs, Column c, int maxStringColWidth, int i) throws SQLException {
      String value = rs.getString(i+1) == null ? "NULL" : rs.getString(i+1);
      c.adjustWidth(value);
    }
  },
  DOUBLE(){
    @Override
    public void addValue(ResultSet rs, Column c, int maxStringColWidth, int i) throws SQLException {
      String value = rs.getString(i+1) == null ? "NULL" : rs.getString(i+1);
      if (!value.equals("NULL")) {
          Double dValue = rs.getDouble(i+1);
          value = String.format("%.3f", dValue);
      }
      c.adjustWidth(value);
    }
  },
  DATETIME(){
    @Override
    public void addValue(ResultSet rs, Column c, int maxStringColWidth, int i) throws SQLException {
      String value = rs.getString(i+1) == null ? "NULL" : rs.getString(i+1);
      c.adjustWidth(value);
    }
  },
  BOOLEAN(){
    @Override
    public void addValue(ResultSet rs, Column c, int maxStringColWidth, int i) throws SQLException {
      String value = rs.getString(i+1) == null ? "NULL" : rs.getString(i+1);
      c.adjustWidth(value);
    }
  };

  public abstract void addValue(ResultSet rs, Column c, int maxStringColWidth, int i) throws SQLException;
  /**
   * Takes a generic SQL type and returns the category this type
   * belongs to. Types are categorized according to print formatting
   * needs:
   * <p>
   * Integers should not be truncated so column widths should
   * be adjusted without a column width limit. Text columns should be
   * left justified and can be truncated to a max. column width etc...</p>
   *
   * See also: <a target="_blank"
   * href="http://docs.oracle.com/javase/8/docs/api/java/sql/Types.html">
   * java.sql.Types</a>
   *
   * @param type Generic SQL type
   * @return The category this type belongs to
   */
  public static ECatagory whichCategory(int type) {
      switch (type) {
          case Types.BIGINT:
          case Types.TINYINT:
          case Types.SMALLINT:
          case Types.INTEGER:
              return INTEGER;

          case Types.REAL:
          case Types.DOUBLE:
          case Types.DECIMAL:
              return DOUBLE;

          case Types.DATE:
          case Types.TIME:
          case Types.TIME_WITH_TIMEZONE:
          case Types.TIMESTAMP:
          case Types.TIMESTAMP_WITH_TIMEZONE:
              return DATETIME;

          case Types.BOOLEAN:
              return BOOLEAN;

          case Types.VARCHAR:
          case Types.NVARCHAR:
          case Types.LONGVARCHAR:
          case Types.LONGNVARCHAR:
          case Types.CHAR:
          case Types.NCHAR:
              return STRING;

          default:
              return OTHER;
      }
  }
}

package example.db;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBQuery {

  private static final Logger logger = Logger.getLogger(DBQuery.class);

  public static void query(final String connectionStr, final String tableName, example.db.print.output.IOutput output)
  {
    Connection conn = null;
    try {
        conn = DriverManager.getConnection(connectionStr);
        example.db.print.DBTablePrinter.printTable(conn, tableName, output);
        logger.info("Success connected to database.");
    } catch (SQLException ex) {
        logger.fatal("Fail didn't connect to database.");
    }
    finally {
      if (conn != null) {
          try {
              conn.close();
          } catch (SQLException sqlEx) { } // ignore
          conn = null;
      }
    }
  }
}

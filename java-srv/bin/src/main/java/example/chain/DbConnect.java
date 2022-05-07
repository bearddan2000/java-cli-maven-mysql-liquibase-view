package example.chain;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnect implements ILink {

  private static final Logger logger = Logger.getLogger(DbConnect.class);

  @Override
  public boolean hasResource(final String driverStr, final String connectionStr)
  {
    return this.hasConnection(connectionStr);
  }

  private boolean hasConnection(final String connectionStr)
  {
    boolean result = false;
    Connection conn = null;

    try {
        conn =
           DriverManager.getConnection(connectionStr);
        result = true;
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
    return result;
  }
}

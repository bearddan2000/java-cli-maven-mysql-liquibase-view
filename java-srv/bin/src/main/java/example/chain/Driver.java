package example.chain;

import org.apache.log4j.Logger;

public class Driver implements ILink {

  ILink next = new DbConnect();

  private static final Logger logger = Logger.getLogger(Driver.class);

  @Override
  public boolean hasResource(final String driverStr, final String connectionStr)
  {
    if(this.hasDriver(driverStr))
      return this.next.hasResource(driverStr, connectionStr);
    else
      return false;
  }

  private static boolean hasDriver(final String driverStr)
  {
    boolean result = false;
    try {
        // The newInstance() call is a work around for some
        // broken Java implementations
        Class.forName(driverStr).newInstance();
        result = true;
        logger.info("Success driver found.");
    } catch (Exception ex) {
        logger.fatal("Fail driver not found.");
    }
    return result;
  }
}

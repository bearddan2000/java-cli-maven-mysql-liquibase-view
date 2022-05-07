package example.db.print.output;

import org.apache.log4j.Logger;

public class ToLog implements IOutput {
  private StringBuilder sb = new StringBuilder();
  private static final Logger logger = Logger.getLogger(ToLog.class);

  @Override
  public void print(String str){
    sb.append(str);
  }

  @Override
  public void println(String str){
    sb.append(str);
    logger.info(sb.toString());
    sb = new StringBuilder();
  }
}

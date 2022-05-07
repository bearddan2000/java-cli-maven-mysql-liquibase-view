package example.db.print.output;

import java.io.*;
import java.nio.file.*;
import org.apache.log4j.Logger;

public class ToFile implements IOutput {

  private StringBuilder sb = new StringBuilder();
  private final String FILENAME = "/root/log/output.log";
  private static final Logger logger = Logger.getLogger(ToFile.class);

  public ToFile(){
    create();
  }

  private void create() {
    try {
      File f = new File(FILENAME);
      if (f.createNewFile()) {
        logger.info("File created: " + f.getName());
      } else {
        logger.warn("File already exists.");
      }
    } catch (IOException e) {
      logger.error("An error occurred while creating file.");
    }
  }

  @Override
  public void print(String str){
    sb.append(str);
  }

  @Override
  public void println(String str){
    try {
      sb.append(str+"\n");
      String text = sb.toString();
      Path path = Paths.get(FILENAME);
      Files.write(path, text.getBytes(), StandardOpenOption.APPEND);  //Append mode
      sb = new StringBuilder();
    } catch (IOException e) {
      logger.error("An error occurred while writing file.");
    }
  }
}

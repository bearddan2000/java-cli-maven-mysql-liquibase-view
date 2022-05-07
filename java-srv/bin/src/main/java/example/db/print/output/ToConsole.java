package example.db.print.output;

public class ToConsole implements IOutput {
  @Override
  public void print(String str){
    System.out.print(str);
  }

  @Override
  public void println(String str){
    System.out.println(str);
  }
}

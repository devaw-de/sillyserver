import server.Server;
import java.io.IOException;

public class Starter {

  public static void main(String[] args) throws IOException {
    int port;

    try {
      port = Integer.parseInt(args[0]);
    }
    catch (NumberFormatException e) {
      port = Server.DEFAULT_PORT_NUMBER;
      System.out.println("Could not parse cli input. Switching to default port.");
    }
    catch (ArrayIndexOutOfBoundsException e) {
      port = Server.DEFAULT_PORT_NUMBER;
      System.out.println("No port selected. Switching to default port.");
    }

    if(port < 1000 || port > 65534) {
      port = Server.DEFAULT_PORT_NUMBER;
      System.out.println("Illegal port detected. Please try ports above 1000.");
    }


    Server server = new Server(port);
  }

}

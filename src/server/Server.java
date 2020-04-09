package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class Server extends Thread {

  public static final int DEFAULT_PORT_NUMBER = 9090;
  private static int port;
  private static ServerSocket serverSocket;
  private Socket clientSocket;
  private DataHandler dataHandler;
  private int responseStatus;
  private String responseContent;


  public Server(int p) throws IOException {
    Server.port = p;
    this.dataHandler = new DataHandler();
    this.startServer();
  }


  private void startServer() throws IOException {
    System.out.println("Starting server at localhost:" + Server.port);
    System.out.println("Press CTRL+C to stop");
    Server.serverSocket = new ServerSocket(Server.port);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        serverSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println("Quitting. Bye.");
    }));

    while (true) {
      this.clientSocket = this.waitForRequest();
      String[] parsedRequest = this.parseRequest(this.readRequest());
      this.calculateReply(parsedRequest);
      this.sendReply();
      if(this.clientSocket != null) {
        this.clientSocket.close();
      }
      else {
        System.out.println("Client Socket could not be created/closed. Quitting.");
        break;
      }
    }

  }


  private Socket waitForRequest() throws IOException {
    try {
      Socket socket = Server.serverSocket.accept();
      System.out.println("Client connection established");
      return socket;
    }
    catch (SocketException e) {
      return null; // hacky, but seems to work
    }
  }


  private String readRequest() throws IOException {
    if(this.clientSocket != null) {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
      char[] buffer = new char[1000];
      int requestLength = bufferedReader.read(buffer, 0, 1000);
      return new String(buffer, 0, requestLength);
    }
    else {
      return "GET /"; // Dummy-String for parseRequest(), so calculateReply() does not throw an ArrayOutOfBoundException
    }
  }


  /*
    Split the request headers into a String-Array
    Ignore all headers except for the first line of the HTTP Request
    e.g. "GET /something"
  */
  private String[] parseRequest(String request) {
    String line = request.split("\n")[0];
    String[] requestLine = line.split(" ");
    System.out.println("Request: " + requestLine[0] + " " + requestLine[1]);
    return requestLine;
  }


  /*
    Get HTTP-Status-Code and content for response;
   */
  private void calculateReply(String[] request) {
    this.responseStatus = Http.OK;
    this.responseContent = "";
    switch (request[0]) {
      case Http.METHOD_GET:
        this.responseContent = DataHandler.get(request[1]);
        if(this.responseContent.trim().equals("") || this.responseContent.trim().equals(""+Http.NOT_FOUND)) {
          this.responseStatus = Http.NOT_FOUND;
        }
        break;
      case Http.METHOD_POST:
      case Http.METHOD_PUT:
      case Http.METHOD_DELETE:
        this.responseStatus = Http.NOT_IMPLEMENTED;
        break;
      case Http.METHOD_OPTIONS:
        this.responseContent = Http.ALLOWED_METHODS;
        break;
      default:
        this.responseStatus = Http.METHOD_NOT_ALLOWED;
        this.responseContent = Http.ALLOWED_METHODS;
        break;
    }
    System.out.println("ResponseCode: " + this.responseStatus);
  }


  /*
    Send Response to client
   */
  private void sendReply() throws IOException {
    String httpResponse = Http.getDefaultHeaders(this.responseStatus);
    httpResponse += this.responseContent;
    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));
    printWriter.print(httpResponse);
    printWriter.flush();
  }



}

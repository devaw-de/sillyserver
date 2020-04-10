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
  private String[] request;


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
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println("Quitting. Bye.");
    }));

    while (true) {
      this.clientSocket = this.waitForRequest();
      String[] parsedRequest = this.parseRequest(this.readRequest());
      String[] response = this.calculateReply(parsedRequest);
      this.sendResponse(response);
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
  private String[] calculateReply(String[] request) {
    String responseBody = Http.EMPTY_RESPONSE;
    String responseCode = Http.NOT_IMPLEMENTED;
    int requestId;

    String requestMethod = request[0];
    String requestParameter = request[1].replaceFirst("/", "");

    // If a request parameter is malformed, i.e. non-numeric, we already know the response
    if (!requestParameter.matches("\\d+") && !requestParameter.equals("")) {
      responseCode = Http.BAD_REQUEST;
    } else {
      // Only now checking the full request becomes necessary
      if(!requestParameter.equals("")) {
        requestId = Integer.parseInt(requestParameter) - 1;
      }
      else requestId = 0;

      switch (requestMethod) {
        case Http.METHOD_GET:
          responseBody = requestId == 0 ? DataHandler.get() : DataHandler.get(requestId);
          if(responseBody.equals("")) {
            responseCode = Http.NOT_FOUND;
            responseBody = Http.EMPTY_RESPONSE;
          }
          else {
            responseCode = Http.OK;
          }
          break;
        case Http.METHOD_PUT:
          responseCode = DataHandler.put(requestId) ? Http.OK : Http.FORBIDDEN;
          break;
        case Http.METHOD_POST:
          responseCode = Http.NOT_IMPLEMENTED;
          break;
        case Http.METHOD_DELETE:
          responseCode = DataHandler.delete(requestId) ? Http.OK : Http.NOT_FOUND;
          break;
        case Http.METHOD_OPTIONS:
          responseCode = Http.OK;
          responseBody = Http.ALLOWED_METHODS;
          break;
        default:
          responseCode = Http.METHOD_NOT_ALLOWED;
          responseBody = Http.EMPTY_RESPONSE;
          break;
      }
    }
    System.out.println("ResponseCode: " + responseCode);
    return new String[] {responseCode, responseBody};
  }


  /*
    Send Response to client
   */
  private void sendResponse(String[] response) throws IOException {
    String httpResponse = Http.getHeaders(response[0]);
    httpResponse += response[1];
    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));
    printWriter.print(httpResponse);
    printWriter.flush();
  }



}

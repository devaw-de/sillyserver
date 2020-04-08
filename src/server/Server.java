package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;


public class Server extends Thread {

  public static final int DEFAULT_PORT_NUMBER = 9090;
  private static int port;
  private static ServerSocket serverSocket;
  private Socket clientSocket;
  private DataHandler dataHandler;


  public Server(int p) throws IOException, URISyntaxException {
    this.port = p;
    this.dataHandler = new DataHandler();
    this.startServer();
  }


  private void startServer() throws IOException {
    System.out.println("Starting server at localhost:" + this.port);
    System.out.println("Press CTRL+C to stop");
    this.serverSocket = new ServerSocket(this.port);


    while (true) {
      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          try {
            serverSocket.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
          System.out.println("Quitting. Bye.");
        }
      });
      this.clientSocket = this.waitForRequest();
      String[] parsedRequest = this.parseRequest(this.readRequest());
      this.calculateReply(parsedRequest);
      this.clientSocket.close();
    }

  }


  private Socket waitForRequest() throws IOException {
    Socket socket = this.serverSocket.accept();
    System.out.println("Client connection established");
    return socket;
  }


  private String readRequest() throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
    char[] buffer = new char[1000];
    int requestLength = bufferedReader.read(buffer, 0, 1000);
    String message = new String(buffer, 0, requestLength);
    return message;
  }


  /*
    Ignore all headers except for the first line of the HTTP Request
  */
  private String[] parseRequest(String request) throws IOException {
    String line = request.split("\n")[0];
    String[] requestLine = line.split(" ");
    System.out.println("Request: " + requestLine[0] + " " + requestLine[1]);
    return requestLine;
  }


  private void calculateReply(String[] request) throws IOException {
    switch (request[0]) {
      case Http.METHOD_GET:
        this.sendReply(Http.OK, dataHandler.get(request[1]));
        break;
      case Http.METHOD_POST:
        this.sendReply(Http.NOT_IMPLEMENTED);
        break;
      case Http.METHOD_PUT:
        this.sendReply(Http.NOT_IMPLEMENTED);
      case Http.METHOD_DELETE:
        this.sendReply(Http.NOT_IMPLEMENTED);
        break;
      case Http.METHOD_OPTIONS:
        this.sendReply(Http.OK, Http.ALLOWED_METHODS);
        break;
      default:
        this.sendReply(Http.METHOD_NOT_ALLOWED, Http.HEADER_ALLOW + ' ' + Http.ALLOWED_METHODS);
        break;
    }
  }


  private void sendReply(int status, String reply) throws IOException {
    String httpResponse = Http.getDefaultHeaders(status);
    httpResponse += reply;
    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));
    printWriter.print(httpResponse);
    printWriter.flush();
  }
  private void sendReply(int status) throws IOException {
    this.sendReply(status, "");
  }


}

package server;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class DataHandler {

  // private static ArrayList<String> fileData;
  private static String fileData;
  private static String FILE_PATH = "data.json";


  public DataHandler() throws IOException {
    this.fileData = this.prepData();
  }


  /*
    Read a file
   */
  private String prepData() throws IOException {
    String data = "";
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream = classloader.getResourceAsStream(FILE_PATH);
    InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    BufferedReader reader = new BufferedReader(streamReader);
    for (String line; (line = reader.readLine()) != null;) {
      data += line;
    }
    return data;
  }


  /*
    Determine data to be sent in response to a GET request
    TODO: use actual data
    TODO: use JSON library
   */
  public static String get(String requestParameter) {
    String data = "";
    switch (requestParameter) {
      case "/":
        // data = fileData.get(0); // TODO: need to serialize
        data = fileData;
        break;
      case "/favicon.ico":
        data += Http.NOT_IMPLEMENTED;
        break;
      default:
        data += Http.BAD_REQUEST;
        break;
    }
    return data;
  }

}

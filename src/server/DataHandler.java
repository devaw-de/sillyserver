package server;

import java.io.*;
import java.nio.charset.StandardCharsets;


class DataHandler {

  private static String fileData;
  private static String FILE_PATH = "data.json";


  DataHandler() throws IOException {
    fileData = this.prepData();
  }


  /*
    Read a file
   */
  private String prepData() throws IOException {
    String data = "";
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream = classloader.getResourceAsStream(FILE_PATH);
    if(inputStream != null) {
      InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
      BufferedReader reader = new BufferedReader(streamReader);
      for (String line; (line = reader.readLine()) != null; ) {
        data += line;
      }
      return data;
    }
    else {
      return "";
    }
  }


  /*
    Determine data to be sent in response to a GET request
    TODO: use JSON library
   */
  static String get(String requestParameter) {
    String data = "";
    switch (requestParameter) {
      case "/":
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

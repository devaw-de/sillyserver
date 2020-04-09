package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;


class DataHandler {

  private static String FILE_PATH = "data.json";
  private static String fileData; // raw file contents
  private static Gson gson;
  private static List<TodoItem> list;


  DataHandler() throws IOException {
    DataHandler.gson = new Gson();
    DataHandler.fileData = DataHandler. this.prepData();
    DataHandler.list = gson.fromJson(fileData, new TypeToken<List<TodoItem>>() {}.getType());
  }


  /*
    Read the file
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
   */
  static String get(String requestParameter) {
    String data;
    if (requestParameter.equals("/")) {
      // Reply with the entire list
      data = gson.toJson(list);
    }
    else if(requestParameter.equals("/favicon.ico")) {
      // catch the favicon-request browsers tend to send
      data = "" + Http.NOT_IMPLEMENTED;
    }
    else if(requestParameter.matches("/\\d+")) {
      // Reply with a single item
      String trimmedParameter = requestParameter.replaceFirst("/", "");
      int id = Integer.parseInt(trimmedParameter) - 1;
      if(id > 0 && id < list.size()) {
        data = gson.toJson(list.get(id));
      }
      else {
        data = "" + Http.NOT_FOUND;
      }
    }
    else {
      data = "" + Http.BAD_REQUEST;
    }
    return data;
  }


  /*
    Determine data to be sent in response to a POST request
   */
  static String post(String requestParameter) {
    return "" + Http.METHOD_NOT_ALLOWED;
  }


  /*
    Determine data to be sent in response to a PUT request
   */
  static String put(String requestParameter) {
    return "" + Http.METHOD_NOT_ALLOWED;
  }


  /*
    Determine data to be sent in response to a DELETE request
   */
  static String delete(String requestParameter) {
    return "" + Http.METHOD_NOT_ALLOWED;
  }

}

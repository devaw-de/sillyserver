package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.List;


class DataHandler {

  private static String FILE_PATH = "data.json";
  private static String fileData; // raw file contents
  private static Gson gson;
  private static List<TodoItem> list;


  static void init() throws IOException {
    DataHandler.gson = new Gson();
    DataHandler.fileData = DataHandler.prepData();
    DataHandler.list = gson.fromJson(fileData, new TypeToken<List<TodoItem>>() {}.getType());
  }


  /*
    Read the file
   */
  private static String prepData() throws IOException {
    String data = "";
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream = classloader.getResourceAsStream(FILE_PATH);
    if(inputStream != null) {
      InputStreamReader streamReader = new InputStreamReader(inputStream, Http.CHARSET);
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
  static String get(int id) {
    // Get a single Item
    String data;
    if(id > 0 && id < list.size()) {
      data = gson.toJson(list.get(id));
    }
    else {
      data = "0";
    }
    return data;
  }
  static String get() {
    // Get all items
    return gson.toJson(list);
  }


  /*
    Determine data to be sent in response to a POST request
   */
  static int post(String body) {
    try {
      TodoItem item = parseRequestBody(body);
      list.add(item);
      return list.size();
    }
    catch(IndexOutOfBoundsException e) {
      System.out.println("Failed to create item");
    }
    return 0;
  }


  /*
    Determine whether a PUT request can be fulfilled;
    Toggle the field if possible
   */
  static boolean put(int id) {
    try {
      DataHandler.list.get(id).toggleCompleted();
      return true;
    }
    catch (IndexOutOfBoundsException e) {
      System.out.println("Failed to update item #" + id);
    }
    return false;
  }


  /*
    Determine whether a DELETE request can be fulfilled;
    Delete the item if possible
   */
  static boolean delete(int id) {
    try {
      list.remove(id);
      return true;
    }
    catch(IndexOutOfBoundsException e) {
      System.out.println("Failed to delete item #" + id);
    }
    return false;
  }


  /*
    Parse a request's body.
    Excepts the body to consist of a single json object
   */
  static TodoItem parseRequestBody(String body) {
    TodoItem item = gson.fromJson(body, TodoItem.class);
    return item;
  }


}

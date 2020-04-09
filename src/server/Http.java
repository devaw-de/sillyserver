package server;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Selection of HTTP Status Codes and Methods
 */

public class Http {

  // General Settings
  static final String PROTOCOL = "HTTP/1.1";
  static final String HOSTNAME = "localhost";
  static final Charset CHARSET = StandardCharsets.UTF_8;
  static final String HEADER_ALLOW = "Allow";
  static final String HEADER_CONTENT_TYPE = "text/json";
  static final String CRLF = "\r\n";
  static final String SPACE = " ";
  // Status Codes 200..
  static final int OK = 200;
  // Status Codes 400..
  static final int BAD_REQUEST = 400;
  static final int FORBIDDEN = 403;
  static final int NOT_FOUND = 404;
  static final int METHOD_NOT_ALLOWED = 405;
  static final int I_AM_A_TEAPOT = 418;
  // Status Codes 500..
  static final int INTERNAL_SERVER_ERROR = 500;
  static final int NOT_IMPLEMENTED = 501;
  // Methods
  static final String METHOD_DELETE = "DELETE";
  static final String METHOD_GET = "GET";
  static final String METHOD_OPTIONS = "OPTIONS";
  static final String METHOD_POST = "POST";
  static final String METHOD_PUT = "PUT";
  static final String ALLOWED_METHODS = METHOD_GET + ',' + METHOD_POST + ',' + METHOD_OPTIONS;


  static String getDefaultHeaders(int status) {
    System.out.println(status);
    if(status != OK) {
      System.out.println("no ok");
      return getErrorHeaders(status);
    }
    else {
      String headers = PROTOCOL + SPACE + OK + CRLF;
      headers += "Host:" + SPACE + HOSTNAME + CRLF;
      headers += "Content-type: " + HEADER_CONTENT_TYPE + "; charset=" + CHARSET + CRLF;
      headers += CRLF; // Empty line as defined by the protocol
      return headers;
    }
  }


  private static String getErrorHeaders(int status) {
    String headers = PROTOCOL + SPACE + status + CRLF;
    headers += "Host:" + SPACE + HOSTNAME + CRLF;
    headers += CRLF;
    return headers;
  }

}

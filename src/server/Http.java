package server;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Selection of HTTP Status Codes and Methods
 */

public class Http {

  // General Settings
  public static final String PROTOCOL = "HTTP/1.1";
  public static final String HOSTNAME = "localhost";
  public static final Charset CHARSET = StandardCharsets.UTF_8;
  public static final String HEADER_ALLOW = "Allow";
  public static final String HEADER_CONTENT_TYPE = "text/json";
  public static final String CRLF = "\r\n";
  public static final String SPACE = " ";
  // Status Codes 200..
  public static final int OK = 200;
  // Status Codes 400..
  public static final int BAD_REQUEST = 400;
  public static final int FORBIDDEN = 403;
  public static final int NOT_FOUND = 404;
  public static final int METHOD_NOT_ALLOWED = 405;
  public static final int I_AM_A_TEAPOT = 418;
  // Status Codes 500..
  public static final int INTERNAL_SERVER_ERROR = 500;
  public static final int NOT_IMPLEMENTED = 501;
  // Methods
  public static final String METHOD_DELETE = "DELETE";
  public static final String METHOD_GET = "GET";
  public static final String METHOD_OPTIONS = "OPTIONS";
  public static final String METHOD_POST = "POST";
  public static final String METHOD_PUT = "PUT";
  public static final String ALLOWED_METHODS = METHOD_GET + ',' + METHOD_POST + ',' + METHOD_OPTIONS;


  public static String getDefaultHeaders(int status) {
    if(status == OK) {
      String headers = PROTOCOL + SPACE + OK + CRLF;
      headers += "Host:" + SPACE + HOSTNAME + CRLF;
      headers += "Content-type: " + HEADER_CONTENT_TYPE + "; charset=" + CHARSET + CRLF;
      headers += CRLF + CRLF; // Empty line as defined by the protocol
      return headers;
    }
    else {
      return getErrorHeaders(status);
    }
  }

  private static String getErrorHeaders(int status) {
    return PROTOCOL + SPACE + status + CRLF;
  }

}

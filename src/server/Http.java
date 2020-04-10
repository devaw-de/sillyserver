package server;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Selection of HTTP Status Codes and Methods
 */

class Http {

  // General Settings
  static final String PROTOCOL = "HTTP/1.1";
  static final String HOSTNAME = "localhost";
  static final Charset CHARSET = StandardCharsets.UTF_8;
  // static final String HEADER_ALLOW = "Allow";
  static final String HEADER_CONTENT_TYPE = "application/json";
  static final String CRLF = "\r\n";
  static final String SPACE = " ";
  static final String EMPTY_RESPONSE = "";
  // Status Codes 200..
  static final String OK = "200";
  static final String CREATED = "201";
  // Status Codes 400..
  static final String BAD_REQUEST = "400";
  static final String FORBIDDEN = "403";
  static final String NOT_FOUND = "404";
  static final String METHOD_NOT_ALLOWED = "405";
  static final String I_AM_A_TEAPOT = "418";
  // Status Codes 500..
  static final String INTERNAL_SERVER_ERROR = "500";
  static final String NOT_IMPLEMENTED = "501";
  // Methods
  static final String METHOD_DELETE = "DELETE";
  static final String METHOD_GET = "GET";
  static final String METHOD_OPTIONS = "OPTIONS";
  static final String METHOD_POST = "POST";
  static final String METHOD_PUT = "PUT";
  static final String ALLOWED_METHODS = METHOD_GET + ',' + METHOD_POST + ',' + METHOD_OPTIONS + ',' + METHOD_DELETE + ',' + METHOD_OPTIONS;


  static String getHeaders(String status) {
    String headers = PROTOCOL + SPACE + status + CRLF;
    headers += "Host:" + SPACE + HOSTNAME + CRLF;
    headers += "Content-type: " + HEADER_CONTENT_TYPE + "; charset=" + CHARSET + CRLF;
    headers += "Access-Control-Allow-Origin: *" + CRLF;
    headers += "Access-Control-Allow-Headers: *" + CRLF;
    headers += "Access-Control-Allow-Methods: " + METHOD_GET + "," + METHOD_POST + "," + METHOD_PUT + "," + METHOD_DELETE + "," + METHOD_OPTIONS + CRLF;
    headers += CRLF; // Empty line as defined by the protocol
    return headers;
  }



}

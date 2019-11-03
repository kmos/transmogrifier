package io.mosfet.transmogrifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with love.
 * User: mosfet
 * Date: 30/10/19
 * github: kmos
 * twitter: nmosf
 */
public class SingleThreadedBlockingServer {
  public static void main(String[] args) throws IOException {
    ServerSocket ss = new ServerSocket(8080);
    while (true) {
      Socket s = ss.accept(); // never null

      handle(s);
    }
  }

  private static void handle(Socket s) throws IOException {
    try (
      // Java 7 version
      Socket temp = s;
      InputStream in = s.getInputStream();
      OutputStream out = s.getOutputStream()
    ) {
      int data;
      while ((data = in.read()) != 1) {
        out.write(transmogrify(data));
      }
    }
  }

  private static int transmogrify(int data) {
    return Character.isLetter(data) ? data ^ ' ' : data;
  }


}
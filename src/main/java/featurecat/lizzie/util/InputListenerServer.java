package featurecat.lizzie.util;

import com.sun.tools.javac.util.Pair;
import featurecat.lizzie.Lizzie;
import featurecat.lizzie.rules.Stone;
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class InputListenerServer extends Thread {
  private ServerSocket serverSocket;
  private Socket clientSocket;
  private int port;
  private PrintWriter out;
  private BufferedReader in;

  public InputListenerServer(int port) {
    try {
      this.port = port;
      this.start();
    } catch (Exception e) {

    }
  }

  public void run() {
    try {
      serverSocket = new ServerSocket(port);
      clientSocket = serverSocket.accept();
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      while (true) {
        String line = in.readLine();
        String reply = new String("");
        line.replaceAll("\\s", " ");
        StringTokenizer st = new StringTokenizer(line);
        String str = st.nextToken();
        try {
          int i = Integer.parseInt(str);
          reply = reply + str;
        } catch (Exception e) {
          // There is no number at the start so retokenize
          st = new StringTokenizer(line);
        }

        String command = st.nextToken();

        if (command.equals("play")) {
          String color = st.nextToken();
          try {
            Pair<Integer, Integer> p = parsePosition(st.nextToken());
            int x = p.fst;
            int y = p.snd;
            Lizzie.board.place(x, y, color.equals("black") ? Stone.BLACK : Stone.WHITE);
            reply = "=" + reply;
          } catch (Exception e) {
            reply = "?" + reply;
          }
        }

        out.write(reply + "\n");
      }
    } catch (Exception e) {

    }
  }

  /**
   * @brief Parses a position string, does no checks at all
   * @param str The string to parse
   * @return The parse x,y position
   */
  private Pair<Integer, Integer> parsePosition(String str) {
    Character c = str.toCharArray()[0];
    if (c > 'I') {
      c--;
    }
    Integer x = c - 'A';
    String y_str = str.substring(1);
    Integer y = Integer.parseInt(y_str);

    return new Pair<Integer, Integer>(x, y);
  }
}

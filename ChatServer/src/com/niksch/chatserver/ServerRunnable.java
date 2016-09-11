package com.niksch.chatserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ServerRunnable implements Runnable{
  private Socket socket;
  /**
   * the array listing all hex chars
   */
  private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
  private boolean running = true;

  public ServerRunnable(Socket clientSocket){
    socket = clientSocket;
  }

  @Override
  public void run(){
    // get the in and out streams
    InputStream in;
    OutputStream out;
    try{
      in = socket.getInputStream();
      out = socket.getOutputStream();
    }catch (IOException e){
      e.printStackTrace();
      return;
    }

    // while the server is running
    while (socket.isConnected()){
      try{
        // get package length
        byte[] count = readFromStream(in, 4);
        if (count == null)
          break;
        int c = ByteBuffer.wrap(count).getInt();
        // read the total package
        byte[] data = readFromStream(in, c);
        if (data == null)
          break;
        // dump the data
        dump(data);
        out.write(("Received " + c + " bytes\r\n").getBytes());
      }catch (IOException e){
        e.printStackTrace();
      }
    }
  }

  private void dump(byte[] data){
    char[] hexChars = new char[data.length * 3];
    for (int i = 0; i < data.length; i++){
      int v = data[i] & 0xFF;
      hexChars[i * 3] = hexArray[v >>> 4];
      hexChars[i * 3 + 1] = hexArray[v & 0x0F];
      hexChars[i * 3 + 2] = ' ';
    }
    System.out.println(hexChars);
    System.out.println();
  }

  private byte[] readFromStream(InputStream in, int byteCount) throws IOException{
    boolean end = false;
    byte[] buffer = new byte[byteCount];
    int bytesRead = 0;
    while (!end){
      int n = in.read(buffer, bytesRead, byteCount - bytesRead);
      if (n == -1)
        return null;
      bytesRead += n;
      if (bytesRead == byteCount)
        end = true;
    }
    return buffer;
  }
}

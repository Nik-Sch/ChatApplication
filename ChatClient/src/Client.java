import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Client{
  private String server;
  private int port;
  private InetAddress address;
  Socket socket;

  public Client(String server, int port){
    this.server = server;
    this.port = port;
  }

  public Client(String address){
    String[] args = address.split(":");
    if (args.length != 2)
      throw new RuntimeException("Invalid address string provided");
    try{
      this.server = args[0];
      this.port = Integer.parseInt(args[1]);
    }catch (NumberFormatException e){
      throw new RuntimeException("Invalid address string provided");
    }
  }

  public boolean start(){
    try{
      address = InetAddress.getByName(server);
      socket = new Socket(address, port);
      sendConnectionPackage();
    }catch (IOException e){
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public void stop(){
    try{
      socket.close();
    }catch (IOException e){
      e.printStackTrace();
    }
  }

  private void sendConnectionPackage() throws IOException{
    OutputStream out = socket.getOutputStream();
    byte[] size = ByteBuffer.allocate(4).putInt(22).array();
    byte[] data = "Connection Established".getBytes();
    out.write(concatenate(size, data));
  }

  public byte[] concatenate(byte[] a, byte[] b){
    int aLen = a.length;
    int bLen = b.length;

    @SuppressWarnings("unchecked")
    byte[] c = (byte[]) Array.newInstance(a.getClass().getComponentType(), aLen+bLen);
    System.arraycopy(a, 0, c, 0, aLen);
    System.arraycopy(b, 0, c, aLen, bLen);

    return c;
  }


  public void listen(){
    try{
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      System.out.println(in.readLine());
    }catch (IOException e){
      e.printStackTrace();
    }
  }
}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client{
  Socket socket;
  private String server;
  private int port;
  private InetAddress address;

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

  private void sendConnectionPackage(){
    sendPackage(new ByteWriter("Connection Package".getBytes()));
  }

  private void sendPackage(ByteWriter byteWriter){
    try{
      OutputStream out = socket.getOutputStream();
      out.write(byteWriter.prependSize().getBuffer());
    }catch (IOException e){
      e.printStackTrace();
    }
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

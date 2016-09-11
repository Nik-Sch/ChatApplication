public class ChatClient{
  public static void main(String[] args){
    Client client = new Client("localhost:54242");
    client.start();
    client.listen();
    client.stop();
  }

}

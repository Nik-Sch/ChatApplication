package com.niksch.chatserver;

public class ChatServer{

  public static void main(String[] args){
    // starts the server
    Server server = new Server(54242);
    server.start();
  }


}

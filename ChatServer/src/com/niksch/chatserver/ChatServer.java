package com.niksch.chatserver;

import com.niksch.database.SQLiteJDBC;

public class ChatServer{

  public static void main(String[] args){
    // starts the server
    Server server = new Server(54242);
    server.start();
    SQLiteJDBC sqLiteJDBC = SQLiteJDBC.getInstance();
    sqLiteJDBC.addUser(54242);
  }


}

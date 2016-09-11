package com.niksch.chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server{

  private int port = 54242;
  private ServerSocket socket;
  private boolean isStopped = false;
  private ExecutorService threadPool = Executors.newFixedThreadPool(128);

  public Server(){}

  public Server(int port){
    this.port = port;
  }

  public void start(){
    try{
      // start the server in a new thread
      socket = new ServerSocket(port);
      Thread runningThread = new Thread(this::listen);
      runningThread.start();
    }catch (IOException e){
      e.printStackTrace();
    }
  }

  private void listen(){
    // while the server is not stopped accept new clients
    while (!isStopped()){
      Socket clientSocket;
      try{
        // accept the client
        clientSocket = socket.accept();
      }catch (IOException e){
        if (isStopped()){
          System.out.println("Server stopped");
          break;
        }
        e.printStackTrace();
        return;
      }
      // and add the runnable to the threadPool
      threadPool.execute(new ServerRunnable(clientSocket));
    }
    threadPool.shutdown();
  }

  private synchronized boolean isStopped(){
    return isStopped;
  }

  public synchronized void stop(){
    // set isStopped to true
    isStopped = true;
    try{
      socket.close();
    }catch (IOException e){
      e.printStackTrace();
    }
  }
}

package com.niksch.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class SQLiteJDBC{
  private static int DATABASE_VERSION = 0;

  private static class holder{
    static final SQLiteJDBC INSTANCE = new SQLiteJDBC();
  }

  public static SQLiteJDBC getInstance(){
    return holder.INSTANCE;
  }

  Logger logger;

  Connection connection;

  private SQLiteJDBC(){
    logger = LoggerFactory.getLogger(SQLiteJDBC.class);
    try{
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:chatserver.db");
      logger.info("Database started successfully.");
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("PRAGMA user_version");
      int version = resultSet.getInt("user_version");
      if (version < DATABASE_VERSION)
        onUpgrade(version, DATABASE_VERSION);
      else if (version > DATABASE_VERSION)
        onDowngrade(version, DATABASE_VERSION);
    }catch (ClassNotFoundException e){
      logger.error("SQLite JDBC not found.", e);
    }catch (SQLException e){
      logger.error("While connecting to the db an Exception occurred", e);
    }
  }

  private void onUpgrade(int oldVersion, int newVersion){

  }

  private void onDowngrade(int oldVersion, int newVersion){

  }

  public void addUser(int userName){
    try{
      createUserTable();
      Statement statement = connection.createStatement();
      String sql = "INSERT INTO users (userID,registrationDate)" +
              "VALUES (" + userName + ",DATETIME('now'));";
      statement.executeUpdate(sql);
      logger.info("Added the user '{}' to the db", userName);
    } catch (SQLException e){
      logger.error("While adding user '" + userName + "' an Exception occurred", e);
    }
  }

  private void createUserTable(){
    try{
      Statement statement = connection.createStatement();
      String sql = "CREATE TABLE IF NOT EXISTS users" +
              "(userID INT PRIMARY KEY    NOT NULL, " +
              "registrationDate   STRING NOT NULL)";
      statement.executeUpdate(sql);
      logger.info("Created the users table if necessary");
    } catch (SQLException e){
      logger.error("While creating the userTable an Exception occurred", e);
    }
  }

}

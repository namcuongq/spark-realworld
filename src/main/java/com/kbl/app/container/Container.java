package com.kbl.app.container;
import com.moandjiezana.toml.*;
import java.io.File;
import com.kbl.app.common.constant.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.commons.dbutils.QueryRunner;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.*;

public class Container{
  public static Config Config;
  public static Logger Log;
  public static Connection MysqlClient;

  public class Config{
    public String Host;
    public int Port;
    public String MysqlServer;
    public String MysqlUser;
    public String MysqlPassword;
    public String MysqlDatabase;
    public String TokenKey;
  }


  public Container(String config) {
    this.loadConfig(config);
    this.loadLog();
    this.loadMysql();
  }

  void loadMysql(){
    try {
      String url = String.format("jdbc:mysql://%s/%s",this.Config.MysqlServer,this.Config.MysqlDatabase);
      MysqlClient = DriverManager.getConnection(url,this.Config.MysqlUser,this.Config.MysqlPassword);
      MysqlClient.setAutoCommit(false);
    } catch (Exception e) {
        //throw new Exception(e);
    }
  }

  void loadLog(){
    StatusLogger.getLogger().setLevel(Level.OFF);
    this.Log = LogManager.getRootLogger();
    Configurator.setLevel(this.Log.getName(), Level.DEBUG);
  }

  void loadConfig(String config){
    File file = new File(config);
    this.Config = new Toml().read(file).to(Config.class);

    if (this.Config.Host == null){
      this.Config.Host = Constant.HOST;
    }

    if (this.Config.Port <=0){
      this.Config.Port = Constant.POST;
    }
  }
}

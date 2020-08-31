package com.kbl.app.model;
import com.kbl.app.*;
import com.kbl.app.common.constant.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import org.mindrot.jbcrypt.BCrypt;

public class User{
  public String fullname;
  public String username;
  public String password;
  public static int id;

  public void insert() throws Exception{
    PreparedStatement p = null;
    try {
      String passHash = BCrypt.hashpw(this.password, BCrypt.gensalt(12));
      String sqlInsert = String.format("INSERT INTO %s (username, password, fullname) VALUES (?,?,?)", Constant.MYSQL_TABLE_USER);
      p = App.Container.MysqlClient.prepareStatement(sqlInsert);
      p.setString(1, this.username);
      p.setString(2, passHash);
      p.setString(3, this.fullname);
      p.executeUpdate();
      App.Container.MysqlClient.commit();
    } catch (Exception e) {
      App.Container.MysqlClient.rollback();
      throw e;
    } finally {
      p.close();
    }
  }

  public int getTotal() throws Exception{
    int total = 0;
    try (Statement st = App.Container.MysqlClient.createStatement()){
      String sql = String.format("SELECT count(*) as total FROM %s", Constant.MYSQL_TABLE_USER);
      ResultSet rs = st.executeQuery(sql);
      if(rs.next()) {
       total = rs.getInt("total");
      }

    } catch (Exception e) {
      throw e;
    }
    return total;
  }

  public List<User> getAll() throws Exception{
    List<User> users = new ArrayList<User> ();
    try (Statement st = App.Container.MysqlClient.createStatement()){
      String sql = String.format("SELECT * FROM %s LIMIT 1", Constant.MYSQL_TABLE_USER);
      ResultSet rs = st.executeQuery(sql);
      while(rs.next()) {
        users.add(this.parse(rs));
      }
    } catch (Exception e) {
      throw e;
    }
    return users;
  }

  public User getById(String id) throws Exception{
    User user = new User();
    try{
      user = this.queryOne(String.format("WHERE id = %s ", id));
    } catch (Exception e) {
      throw e;
    }

    return user;

  }

  public User queryOne(String condition) throws Exception{
    User user = new User();
    try (Statement st = App.Container.MysqlClient.createStatement()){
      String sql = String.format("SELECT id FROM %s %s LIMIT 1", Constant.MYSQL_TABLE_USER, condition);
      ResultSet rs = st.executeQuery(sql);
      if(rs.next()) {
       user = this.parse(rs);
      }
    } catch (Exception e) {
      throw e;
    }

    return user;
  }

  public User authen(String username, String password) throws Exception{
    User user = new User();
    try{
      user = this.queryOne(String.format("WHERE username = '%s'", username));
      if (user.id > 0){
        boolean ok = BCrypt.checkpw(password, user.password);
        if (!ok){
          user.id = 0;
        }
      }

    } catch (Exception e) {
      throw e;
    }

    return user;
  }

  User parse(ResultSet rs)  throws Exception{
    User user = new User();
    try{
      user.fullname = rs.getString("fullname");
      user.username = rs.getString("username");
      user.password = "";
      user.id = rs.getInt("id");
    } catch (Exception e) {
      throw e;
    }
    return user;
  }

}

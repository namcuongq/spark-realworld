package com.kbl.app.controller;

import spark.Spark.*;
import com.google.gson.*;
import com.kbl.app.server.*;
import com.kbl.app.*;
import com.kbl.app.model.*;
import java.util.*;
import com.kbl.app.common.utils.*;

public class UserEndpoint
{
    public static String login(spark.Request req, spark.Response res) throws Exception{
        Map <String, Object> response = new HashMap<String, Object>();
        try {
          Gson gson = new GsonBuilder().create();
          User user = gson.fromJson(req.body() , User.class);
          if (user.username == "" || user.password == ""){
              return Server.BadRequest(res,"incorrect username or password");
          }

          user = user.authen(user.username, user.password);
          if (user.id <= 0) {
            return Server.BadRequest(res,"incorrect username or password");
          }

          Map <String, String> clams = new HashMap<String, String>();
          clams.put("id", String.format("%d",user.id));

          Date today = new Date();
          long exp = today.getTime() + (1000 * 60 * 60 * 24);
          response.put("exp", exp);

          String token = Utils.makeJWT(clams, App.Container.Config.TokenKey, new Date(exp));
          response.put("token", token);
          
          exp = today.getTime() + (1000 * 60 * 60 * 24 * 3);
          String refresh_token = Utils.makeJWT(clams, App.Container.Config.TokenKey, new Date(exp));
          response.put("refresh_token", refresh_token);

        }catch (Exception e) {
          throw new Exception("login user: " + e);
        }
        return Server.Data(res, response);
    }

    public static String getInfo(spark.Request req, spark.Response res) throws Exception{
      HashMap<String, Object> response = new HashMap<String, Object>();
      User user;
      try {
        String id = req.params("id");
        user = new User().getById(id);
      }catch (Exception e) {
        throw new Exception("get info user: " + e);
      }

      if (user.id == 0){
        return Server.NotFound(res, "user not found");
      }

      response.put("user",user);
      return Server.Data(res, response);
    }

    public static String getList(spark.Request req, spark.Response res) throws Exception{
      HashMap<String, Object> response = new HashMap<String, Object>();
      List<User> users = null;
      int total = 0;
      try {
        total = new User().getTotal();
        if (total > 0){
          users = new User().getAll();
        }
      }catch (Exception e) {
        throw new Exception("get list user: " + e);
      }
      response.put("total",total);
      response.put("users",users);

      return Server.Data(res, response);
    }

    public static String register(spark.Request req, spark.Response res) throws Exception{
      try {
        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(req.body() , User.class);
        user.insert();
      }catch (Exception e) {
        throw new Exception("register user: " + e);
      }
      return Server.Success(res);
    }
}

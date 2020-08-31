package com.kbl.app.server;
import static spark.Spark.*;
import com.google.gson.*;
import com.kbl.app.*;
import com.kbl.app.controller.*;
import com.kbl.app.server.*;
import com.kbl.app.common.constant.*;

public class Router {

  public static void Start(){
    ipAddress(App.Container.Config.Host);
    port(App.Container.Config.Port);

    App.Container.Log.info("server run on {}:{}",App.Container.Config.Host,App.Container.Config.Port);

    path(Constant.ROUTER_PREFIX, () -> {
      post("/login", (req, res) -> UserEndpoint.login(req, res));
      post("/register", (req, res) -> UserEndpoint.register(req, res));

      path("/user", () -> {
          get("/:id", (req, res) -> UserEndpoint.getInfo(req, res));
          get("", (req, res) -> UserEndpoint.getList(req, res));
      });
    });

    before("/api/v1/*", (request, response) -> {
      if (!Constant.ROUTER_PUBLIC.contains(request.uri())){
    	   String auth = request.headers("Authorization");
       	 System.out.println(request.uri());
      }
    });

    after((request, response) -> {
        response.type("application/json");
    });

    exception(Exception.class, (e, request, response) -> {
      App.Container.Log.error(e.getMessage());
      response.status(500);
      response.body("{\"message\":\"internal server error\",\"code\":500}");
		});
  }

}

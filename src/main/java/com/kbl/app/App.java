package com.kbl.app;

import static spark.Spark.*;
import com.kbl.app.controller.*;
import com.kbl.app.container.*;
import com.kbl.app.server.*;
import com.kbl.app.common.constant.*;

public class App {
    public static Container Container;

    public static void main( String[] args ){
      try {
        Container = new Container(Constant.PATH_CONFIG);
        Router server = new Router();
        server.Start();
      }catch (Exception e) {
        System.out.println("Start server error " + e);
      }
    }
}

package com.kbl.app.server;
import spark.Spark.*;
import com.google.gson.*;

class StandardResponse {
    private String message;
    private int code;
    private Object data;

    public StandardResponse(String message, int code, Object data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }
}

public class Server {

    public static String Success(spark.Response res) {
        res.status(200);
        return new Gson().toJson(new StandardResponse("success", 200, null));
    }

    public static String BadRequest(spark.Response res, String message) {
        res.status(400);
        return new Gson().toJson(new StandardResponse(message, 400, null));
    }

    public static String NotFound(spark.Response res, String message) {
        res.status(404);
        return new Gson().toJson(new StandardResponse(message, 404, null));
    }

    public static String Data(spark.Response res, Object data) {
        res.status(200);
        return new Gson().toJson(new StandardResponse("success", 200, data));
    }
}

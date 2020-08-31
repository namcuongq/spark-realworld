package com.kbl.app.common.constant;
import java.util.*;

public class Constant {
		public final static String HOST = "127.0.0.1";
    public final static int    POST = 5000;
    public final static String PATH_CONFIG = "config.toml";

		public final static String MYSQL_TABLE_USER = "user";

		public final static String ROUTER_PREFIX = "/api/v1";
		public final static List<String> ROUTER_PUBLIC = new ArrayList<>(Arrays.asList(ROUTER_PREFIX + "/login", ROUTER_PREFIX + "/register"));
}

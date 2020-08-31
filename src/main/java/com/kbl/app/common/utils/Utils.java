package com.kbl.app.common.utils;
import java.util.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import io.jsonwebtoken.JwtBuilder;



public class Utils {

  public static String makeJWT(Map<String, String> clams, String priKey, Date exp){
      byte[] key = priKey.getBytes();

      JwtBuilder jwt = Jwts.builder().setExpiration(exp);
      for (Object objectName : clams.keySet()) {
         jwt.claim(String.format("%s",objectName), clams.get(objectName));
         System.out.println(String.format("%s",objectName));
       }
       return jwt.signWith(SignatureAlgorithm.HS256, key).compact();
  }

}

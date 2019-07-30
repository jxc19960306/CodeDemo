package com.jxc.exampleDemo.SSO.JWT;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @param
 * @return
 */
public class JwtUtil {

    public static void main(String[] args) throws SignatureException {

        // 生成token，颁发token
        String salt = "127.0.0.1";
        HashMap<String, String> stringStringHashMap = new HashMap();
        //添加 私人信息
        stringStringHashMap.put("userId","2");
        stringStringHashMap.put("nickName","博哥");
        //编码
        String token = encode("atguigu0328", stringStringHashMap, salt, new Date(), JwtUtil.createExpiration(1000 * 60 * 30));

        System.out.println(token);


        // 验证身份
        Map userMap = decode("atguigu0328", token, salt);

        System.out.println(userMap);

    }


    /***
     * jwt加密
     * @param key
     * @param map 要存储的信息
     * @param salt 盐
     * @param issuedAt 发布时间
     * @param expiration 过期时间
     * @return
     */
    public static String encode(String key,Map map,String salt,  Date issuedAt, Date expiration){

        if(salt!=null){
            key+=salt;
        }
        JwtBuilder jwtBuilder = Jwts.builder()
                .setExpiration(new Date())
                .signWith(SignatureAlgorithm.HS256, key);
        jwtBuilder.addClaims(map);

        String token = jwtBuilder.compact();
        return token;
    }

    /***
     * jwt解密
     * @param key
     * @param token
     * @param salt
     * @return
     * @throws SignatureException
     */
    public static  Map decode(String key,String token,String salt)throws SignatureException {
        if(salt!=null){
            key+=salt;
        }
        Claims map = null;

        map = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();

        System.out.println("map.toString() = " + map.toString());

        return map;
    }

    public static Date createExpiration(long time){
        return new Date(System.currentTimeMillis() + time);
    }
}

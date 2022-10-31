package cn.percent.common.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * jwt相关的
 * @author: zhangpengju
 * Date: 2021/11/19
 * Time: 14:28
 * Description:
 */
@Component
public class JwtUtils {

    /**
     * 过期时间
     */
    private static long tokenExpiration = 24*60*60*1000;
    /**
     * 签名秘钥
     */
    private static String tokenSignKey = "91E+Ap7Kh+dtVXKku+/XBXYTqjzPBf/Rve7JmV2XTDY=";

    /**
     * 自定义的过期时间
     * @param userId
     * @param loginAcct
     * @param tokenExpiration 单位ms
     * @param status token的类型 是accessToken还是refreshToken
     * @return
     */
    public static String createToken(Long userId, String loginAcct,Long tokenExpiration,String status) {
        String token = Jwts.builder()
                .setSubject("system")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("userId", userId)
                .claim("loginAcct", loginAcct)
                .claim("status",status)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    /**
     * 自定义的过期时间
     * @param userId
     * @param loginAcct
     * @param tokenExpiration
     * @return
     */
    public static String createToken(String userId, String loginAcct,String tokenExpiration) {
        String token = Jwts.builder()
                .setSubject("system")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("userId", userId)
                .claim("loginAcct", loginAcct)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    /**
     * 根据参数生成token,默认的过期时间24小时
     * @param userId
     * @param loginAcct
     * @return
     */
    public static String createToken(Long userId, String loginAcct) {
        String token = Jwts.builder()
                .setSubject("system")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("userId", userId)
                .claim("loginAcct", loginAcct)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }
    /**
     * 根据token字符串得到用户id
     * @param token
     * @return
     */
    public static Claims getClaims(String token) {
        if(StrUtil.isEmpty(token)){
            return null;
        }
        Jws<Claims> claimsJws=null;
        try {
            claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        }catch (ExpiredJwtException e) {
            return null;
        }
        Claims claims = claimsJws.getBody();
        return claims;
    }

    /**
     * 根据token字符串得到用户id
     * @param token
     * @return
     */
    public static Long getUserId(String token) {
        if(StrUtil.isEmpty(token)){
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        Integer userId = (Integer)claims.get("userId");
        return userId.longValue();
    }

    /**
     * 根据token字符串得到用户名称
     * @param token
     * @return
     */
    public static String getloginAcct(String token) {
        if(StrUtil.isEmpty(token)){
            return "";
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String)claims.get("loginAcct");
    }

    public static void main(String[] args) {

        String token="eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJSKq4sLknNVdJRSq0oULIyNDO2tLSwNDIw1VEqLU4t8kwBipmYG1kYWhibGBmYGJiYWZoamEEk_RJzU4EmVBVkAbUXlySWlBYDuYnJyanFxSH52al5SrUAnloZV2YAAAA.o2tlogK8nF76mWNGPDgN8zHp-fTy3dp6n__0IqDDkWjgyMIVSHD51XUuqYW1WSreqePodJYFFrM-D6F7N_6kAg";
        System.out.println("getloginAcct(token) = " + JSONObject.toJSONString(getClaims(token)));
    }
}

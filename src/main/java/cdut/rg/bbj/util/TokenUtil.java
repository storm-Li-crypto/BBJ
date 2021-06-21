package cdut.rg.bbj.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

// Token工具类
public class TokenUtil {

    // 有效时长
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;
    // 密钥
    private static final String TOKEN_SECRET = "ben";

    // 加密
    public static String sign(String useraccount){
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("useraccount",useraccount)
                    .withExpiresAt(expiresAt)
                    //使用HMAC256算法加密
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }

    // 解密
    public static boolean verify(String token){
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256((TOKEN_SECRET)))
                    .withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("认证通过");
            System.out.println("useraccount" +jwt.getClaim("useraccount").asString());
            System.out.println("过期时间：" + jwt.getExpiresAt());
            return true;
        } catch (Exception e){
            return false;
        }
    }

    // 返回用户名
    public static String getUserID(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256((TOKEN_SECRET)))
                    .withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            String useraccount = jwt.getClaim("useraccount").asString();
            return useraccount;
        }catch (Exception e){
            return "";
        }

    }
}


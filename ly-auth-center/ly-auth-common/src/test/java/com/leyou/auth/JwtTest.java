package com.leyou.auth;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {

    //先创建tmp/rsa目录
    private static final String pubKeyPath = "E:\\huanjing\\tmp\\rsa\\rsa.pub";

    private static final String priKeyPath = "E:\\huanjing\\tmp\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    //第一步产生公钥 私钥 产生后注释
    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "1234");
    }

    //在所有的测试执行之前，先加载公钥和私钥
    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        /*String token = JwtUtils.generateToken(new UserInfo(666L, "tom"), privateKey, 5);
        System.out.println("token = " + token);*/
        String token = JwtUtils.generateToken(new UserInfo(12L, "tom"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        /*String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MzIsInVzZXJuYW1lIjoiaGVpbWE2MiIsImV4cCI6MTU1NTQwNTgwMH0.p_5U7oEsS2soIEibyPhcjeUWbu6781tAwMblD-kHs_H_wtZqldceYrEUNtphNeUpZED9gaPI28gPbJqf3i3DiF1s4phOw-T7bJG5aNAG2BNm0iuG0FXO40j451OkTCWPNg4HCIXwIXRpq6ez0LJ4AKWBwvF_a0ii6bp7H3kZ6po";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());*/
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MTIsInVzZXJuYW1lIjoidG9tIiwiZXhwIjoxNTc4NzM2NTc3fQ.A-cKI9sLybLunHuFRscII9ZGcrJSp8jq9BcvNELD_04wE-3CacKCVl2WJhJF6cQQPh7G7KS-iZU6RJ-O_5_7ZbjUBpOajeK6B9yXD46Ajyt0hbcHLiCXwlwaL2elxONdKaIPBYdwMKkJ7EHS8ndLDLnsIDAv08nuKbIoaG_Jj4c";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
    @Test
    public void jiemi() throws Exception {
        //解密
        String s="eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MTIsInVzZXJuYW1lIjoidG9tIiwiZXhwIjoxNTc4NzM2NTc3fQ.A-cKI9sLybLunHuFRscII9ZGcrJSp8jq9BcvNELD_04wE-3CacKCVl2WJhJF6cQQPh7G7KS-iZU6RJ-O_5_7ZbjUBpOajeK6B9yXD46Ajyt0hbcHLiCXwlwaL2elxONdKaIPBYdwMKkJ7EHS8ndLDLnsIDAv08nuKbIoaG_Jj4c";
        UserInfo userInfo = JwtUtils.getInfoFromToken(s, publicKey);
        System.out.println(userInfo);

    }

}
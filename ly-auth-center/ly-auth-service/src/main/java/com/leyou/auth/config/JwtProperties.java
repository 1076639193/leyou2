package com.leyou.auth.config;


import com.leyou.auth.utils.RsaUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {

    private String secret;//ly@Login(Auth}*^31)&%
    private String pubKeyPath;//D:/rsa/rsa.pub
    private String priKeyPath;
    private Integer expire;
    private String cookieName;
    private Integer cookieMaxAge;


    private PublicKey publicKey;
    private PrivateKey privateKey;

    @PostConstruct//构造方法
    public void init() throws Exception {

        File file = new File(pubKeyPath);
        File file1 = new File(priKeyPath);
        //没有公钥和私钥
        if(!file.exists()&&!file1.exists()){
            //产生
            RsaUtils.generateKey(pubKeyPath,priKeyPath,secret);
        }
        //
        //获取公钥
        publicKey=RsaUtils.getPublicKey(pubKeyPath);
        //获取私钥
        privateKey=RsaUtils.getPrivateKey(priKeyPath);


    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getPriKeyPath() {
        return priKeyPath;
    }

    public void setPriKeyPath(String priKeyPath) {
        this.priKeyPath = priKeyPath;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public Integer getCookieMaxAge() {
        return cookieMaxAge;
    }

    public void setCookieMaxAge(Integer cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}
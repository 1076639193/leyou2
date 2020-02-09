package com.leyou.auth.controller;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties jwtProperties;


    @PostMapping("accredit")
    public ResponseEntity<Void> accredit(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //字符串
        String token=authService.accredit(username,password);
        if(null==token){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //向浏览器写入cookie
        CookieUtils.setCookie(request,response,jwtProperties.getCookieName(),token,jwtProperties.getCookieMaxAge(),null,true);
        return ResponseEntity.ok().build();

    }

    /**
     * 使用注解直接获取对应的cookie的值
     *
     * 由于每次用户在前端操作都会执行verify方法，所以，我们可以在verify中重新生成token和cookie
     *
     * @return
     */
    @GetMapping("verify")
    public  ResponseEntity<UserInfo> verify(@CookieValue("LY_TOKEN") String s, HttpServletRequest request, HttpServletResponse response) throws Exception {

        UserInfo infoFromToken = JwtUtils.getInfoFromToken(s, jwtProperties.getPublicKey());
        if(infoFromToken!=null){

            //刷新cookie里面字符串
            // 产生token
            String token = JwtUtils.generateToken(infoFromToken, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            //向浏览器写入cookie
            CookieUtils.setCookie(request,response,jwtProperties.getCookieName(),token,jwtProperties.getCookieMaxAge(),null,true);


            return ResponseEntity.ok(infoFromToken);

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}

package com.bigdata.service;

import com.bigdata.interceptors.LoginInterceptor;
import com.bigdata.pojo.Cart;
import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.auth.entity.UserInfo;
import com.leyou.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class CartService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String PEX= "ly:cart:uid:";

    public void addCart(Cart cart) {
        //获取用户消息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //ly:cart:uid:32
        BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(PEX + userInfo.getId());
        //根据商品id取值
        Object o = ops.get(cart.getSkuId().toString());
        if(null!=o){
            //首先从redis取出来，变成对象
            Cart cart1=JsonUtils.nativeRead(o.toString(), new TypeReference<Cart>() {
            });
            //redis数量和页面传过来的cart 进行相加
            cart1.setNum(cart.getNum()+cart.getNum());
            //页面cart和cart数量相加
            ops.put(cart.getSkuId().toString(),JsonUtils.serialize(cart1));

        }else{
            //放入redis
            ops.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));

        }

        //放入购物车
        System.out.println(userInfo);

    }

    public List<Cart> queryCarts() {
        //获取用户消息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //ly:cart:uid:32
        BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(PEX + userInfo.getId());
        //根据ly:cart:uid:32获取值
        List<Object> values = ops.values();

        List<Cart> carts=new ArrayList<>();
        for(Object o: values){
            //把o变成cart
            Cart cart1=JsonUtils.nativeRead(o.toString(), new TypeReference<Cart>() {
            });
            carts.add(cart1);
        }
        return  carts;

    }

    public void increment(Cart cart) {
        //获取用户消息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //ly:cart:uid:32
        BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(PEX + userInfo.getId());
        //根据商品skuid获取值
        Object o = ops.get(cart.getSkuId().toString());//json字符串
        Cart c1=JsonUtils.nativeRead(o.toString(), new TypeReference<Cart>() {
        });
        //修改
        c1.setNum(c1.getNum()+1);

        // //放入redis
        ops.put(cart.getSkuId().toString(), JsonUtils.serialize(c1));

    }

    public void delete(String i) {
        //获取用户消息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //ly:cart:uid:32
        BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(PEX + userInfo.getId());
        ops.delete(i);


    }
}
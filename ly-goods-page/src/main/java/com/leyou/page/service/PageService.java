package com.leyou.page.service;

import com.leyou.item.pojo.*;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*@Service
public class PageService {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecClient specClient;
    public Map<String,Object> loadData(Long spuId) {
        Map<String,Object> map = new HashMap<>();
        //根据spu的id查询spu
        Spu spu=this.goodsClient.querySpuById(spuId);
        map.put("spu",spu);

        //根据spuId查询spuDetail
       SpuDetail spuDetail=this.goodsClient.querySpuDetailBySpuId(spuId);
       map.put("spuDetail",spuDetail);

        //根据spuId查询skus
        List<Sku> skus = this.goodsClient.querySkuBySpuId(spuId);
        map.put("skus",skus);

        // 根据分类查询特有的规格参数，并且处理成map 解构key为规格参数的id，值为规格参数的名字
        List<SpecParam> specailSpecParams=this.specClient.querySpecParam(null,spu.getCid3(),null,false);
        //存放对照表
        Map<Long,String> specParams=new HashMap<>();
        specailSpecParams.forEach(specailSpecParam->{
            specParams.put(specailSpecParam.getId(),specailSpecParam.getName());
        });
        map.put("specParams",specParams);

        //需要根据分类查询规格组，同时查询组内的参数
        List<SpecGroup> specGroups=this.specClient.querySpecGroups(spu.getCid3());
        map.put("specGroups",specGroups);

        return map;
    }
}*/
@Service
public class PageService {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecClient specClient;

    public Map<String,Object> loadData(Long spuId) {

        Map<String,Object> results = new HashMap<>();

        //根据spu的id查询spu
        Spu spu = this.goodsClient.querySpuById(spuId);

        results.put("spu",spu);

        //根据spuId查询spuDetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId);
        results.put("spuDetail",spuDetail);

        //根据spuId查询skus
        List<Sku> skus = this.goodsClient.querySkuBySpuId(spuId);

        results.put("skus",skus);

        // 根据分类查询特有的规格参数，并且处理成map 解构key为规格参数的id，值为规格参数的名字
        List<SpecParam> specailSpecParams = this.specClient.querySpecParam(null, spu.getCid3(), null, false);

        //存放对照表
        Map<Long,String> specParams = new HashMap<>();

        specailSpecParams.forEach(specailSpecParam->{
            specParams.put(specailSpecParam.getId(),specailSpecParam.getName());
        });

        results.put("specParams",specParams);


        //需要根据分类查询规格组，同时查询组内的参数
        List<SpecGroup> specGroups = this.specClient.querySpecGroups(spu.getCid3());

        results.put("specGroups",specGroups);
        return results;
    }
}
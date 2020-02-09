package com.leyou.item.service;

import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;
    @Autowired
    SpuMapper spuMapper;
    @Autowired
    SpuDetailMapper spuDetailsMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Transactional
    public void saveGoods(SpuBo spuBo){
        //添加spu表
        //缺失了4个属性
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(new Date());
        //返回主键添加
        this.spuMapper.insertSelective(spuBo);

        //保存spuDetail,由于缺失spuId，所以要加入spuId
        //spuBo SpuDetail 商品详情
        SpuDetail spuDetail=spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        this.spuDetailMapper.insert(spuDetail);

        //保存sku,把spubo中相同的属性给sku
        List<Sku> skus=spuBo.getSkus();
        saveSkus(spuBo,skus);

        //发送消息
        sendMessage(spuBo.getId(),"insert");
    }

    private void saveSkus(SpuBo spuBo,List<Sku> skus){
        skus.forEach(sku->{
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());

            //保存sku并回显id
            this.skuMapper.insertSelective(sku);

            //同时保存库存 保存库存
            Stock stock=new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });
    }

    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }

    public List<Sku> querySkuBySpuId(Long id) {

        Sku sku=new Sku();
        sku.setSpuId(id);
        List<Sku> skus=this.skuMapper.select(sku);
        skus.forEach(s->{
            s.setStock(this.stockMapper.selectByPrimaryKey(s.getId()).getStock());
        });
        return skus;
    }

    public void updateGoods(SpuBo spuBo) {
        //操作四张表
        //spu，spuDetail可以直接更新,时间改为当前时间
        spuBo.setLastUpdateTime(new Date());
        this.spuMapper.updateByPrimaryKeySelective(spuBo);

        this.spuDetailsMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());
        //sku，
        //先删除当前spu对应的所有的sku，然后重新添加
        //先查，再根据查到的结果删除
        Sku record = new Sku();
        record.setSpuId(spuBo.getId());
        //从数据库中查询到的skus
        List<Sku> list=skuMapper.select(record);
        //根据sku信息，删除对应数据库中的信息
        //删除库存数据
        if(!CollectionUtils.isEmpty(list)){
            List<Long> ids = list.stream().map(Sku::getId).collect(Collectors.toList());
            this.stockMapper.deleteByIdList(ids);
            this.skuMapper.delete(record);
        }

        //循环添加sku
        saveSkus(spuBo, spuBo.getSkus());

        //发送消息
        sendMessage(spuBo.getId(),"update");
    }

    public Spu querySpuById(Long spuId) {
        return this.spuMapper.selectByPrimaryKey(spuId);
    }

    //发送消息的方法
    public void sendMessage(Long id,String type ){
        this.amqpTemplate.convertAndSend("item."+type,id);
        //item.insert routerkey
        //147


        //item.update routerkey
        //148

    }
}

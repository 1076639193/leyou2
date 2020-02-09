package com.leyou.page.client;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("item-service")
public interface GoodsClient {
    @GetMapping("spu/page")
    public PageResult<SpuBo> querySpuByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows);
    //http://api.leyou.com/api/item/goods
    @PostMapping
    public Void saveGoods(@RequestBody SpuBo spuBo);
    //GET http://api.leyou.com/api/item/spu/detail/196
    @GetMapping("spu/detail/{spuId}")
    public SpuDetail querySpuDetailBySpuId(@PathVariable("spuId") Long id);
    //http://api.leyou.com/api/item/sku/list?id=196
    @GetMapping("sku/list")
    public List<Sku> querySkuBySpuId(@RequestParam("id") Long id);
    //http://api.leyou.com/api/item/goods
    @PutMapping
    public Void updateGoods(@RequestBody SpuBo spuBo);

    @GetMapping("spu/{id}")
    public Spu querySpuById(@PathVariable("id") Long spuId );
}

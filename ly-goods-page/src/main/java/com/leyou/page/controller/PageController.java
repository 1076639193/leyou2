package com.leyou.page.controller;

import com.leyou.page.service.FileService;
import com.leyou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {
    @Autowired
    PageService pageService;
    @Autowired
    private FileService fileService;
    //根据spu的id对应展示，详情页面
    @GetMapping("item/{spuId}.html")
    public String toPage(@PathVariable("spuId") Long spuId, Model model){

        model.addAllAttributes(this.pageService.loadData(spuId));
       /* model.addAttribute("spu",spu);
        model.addAttribute("spuDetail",spuDetail);*/
       //判断存在不存在 ，如果不存在则创建一个静态页面
        //会优先进入静态，在近索引库中查询
        if(!this.fileService.exists(spuId)){
            this.fileService.syncCreateHtml(spuId);
        }
        return  "item";
    }
}

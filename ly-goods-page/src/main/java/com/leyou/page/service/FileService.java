package com.leyou.page.service;

import com.leyou.page.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

@Service
public class FileService {
    @Autowired
    private  PageService pageService;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${ly.thymeleaf.destPath}")
    private String destPath;// D:/nginx-1.12.2/html
    /**
     * 判断某个商品的页面是否存在
     * @param id
     * @return
     */
    public Boolean exists(Long id){
        return this.createPath(id).exists();
    }
    public File createPath(Long id){
        File dest=new File(this.destPath);
        if(!dest.exists()){
            //不存在就创建
            dest.mkdirs();
        }
        return new File(dest,id+".html");
    }
    /**
     * 异步创建html页面
     * @param id
     */
    public void syncCreateHtml(Long id){
        ThreadUtils.execute(() -> {
            try {
                createHtml(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 创建html页面
     * @param id
     */
    public void createHtml(Long id){
        // 上下文，准备模型数据
        Context context = new Context();
        // 调用之前写好的加载数据方法
        context.setVariables(this.pageService.loadData(id));
        // 准备文件路径
        File filePath=new File(destPath,id+ ".html");
        // 准备输出流
        try {
            PrintWriter writer=new PrintWriter(filePath,"UTF-8");
            templateEngine.process("item",context,writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void deleteHtml(Long id){
        File file=new File(destPath,id+".html");
        file.deleteOnExit();
    }
}

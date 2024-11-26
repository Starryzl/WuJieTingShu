package com.atguigu.tingshu.album.service;

import cn.hutool.json.JSONObject;
import com.atguigu.tingshu.model.album.BaseAttribute;
import com.atguigu.tingshu.model.album.BaseCategory1;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BaseCategoryService extends IService<BaseCategory1> {

    /**
     * 查询所有分类（1、2、3级分类）
     */
    List<JSONObject> getBaseCategoryList();

    /**
     * 根据一级分类Id获取分类属性（标签）列表
     * @param category1Id
     * @return
     */
    List<BaseAttribute> getAttributesByCategory1Id(Long category1Id);
}

package com.atguigu.tingshu.album.api;

import cn.hutool.json.JSONObject;
import com.atguigu.tingshu.album.service.BaseCategoryService;
import com.atguigu.tingshu.common.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@Tag(name = "分类管理")
@RestController
@RequestMapping(value="/api/album")
@SuppressWarnings({"all"})
public class BaseCategoryApiController {

	@Autowired
	private BaseCategoryService baseCategoryService;

	/**
	 * 查询所有分类（1、2、3级分类）
	 * /api/album/category/getBaseCategoryList
	 * @return
	 */
	@GetMapping("/category/getBaseCategoryList")
	//Map<String,Object>可由JSONObject替换，底层也是map，方法更多
	public Result<List<JSONObject>> getBaseCategoryList(){

		List<JSONObject> categoryList = baseCategoryService.getBaseCategoryList();
		return Result.ok(categoryList);
	}

}


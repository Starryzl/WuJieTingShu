package com.atguigu.tingshu.album.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.atguigu.tingshu.album.mapper.AlbumAttributeValueMapper;
import com.atguigu.tingshu.album.mapper.AlbumInfoMapper;
import com.atguigu.tingshu.album.mapper.AlbumStatMapper;
import com.atguigu.tingshu.album.service.AlbumInfoService;
import com.atguigu.tingshu.common.constant.SystemConstant;
import com.atguigu.tingshu.common.execption.GuiguException;
import com.atguigu.tingshu.model.album.AlbumAttributeValue;
import com.atguigu.tingshu.model.album.AlbumInfo;
import com.atguigu.tingshu.model.album.AlbumStat;
import com.atguigu.tingshu.query.album.AlbumInfoQuery;
import com.atguigu.tingshu.vo.album.AlbumInfoVo;
import com.atguigu.tingshu.vo.album.AlbumListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@SuppressWarnings({"all"})
public class AlbumInfoServiceImpl extends ServiceImpl<AlbumInfoMapper, AlbumInfo> implements AlbumInfoService {

	@Autowired
	private AlbumInfoMapper albumInfoMapper;

	@Autowired
	private AlbumAttributeValueMapper albumAttributeValueMapper;

	@Autowired
	private AlbumStatMapper albumStatMapper;

	/**
	 * 新增专辑
	 * @param albumInfoVo
	 *
	 * 涉及到的表：
	 * album_info
	 * album_attribute_value album_id
	 * album_stat album_id
	 *
	 * @Transactional
	 * 	默认处理RunTimeException
	 * 	SQLException IoException不处理
	 * 	回滚扩大
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveAlbumInfo(AlbumInfoVo albumInfoVo,Long userId) {

		//拷贝数据，创建保存对象
		AlbumInfo albumInfo = BeanUtil.copyProperties(albumInfoVo, AlbumInfo.class);
		//设置用户id
		albumInfo.setUserId(userId);
		//设置免费试听集数
		albumInfo.setTracksForFree(5);
		//设置审核通过--暂直接审核通过--未来后台有管理员审核
		albumInfo.setStatus(SystemConstant.ALBUM_STATUS_PASS);
		//保存album_info
		albumInfoMapper.insert(albumInfo);


		//保存album_attribute_value
		List<AlbumAttributeValue> albumAttributeValueVoList = albumInfo.getAlbumAttributeValueVoList();

		//判断
		if(CollectionUtil.isEmpty(albumAttributeValueVoList)){
			throw new GuiguException(400,"专辑信息不完整，没有属性信息。");
		}
		for (AlbumAttributeValue albumAttributeValue : albumAttributeValueVoList) {
			albumAttributeValue.setAlbumId(albumInfo.getId());
			albumAttributeValueMapper.insert(albumAttributeValue);
		}

		//保存album_stat
		this.saveAlbumStat(albumInfo.getId(),SystemConstant.ALBUM_STAT_PLAY,0);
		this.saveAlbumStat(albumInfo.getId(),SystemConstant.ALBUM_STAT_SUBSCRIBE,0);
		this.saveAlbumStat(albumInfo.getId(),SystemConstant.ALBUM_STAT_BUY,0);
		this.saveAlbumStat(albumInfo.getId(),SystemConstant.ALBUM_STAT_COMMENT,0);

	}

	/**
	 * 查看当前用户专辑分页列表
	 * @param albumListVoPage
	 * @param albumInfoQuery
	 * @return
	 */
	@Override
	public Page<AlbumListVo> findUserAlbumPage(Page<AlbumListVo> albumListVoPage, AlbumInfoQuery albumInfoQuery) {




		return albumInfoMapper.selectUserAlbumPage(albumListVoPage,albumInfoQuery);
	}

	/**
	 * 保存专辑统计信息
	 * @param albumId
	 * @param statType
	 * @param statNum
	 */
	private void saveAlbumStat(Long albumId, String statType, int statNum) {

		//封装统计对象
		AlbumStat albumStat = new AlbumStat();
		albumStat.setAlbumId(albumId);
		albumStat.setStatType(statType);
		albumStat.setStatNum(statNum);

		albumStatMapper.insert(albumStat);
	}
}

package com.wjt.dao;

import com.wjt.model.GuanghuaEntity;
import org.springframework.stereotype.Repository;

/**
 * 日月光华bbs;
 */
@Repository
public interface GuanghuaMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GuanghuaEntity record);

    int insertSelective(GuanghuaEntity record);

    GuanghuaEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GuanghuaEntity record);

    int updateByPrimaryKey(GuanghuaEntity record);
}
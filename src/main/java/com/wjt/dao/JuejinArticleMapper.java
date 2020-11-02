package com.wjt.dao;

import com.wjt.model.JuejinArticleEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface JuejinArticleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(JuejinArticleEntity record);

    int insertSelective(JuejinArticleEntity record);

    JuejinArticleEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(JuejinArticleEntity record);

    int updateByPrimaryKey(JuejinArticleEntity record);
}
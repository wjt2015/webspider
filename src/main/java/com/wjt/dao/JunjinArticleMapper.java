package com.wjt.dao;

import com.wjt.model.JunjinArticleEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface JunjinArticleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(JunjinArticleEntity record);

    int insertSelective(JunjinArticleEntity record);

    JunjinArticleEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(JunjinArticleEntity record);

    int updateByPrimaryKey(JunjinArticleEntity record);
}
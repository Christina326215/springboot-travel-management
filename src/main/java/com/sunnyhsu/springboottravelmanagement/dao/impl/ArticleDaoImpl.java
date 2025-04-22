package com.sunnyhsu.springboottravelmanagement.dao.impl;

import com.sunnyhsu.springboottravelmanagement.dao.ArticleDao;
import com.sunnyhsu.springboottravelmanagement.dto.ArticleQueryParams;
import com.sunnyhsu.springboottravelmanagement.dto.ArticleRequest;
import com.sunnyhsu.springboottravelmanagement.model.Article;
import com.sunnyhsu.springboottravelmanagement.rowmapper.ArticleRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ArticleDaoImpl implements ArticleDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer countArticle(ArticleQueryParams articleQueryParams) {
        String sql = "SELECT count(*) FROM destinations WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

        // 查詢條件
        sql = addFilteringSql(sql, map, articleQueryParams);

        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);

        return total;
    }

    @Override
    public List<Article> getAllArticles(ArticleQueryParams articleQueryParams) {
        String sql = "SELECT id, name, description, image_url, created_date, last_modified_date " +
                "FROM destinations " +
                "WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

        // 查詢條件
        sql = addFilteringSql(sql, map, articleQueryParams);

        // 排序
        sql = sql + " ORDER BY " + articleQueryParams.getOrderBy() + " " + articleQueryParams.getSort();

        // 分頁
        sql = sql + " LIMIT :limit OFFSET :offset";
        map.put("limit", articleQueryParams.getLimit());
        map.put("offset", articleQueryParams.getOffset());

        List<Article> articleList = namedParameterJdbcTemplate.query(sql, map, new ArticleRowMapper());

        return articleList;
    }

    @Override
    public Article getArticleById(Integer articleId) {
        String sql = "SELECT id, name, description, image_url, created_date, last_modified_date " +
                "FROM destinations " +
                "WHERE id = :articleId";

        Map<String, Object> map = new HashMap<>();
        map.put("articleId", articleId);

        List<Article> articleList = namedParameterJdbcTemplate.query(sql, map, new ArticleRowMapper());

        if (articleList.size() > 0){
            return articleList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Integer createArticle(ArticleRequest articleRequest) {
        String sql = "INSERT INTO destinations(name, description, image_url, created_date, last_modified_date) " +
                "VALUES (:title, :content, :imageUrl, :createdDate, :lastModifiedDate)";

        Map<String, Object> map = new HashMap<>();
        map.put("title", articleRequest.getTitle());
        map.put("content", articleRequest.getContent());
        map.put("imageUrl", articleRequest.getImageUrl());
        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int articleId = keyHolder.getKey().intValue();

        return articleId;
    }

    @Override
    public void updateArticle(Integer articleId, ArticleRequest articleRequest) {
        String sql = "UPDATE destinations SET id = :articleId, name = :title, description = :content, " +
                "image_url = :imageUrl, created_date = :createdDate, last_modified_date = :lastModifiedDate " +
                "WHERE id = :articleId";

        Map<String, Object> map = new HashMap<>();
        map.put("articleId", articleId);
        map.put("title", articleRequest.getTitle());
        map.put("content", articleRequest.getContent());
        map.put("imageUrl", articleRequest.getImageUrl());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void deleteArticleById(Integer articleId) {
        String sql = "DELETE FROM destinations WHERE id = :articleId";

        Map<String, Object> map = new HashMap<>();
        map.put("articleId", articleId);

        namedParameterJdbcTemplate.update(sql, map);
    }

    private String addFilteringSql(String sql, Map<String, Object> map, ArticleQueryParams articleQueryParams){
        if (articleQueryParams.getSearch() != null){
            sql = sql + " AND (name LIKE :search OR description LIKE :search)";
            map.put("search", "%" + articleQueryParams.getSearch() + "%");
        }

        return sql;

    }
}


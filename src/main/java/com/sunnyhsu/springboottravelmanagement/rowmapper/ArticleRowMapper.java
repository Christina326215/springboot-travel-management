package com.sunnyhsu.springboottravelmanagement.rowmapper;

import com.sunnyhsu.springboottravelmanagement.model.Article;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticleRowMapper implements RowMapper<Article> {

    @Override
    public Article mapRow(ResultSet resultSet, int i) throws SQLException {

        Article article = new Article();

        article.setArticleId(resultSet.getInt("id"));
        article.setTitle(resultSet.getString("name"));
        article.setContent(resultSet.getString("description"));
        article.setImageUrl(resultSet.getString("image_url"));
        article.setCreatedDate(resultSet.getTimestamp("created_date"));
        article.setLastModifiedDate(resultSet.getTimestamp("last_modified_date"));

        return article;
    }
}


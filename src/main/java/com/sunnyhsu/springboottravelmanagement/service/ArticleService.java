package com.sunnyhsu.springboottravelmanagement.service;

import com.sunnyhsu.springboottravelmanagement.dto.ArticleQueryParams;
import com.sunnyhsu.springboottravelmanagement.dto.ArticleRequest;
import com.sunnyhsu.springboottravelmanagement.model.Article;

import java.util.List;

public interface ArticleService {

    Integer countArticle(ArticleQueryParams articleQueryParams);

    List<Article> getAllArticles(ArticleQueryParams articleQueryParams);

    Article getArticleById(Integer articleId);

    Integer createArticle(ArticleRequest articleRequest);

    void updateArticle(Integer articleId, ArticleRequest articleRequest);

    void deleteArticleById(Integer articleId);
}

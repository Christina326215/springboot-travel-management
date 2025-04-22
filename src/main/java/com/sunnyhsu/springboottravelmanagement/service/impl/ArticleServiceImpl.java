package com.sunnyhsu.springboottravelmanagement.service.impl;

import com.sunnyhsu.springboottravelmanagement.dao.ArticleDao;
import com.sunnyhsu.springboottravelmanagement.dto.ArticleQueryParams;
import com.sunnyhsu.springboottravelmanagement.dto.ArticleRequest;
import com.sunnyhsu.springboottravelmanagement.model.Article;
import com.sunnyhsu.springboottravelmanagement.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Override
    public Integer countArticle(ArticleQueryParams articleQueryParams) {
        return articleDao.countArticle(articleQueryParams);
    }

    @Override
    public List<Article> getAllArticles(ArticleQueryParams articleQueryParams) {
        return articleDao.getAllArticles(articleQueryParams);
    }

    @Override
    public Article getArticleById(Integer articleId) {
        return articleDao.getArticleById(articleId);
    }

    @Override
    public Integer createArticle(ArticleRequest articleRequest) {
        return articleDao.createArticle(articleRequest);
    }

    @Override
    public void updateArticle(Integer articleId, ArticleRequest articleRequest) {
        articleDao.updateArticle(articleId, articleRequest);
    }

    @Override
    public void deleteArticleById(Integer articleId) {
        articleDao.deleteArticleById(articleId);
    }
}


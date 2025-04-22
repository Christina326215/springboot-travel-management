package com.sunnyhsu.springboottravelmanagement.controller;

import com.sunnyhsu.springboottravelmanagement.dto.ArticleQueryParams;
import com.sunnyhsu.springboottravelmanagement.dto.ArticleRequest;
import com.sunnyhsu.springboottravelmanagement.model.Article;
import com.sunnyhsu.springboottravelmanagement.service.ArticleService;
import com.sunnyhsu.springboottravelmanagement.service.FileStorageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import com.sunnyhsu.springboottravelmanagement.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
@Controller
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/management/articles")
    public String showArticleManagement(Model model) {
        return "articles";
    }

    @GetMapping("/admin/articles")
    public ResponseEntity<Page<Article>> getAllArticles(
            // 查詢條件 Filtering
            @RequestParam(required = false) String search,

            // 排序 Sorting
            @RequestParam(defaultValue = "created_date") String orderBy,
            @RequestParam(defaultValue = "desc") String sort,

            // 分頁 Pagination
            @RequestParam(defaultValue = "6") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ){
        ArticleQueryParams articleQueryParams = new ArticleQueryParams();
        articleQueryParams.setSearch(search);
        articleQueryParams.setOrderBy(orderBy);
        articleQueryParams.setSort(sort);
        articleQueryParams.setLimit(limit);
        articleQueryParams.setOffset(offset);

        // 取得 article list
        List<Article> articleList = articleService.getAllArticles(articleQueryParams);
        System.out.println("查詢結果：" + articleList.get(0)); // 檢查查詢結果

        // 取得 article 總數
        Integer total = articleService.countArticle(articleQueryParams);

        // 分頁
        Page<Article> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResults(articleList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @GetMapping("/admin/articles/{articleId}")
    public ResponseEntity<Article> getArticleById(@PathVariable Integer articleId){
        Article article = articleService.getArticleById(articleId);

        if (article != null){
            return ResponseEntity.status(HttpStatus.OK).body(article);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/admin/articles")
    public ResponseEntity<Article> createArticle(@RequestBody @Valid ArticleRequest articleRequest){
        Integer articleId = articleService.createArticle(articleRequest);

        Article article = articleService.getArticleById(articleId);

        return ResponseEntity.status(HttpStatus.CREATED).body(article);
    }


    @PutMapping("/admin/articles/{articleId}")
    public ResponseEntity<Article> updateArticle(@PathVariable Integer articleId,
                                                 @RequestBody @Valid ArticleRequest articleRequest){
        // 檢查 article 是否存在
        Article article = articleService.getArticleById(articleId);
        if (article == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

//        // 處理圖片上傳
//        if (imageFile != null && !imageFile.isEmpty()) {
//            String imageUrl = fileStorageService.storeFile(imageFile); // 自定義圖片存儲邏輯
//            articleRequest.setImageUrl(imageUrl);
//        }

        // 修改文章
        articleService.updateArticle(articleId, articleRequest);

        Article updateArticle = articleService.getArticleById(articleId);

        return ResponseEntity.status(HttpStatus.OK).body(updateArticle);
    }

    @DeleteMapping("/admin/articles/{articleId}")
    public ResponseEntity<?> deleteArticleById(@PathVariable Integer articleId){
        articleService.deleteArticleById(articleId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}


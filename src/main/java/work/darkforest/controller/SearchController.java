package work.darkforest.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.darkforest.base.BusinessException;
import work.darkforest.base.WebResponse;
import work.darkforest.service.ElasticsearchService;
import work.darkforest.service.RedisService;

@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    private RedisService redisService;
    @Autowired
    private ElasticsearchService elasticsearchService;

    @GetMapping("/{keyWord}/{page}/{size}")
    @ApiOperation("多字段同时查询")
    public WebResponse searchAll(@PathVariable String keyWord,
                                 @PathVariable Integer page,
                                 @PathVariable Integer size){
        redisService.incrTag(keyWord);
        try {
            return WebResponse.Builder.success(elasticsearchService.queryAll(keyWord, page, size));
        } catch (BusinessException e) {
            return WebResponse.Builder.fail(e.getMessage());
        }
    }


    @GetMapping("name/{name}/{page}/{size}")
    @ApiOperation("根据诗名进行搜索")
    public WebResponse searchByName(@PathVariable String name,
                                    @PathVariable Integer page,
                                    @PathVariable Integer size){
        redisService.incrTag(name);
        try {
            return WebResponse.Builder.success(elasticsearchService.queryByName(name, page, size));
        } catch (BusinessException e) {
            return WebResponse.Builder.fail(e.getMessage());
        }
    }

    @GetMapping("author/{author}/{page}/{size}")
    @ApiOperation("根据作者进行搜索")
    public WebResponse searchByAuthor(@PathVariable String author,
                                    @PathVariable Integer page,
                                    @PathVariable Integer size){
        redisService.incrTag(author);
        try {
            return WebResponse.Builder.success(elasticsearchService.queryByAuthor(author, page, size));
        } catch (BusinessException e) {
            return WebResponse.Builder.fail(e.getMessage());
        }
    }

    @GetMapping("content/{content}/{page}/{size}")
    @ApiOperation("根据诗的内容进行搜索")
    public WebResponse searchByContent(@PathVariable String content,
                                       @PathVariable Integer page,
                                       @PathVariable Integer size){
        redisService.incrTag(content);
        try {
            return WebResponse.Builder.success(elasticsearchService.queryByContent(content, page, size));
        } catch (BusinessException e) {
            return WebResponse.Builder.fail(e.getMessage());
        }
    }

    @GetMapping("type/{type}/{page}/{size}")
    @ApiOperation("根据诗的类型进行搜索")
    public WebResponse searchByType(@PathVariable String type,
                                    @PathVariable Integer page,
                                    @PathVariable Integer size){
        redisService.incrTag(type);
        try {
            return WebResponse.Builder.success(elasticsearchService.queryByType(type, page, size));
        } catch (BusinessException e) {
            return WebResponse.Builder.fail(e.getMessage());
        }
    }

    @GetMapping("hot")
    @ApiOperation("查询热榜的中的数据")
    public WebResponse searchHot(){
        return WebResponse.Builder.success(redisService.list());
    }
}

package work.darkforest.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.darkforest.base.WebResponse;
import work.darkforest.service.RedisService;

@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    private RedisService redisService;

    @GetMapping("name/{name}")
    @ApiOperation("根据诗名进行搜索")
    public WebResponse searchByName(@PathVariable String name){
        redisService.incrTag(name);
        return WebResponse.Builder.success(null);
    }

    @GetMapping("author/{author}")
    @ApiOperation("根据作者名进行搜索")
    public WebResponse searchByAuthor(@PathVariable String author){
        redisService.incrTag(author);
        return WebResponse.Builder.success(null);
    }

    @GetMapping("hot")
    @ApiOperation("查询热榜的中的数据")
    public WebResponse searchHot(){
        return WebResponse.Builder.success(redisService.list());
    }
}

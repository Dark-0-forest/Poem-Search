package work.darkforest.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import work.darkforest.base.BusinessException;
import work.darkforest.base.WebResponse;
import work.darkforest.entity.po.Poem;
import work.darkforest.service.PoemService;

import java.util.UUID;

@RestController
@RequestMapping("poem")
public class PoemController {
    @Autowired
    private PoemService poemService;

    @PostMapping("list")
    @ApiOperation("查询所有")
    public WebResponse list(){
        return WebResponse.Builder.success(poemService.list());
    }

    @GetMapping("get/{id}")
    @ApiOperation("查询一个")
    public WebResponse getById(@PathVariable String id){
        try {
            return WebResponse.Builder.success(poemService.findById(id));
        } catch (BusinessException e) {
            return WebResponse.Builder.fail(e.getMessage());
        }
    }

    @GetMapping("pageCount/{size}")
    @ApiOperation("查询总页数")
    public WebResponse pageCount(@PathVariable int size){
        try {
            return WebResponse.Builder.success(poemService.pageCount(size));
        } catch (BusinessException e) {
            return WebResponse.Builder.fail(e.getMessage());
        }
    }

    @PostMapping("/{page}/{size}")
    @ApiOperation("分页查询")
    public WebResponse page(@PathVariable int page,
                            @PathVariable int size){
        try {
            return WebResponse.Builder.success(poemService.listByPage(page, size));
        } catch (BusinessException e) {
            return WebResponse.Builder.fail(e.getMessage());
        }
    }

    @PostMapping("add")
    @ApiOperation("添加")
    public WebResponse add(Poem poem){
        try {
            String id = UUID.randomUUID().toString();
            poem.setId(id);
            poemService.add(poem);
            return WebResponse.Builder.success("添加成功", id);
        } catch (BusinessException e) {
            return WebResponse.Builder.fail(e.getMessage());
        }
    }

    @PostMapping("update")
    @ApiOperation("修改")
    public WebResponse update(Poem poem){
        try {
            poemService.update(poem);
            return WebResponse.Builder.success("修改成功");
        } catch (BusinessException e) {
            return WebResponse.Builder.fail(e.getMessage());
        }
    }

    @PostMapping("del/{id}")
    @ApiOperation("逻辑删除")
    public WebResponse del(@PathVariable String id){
        try {
            poemService.delById(id);
            return WebResponse.Builder.success("删除成功");
        } catch (BusinessException e) {
            return WebResponse.Builder.fail(e.getMessage());
        }
    }

    @PostMapping("recovery/{id}")
    @ApiOperation("逻辑删除")
    public WebResponse recovery(@PathVariable String id){
        try {
            poemService.recovery(id);
            return WebResponse.Builder.success("恢复成功");
        } catch (BusinessException e) {
            return WebResponse.Builder.fail(e.getMessage());
        }
    }

}

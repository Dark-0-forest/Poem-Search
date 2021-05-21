package work.darkforest.service;

import work.darkforest.base.BusinessException;
import work.darkforest.entity.po.Poem;

import java.util.List;

public interface ElasticsearchService {
    // 根据诗名查询
    List<Poem> queryByName(String name, Integer page, Integer size) throws BusinessException;

    // 根据内容查询
    List<Poem> queryByContent(String content, Integer page, Integer size) throws BusinessException;

    // 根据作者姓名查询
    List<Poem> queryByAuthor(String author, Integer page, Integer size) throws BusinessException;

    // 根据诗歌类别查询
    List<Poem> queryByType(String type, Integer page, Integer size) throws BusinessException;

    // 多字段查询
    List<Poem> queryAll(String content, Integer page, Integer size) throws BusinessException;
}

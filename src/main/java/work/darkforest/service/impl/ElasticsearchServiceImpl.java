package work.darkforest.service.impl;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.darkforest.base.BusinessException;
import work.darkforest.entity.po.Poem;
import work.darkforest.service.ElasticsearchService;
import org.elasticsearch.action.search.SearchRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {
    @Autowired
    RestHighLevelClient elasticsearchClient;

    @Override
    public List<Poem> queryByName(String name, Integer page, Integer size) throws BusinessException {
        return conditionSearch(name, page, size, true, "poemName");
    }

    @Override
    public List<Poem> queryByContent(String content, Integer page, Integer size) throws BusinessException {
        return conditionSearch(content, page, size, true, "poemContent");
    }

    @Override
    public List<Poem> queryByAuthor(String author, Integer page, Integer size) throws BusinessException {
        return conditionSearch(author, page, size, true, "author");
    }

    @Override
    public List<Poem> queryByType(String type, Integer page, Integer size) throws BusinessException {
        return conditionSearch(type, page, size, true, "poemType");
    }

    @Override
    public List<Poem> queryAll(String keyWord, Integer page, Integer size) throws BusinessException {
        return conditionSearch(keyWord, page, size, false, "poemName", "poemContent", "author", "poemType");
    }

    /**
     * 封装了对ES进行条件查询的函数
     * @param searchContent  要查询的内容
     * @param page  页码
     * @param size  每页的大小
     * @param flag  表示是否为单条件查询，true为单条件fieldNames只有一个值，false为多条件查询fieldNames为数组
     * @param fieldNames 要查询的字段名
     * @return  查询的结果
     */
    private List<Poem> conditionSearch(String searchContent, Integer page, Integer size, boolean flag, String... fieldNames) throws BusinessException {
        try {
            // 指定搜索条件
            SearchSourceBuilder builder = new SearchSourceBuilder();
            // 判断flag，如果为true表示单条件查询，使用matchQuery.fieldNames只有一个元素，直接取出0即可
            if (flag) builder.query(QueryBuilders.matchQuery(fieldNames[0], searchContent));
            // 如果flag为false表示多条件查询，使用multiMatchQuery
            else builder.query(QueryBuilders.multiMatchQuery(searchContent, fieldNames));
            // 查询文档条数
            CountRequest request = new CountRequest(new String[]{"poems"}, builder);
            long count = elasticsearchClient.count(request, RequestOptions.DEFAULT).getCount();
            // 通过页数获取起始位置
            page = (page-1) * size;
            if (page < 0 || page >= count) throw new BusinessException("页数有误");
            builder
                    .from(page) // 起始位置
                    .size(size) // 页面大小
                    .highlighter(new HighlightBuilder().field("*")  // 高亮查询
                            .requireFieldMatch(false)
                            .preTags("<span style='color:red'>")
                            .postTags("</span>"));
            // 查询请求
            SearchRequest searchRequest = new SearchRequest("poems").types("poem").source(builder);
            SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
            // 解析获得数据
            return parseResponse(response);

        } catch (BusinessException e){
            throw new BusinessException(e.getMessage());
        } catch (Exception e) {
            throw new BusinessException("查询失败");
        }
    }

    /**
     *
     * @param response  es返回的结果
     * @return  从结果中重构的Poem对象的list
     */
    private List<Poem> parseResponse(SearchResponse response){
        List<Poem> poems = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            Poem poem = new Poem();
            // 获取各个字段的值
            Map<String, Object> map = hit.getSourceAsMap();
            poem.setId(map.get("id").toString())
                    .setPoemName(map.get("poemName").toString())
                    .setAuthor(map.get("author").toString())
                    .setPoemType(map.get("poemType").toString())
                    .setPoemSource(map.get("poemSource").toString())
                    .setPoemContent(map.get("poemContent").toString())
                    .setAuthorDes(map.get("authorDes").toString());
            // 高亮处理
            Map<String, HighlightField> highLightMap = hit.getHighlightFields();
            if (highLightMap.containsKey("poemName"))poem.setPoemName(highLightMap.get("poemName").fragments()[0].toString());
            if (highLightMap.containsKey("author"))poem.setAuthor(highLightMap.get("author").fragments()[0].toString());
            if (highLightMap.containsKey("poemType"))poem.setPoemType(highLightMap.get("poemType").fragments()[0].toString());
            if (highLightMap.containsKey("poemContent"))poem.setPoemContent(highLightMap.get("poemContent").fragments()[0].toString());
            if (highLightMap.containsKey("authorDes"))poem.setAuthorDes(highLightMap.get("authorDes").fragments()[0].toString());
            poems.add(poem);
        }
        return poems;
    }
}

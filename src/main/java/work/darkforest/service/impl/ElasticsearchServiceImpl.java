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
     * ????????????ES???????????????????????????
     * @param searchContent  ??????????????????
     * @param page  ??????
     * @param size  ???????????????
     * @param flag  ?????????????????????????????????true????????????fieldNames??????????????????false??????????????????fieldNames?????????
     * @param fieldNames ?????????????????????
     * @return  ???????????????
     */
    private List<Poem> conditionSearch(String searchContent, Integer page, Integer size, boolean flag, String... fieldNames) throws BusinessException {
        try {
            // ??????????????????
            SearchSourceBuilder builder = new SearchSourceBuilder();
            // ??????flag????????????true??????????????????????????????matchQuery.fieldNames?????????????????????????????????0??????
            if (flag) builder.query(QueryBuilders.matchQuery(fieldNames[0], searchContent));
            // ??????flag???false??????????????????????????????multiMatchQuery
            else builder.query(QueryBuilders.multiMatchQuery(searchContent, fieldNames));
            // ??????????????????
            CountRequest request = new CountRequest(new String[]{"poems"}, builder);
            long count = elasticsearchClient.count(request, RequestOptions.DEFAULT).getCount();
            // ??????????????????????????????
            page = (page-1) * size;
            if (page < 0 || page >= count) throw new BusinessException("????????????");
            builder
                    .from(page) // ????????????
                    .size(size) // ????????????
                    .highlighter(new HighlightBuilder().field("*")  // ????????????
                            .requireFieldMatch(false)
                            .preTags("<span style='color:red'>")
                            .postTags("</span>"));
            // ????????????
            SearchRequest searchRequest = new SearchRequest("poems").types("poem").source(builder);
            SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
            // ??????????????????
            return parseResponse(response);

        } catch (BusinessException e){
            throw new BusinessException(e.getMessage());
        } catch (Exception e) {
            throw new BusinessException("????????????");
        }
    }

    /**
     *
     * @param response  es???????????????
     * @return  ?????????????????????Poem?????????list
     */
    private List<Poem> parseResponse(SearchResponse response){
        List<Poem> poems = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            Poem poem = new Poem();
            // ????????????????????????
            Map<String, Object> map = hit.getSourceAsMap();
            poem.setId(map.get("id").toString())
                    .setPoemName(map.get("poemName").toString())
                    .setAuthor(map.get("author").toString())
                    .setPoemType(map.get("poemType").toString())
                    .setPoemSource(map.get("poemSource").toString())
                    .setPoemContent(map.get("poemContent").toString())
                    .setAuthorDes(map.get("authorDes").toString());
            // ????????????
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

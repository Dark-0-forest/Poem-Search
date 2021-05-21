package work.darkforest;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import work.darkforest.elaticsearch.dao.PoemRepository;
import work.darkforest.entity.po.Poem;
import work.darkforest.mapper.PoemMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class EsTest {
    @Autowired
    private RestHighLevelClient elasticsearchClient;
    @Autowired
    private PoemRepository poemRepository;
    @Autowired
    private PoemMapper poemMapper;

    @Test
    void test(){
        poemRepository.save(poemMapper.findById("0c800acb-0305-4344-98d0-38dc6f8826f5"));
    }

    @Test
    void del() throws IOException {
        poemRepository.deleteAll();
    }

    @Test
    void createAll(){
        poemRepository.deleteAll();
        List<Poem> list = poemMapper.list();
        poemRepository.saveAll(list);
    }

    @Test
    void size(){
        System.out.println(poemRepository.count());
    }

    @Test
    void count() throws IOException {
        // 指定条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.multiMatchQuery("李白","author"));
        CountRequest request = new CountRequest(new String[]{"poems"}, builder);
        long count = elasticsearchClient.count(request, RequestOptions.DEFAULT).getCount();

        builder.from(110).size(20);
        SearchRequest searchRequest = new SearchRequest("poems");
        searchRequest.types("poem").source(builder);
        SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);

        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

    @Test
    void search() throws IOException {
            int page = 1;
            int size = 20;

            // 指定搜索条件
            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(QueryBuilders.matchQuery("author", "李白"));
            // 查询文档条数
            CountRequest request = new CountRequest(new String[]{"poems"}, builder);
            long count = elasticsearchClient.count(request, RequestOptions.DEFAULT).getCount();
            // 通过页数获取起始位置
            page = (page-1) * size;
            if (page < 0 || page >= count) System.out.println("页码错误");

            builder
                    .from(page) // 起始位置
                    .size(size) // 页面大小
                    .highlighter(new HighlightBuilder() // 高亮查询
                            .field("*")
                            .requireFieldMatch(false)
                            .preTags("<span style='color:red'>")
                            .postTags("</span>"));
            // 查询请求
            SearchRequest searchRequest = new SearchRequest("poems").types("poem").source(builder);
            SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);

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
            poems.forEach(poem-> System.out.println(poem));
    }

}

package work.darkforest.elaticsearch.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import work.darkforest.entity.po.Poem;

// Poem表示放入的类型，String表示id的类型
public interface PoemRepository extends ElasticsearchRepository<Poem, String> {
}

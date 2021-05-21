package work.darkforest.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Configuration
public class ElasticsearchClientConfig extends AbstractElasticsearchConfiguration {

    @Override
    @Bean
    // 使用这个client来对es进行操作
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration client = ClientConfiguration.builder()
                .connectedTo("81.68.192.48:9200")
                .build();
        return RestClients.create(client).rest();
    }
}
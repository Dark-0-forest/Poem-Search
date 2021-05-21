package work.darkforest.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
// es创建索引的注解
@Document(indexName = "poems", indexStoreType = "poem")
public class Poem {
    @Id
    private String id;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String poemName;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String author;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String poemType;
    @Field(type = FieldType.Keyword)
    private String poemSource;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String poemContent;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String authorDes;
}

package work.darkforest.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Poem {
    private String id;
    private String poemName;
    private String author;
    private String poemType;
    private String poemSource;
    private String poemContent;
    private String authorDes;
}

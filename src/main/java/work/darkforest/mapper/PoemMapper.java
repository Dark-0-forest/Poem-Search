package work.darkforest.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import work.darkforest.entity.po.Poem;

import java.util.List;

@Mapper
@Repository
public interface PoemMapper {
    // 查询总条数
    int count();

    // 查询所有
    List<Poem> list();

    // 根据id查询
    Poem findById(@Param("id") String id);

    // 分页查询
    List<Poem> listByPage(@Param("start") int start, @Param("size") int size);

    // 添加
    int add(Poem poem);

    // 修改
    int update(Poem poem);

    // 逻辑删除
    int delById(@Param("id") String id);

    // 逻辑恢复
    int recovery(@Param("id") String id);
}

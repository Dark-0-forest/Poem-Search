package work.darkforest.service;

import org.apache.ibatis.annotations.Param;
import work.darkforest.base.BusinessException;
import work.darkforest.entity.po.Poem;

import java.util.List;

public interface PoemService {

    List<Poem> list();

    Poem findById(String id) throws BusinessException;

    int pageCount(int pageSize) throws BusinessException;

    List<Poem> listByPage(int page, int size) throws BusinessException;

    void add(Poem poem) throws BusinessException;

    void update(Poem poem) throws BusinessException;

    void delById(String id) throws BusinessException;

    void recovery(String id) throws BusinessException;
}

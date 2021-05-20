package work.darkforest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.darkforest.base.BusinessException;
import work.darkforest.entity.po.Poem;
import work.darkforest.mapper.PoemMapper;
import work.darkforest.service.PoemService;

import java.util.List;

@Service
public class PoemServiceImpl implements PoemService {
    @Autowired
    private PoemMapper poemMapper;

    @Override
    public List<Poem> list() {
        return poemMapper.list();
    }

    @Override
    public Poem findById(String id) throws BusinessException {
        Poem poem = null;
        try {
            poem = poemMapper.findById(id);
        } catch (Exception e) {
            throw new BusinessException("搜索失败");
        }
        if (poem == null) throw new BusinessException("未搜索到相关的诗歌");
        return poem;
    }

    @Override
    public int pageCount(int pageSize) throws BusinessException {
        if (pageSize <= 0 ) throw new BusinessException("输入的页面大小有误");
        return poemMapper.count() / pageSize + 1;
    }

    @Override
    public List<Poem> listByPage(int page, int size) throws BusinessException {
        int count = pageCount(20);
        if (page <= 0 || page > count) throw new BusinessException("输入的页数有误");
        int start = (page - 1) * size;
        return poemMapper.listByPage(start, size);
    }

    @Override
    public void add(Poem poem) throws BusinessException {
        int count = 0;
        try {
            count = poemMapper.add(poem);
        } catch (Exception e) {
            throw new BusinessException("添加诗歌错误");
        }
        if (count != 1) throw new BusinessException("添加诗歌失败");
    }

    @Override
    public void update(Poem poem) throws BusinessException {
        int count = 0;
        try {
            count = poemMapper.update(poem);
        } catch (Exception e) {
            throw new BusinessException("修改诗歌错误");
        }
        if (count != 1) throw new BusinessException("修改诗歌失败");
    }

    @Override
    public void delById(String id) throws BusinessException {
        int count = 0;
        try {
            count = poemMapper.delById(id);
        } catch (Exception e) {
            throw new BusinessException("删除诗歌错误");
        }
        if (count != 1) throw new BusinessException("删除诗歌失败");
    }

    @Override
    public void recovery(String id) throws BusinessException {
        int count = 0;
        try {
            count = poemMapper.recovery(id);
        } catch (Exception e) {
            throw new BusinessException("恢复诗歌错误");
        }
        if (count != 1) throw new BusinessException("恢复诗歌失败");
    }
}

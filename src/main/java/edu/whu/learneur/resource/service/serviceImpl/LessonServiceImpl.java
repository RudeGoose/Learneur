package edu.whu.learneur.resource.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.whu.learneur.exception.ResourceException;
import edu.whu.learneur.exception.UserServiceException;
import edu.whu.learneur.resource.dao.LessonDao;
import edu.whu.learneur.resource.entity.Lesson;
import edu.whu.learneur.resource.entity.Project;
import edu.whu.learneur.resource.service.ILessonService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LessonServiceImpl extends ServiceImpl<LessonDao, Lesson> implements ILessonService {

    public List<Lesson> addLessons(List<Lesson> lessonList, long knowledgeId){
        List<Lesson> success = new ArrayList<>();
        for(Lesson newLesson : lessonList){
            LambdaQueryWrapper<Lesson> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Lesson::getName, newLesson.getName());
            Lesson tmp = getBaseMapper().selectOne(lqw);
            if(tmp == null){
                getBaseMapper().insert(newLesson);
                success.add(newLesson);
            }
            else if(!tmp.equals(newLesson)){
                newLesson.setIdLesson(tmp.getIdLesson());
                getBaseMapper().updateById(newLesson);
            }
            if(getBaseMapper().existKR(knowledgeId, newLesson.getIdLesson()) == 0){
                getBaseMapper().insertKR(newLesson.getIdLesson(),knowledgeId);
            }
        }
        return success;
    }
    public IPage<Lesson> findLessonPage(Long knowledgeId, Integer pageNum, Integer pageSize) {
        return getBaseMapper().findLessonsByKnowledgeId(knowledgeId,new Page<>(pageNum, pageSize));
    }

    @Override
    public IPage<Lesson> findAllLessons(Integer pageNum, Integer pageSize) {
        Page<Lesson> page = new Page<>(pageNum, pageSize);
        return getBaseMapper().findLessons(page);
    }

    public Lesson findById(long id){
        return getBaseMapper().selectById(id);
    }
}

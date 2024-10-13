package cn.fff.service.impl;

import cn.fff.entity.Score;
import cn.fff.entity.StudentScore;
import cn.fff.mapper.ScoreMapper;
import cn.fff.service.ScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreService {
    @Override
    public List<StudentScore> queryStudentScore() {
        return baseMapper.queryStudentScore();
    }
}

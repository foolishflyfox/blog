package cn.fff.mapper;

import cn.fff.entity.Score;
import cn.fff.entity.StudentScore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScoreMapper extends BaseMapper<Score> {
    List<StudentScore> queryStudentScore();
}

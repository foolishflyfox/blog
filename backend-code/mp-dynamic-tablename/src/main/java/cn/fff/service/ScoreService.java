package cn.fff.service;

import cn.fff.entity.Score;
import cn.fff.entity.StudentScore;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ScoreService extends IService<Score> {

    List<StudentScore> queryStudentScore();
}

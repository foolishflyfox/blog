package cn.fff;

import cn.fff.entity.StudentScore;
import cn.fff.service.ScoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ScoreServiceTest {

    @Autowired
    private ScoreService scoreService;

    @Test
    public void testDynameTableName() {
        List<StudentScore> studentScores = scoreService.queryStudentScore();
        studentScores.forEach(e -> System.out.printf("%s %s %d\n", e.getStudentName(), e.getSubjectName(), e.getScore()));
    }
}

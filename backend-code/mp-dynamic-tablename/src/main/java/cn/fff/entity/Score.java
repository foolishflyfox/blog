package cn.fff.entity;

import lombok.Data;

@Data
public class Score {
    private Integer id;
    private Integer studentId;
    private Integer subjectId;
    private Integer result;
}

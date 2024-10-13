package cn.fff.service.impl;

import cn.fff.entity.Student;
import cn.fff.mapper.StudentMapper;
import cn.fff.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
}

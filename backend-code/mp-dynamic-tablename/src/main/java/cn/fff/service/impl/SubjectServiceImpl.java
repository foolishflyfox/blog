package cn.fff.service.impl;

import cn.fff.entity.Subject;
import cn.fff.mapper.SubjectMapper;
import cn.fff.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {
}

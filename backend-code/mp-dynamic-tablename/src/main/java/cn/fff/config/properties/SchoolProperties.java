package cn.fff.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@ConfigurationProperties("school")
@Data
public class SchoolProperties {
    /** 学校名，动态表名会添加前缀: 学校名_ */
    private String name;
    /** 需要动态添加前缀的表 */
    private Set<String> dynamicTables = new HashSet<>();
}

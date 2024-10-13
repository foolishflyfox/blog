package cn.fff.config.mp;

import cn.fff.config.properties.SchoolProperties;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class DynamicTableNameConfig {
    @Autowired
    private SchoolProperties schoolProperties;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor());
        return interceptor;
    }

    private DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor() {
        DynamicTableNameInnerInterceptor innerInterceptor = new DynamicTableNameInnerInterceptor();
        innerInterceptor.setTableNameHandler((sql, tableName) -> {
            String newTableName = tableName;
            // 配置了学校名并且当前查询的表名在指定配置中，则添加表名前缀
            if (StringUtils.hasLength(schoolProperties.getName())
                    && schoolProperties.getDynamicTables().contains(tableName)) {
                newTableName = schoolProperties.getName() + "_" + tableName;
            }
            return newTableName;
        });

        return innerInterceptor;
    }
}

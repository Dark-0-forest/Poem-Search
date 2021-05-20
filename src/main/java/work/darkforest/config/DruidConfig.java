package work.darkforest.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;


@Configuration
public class DruidConfig {

    // 将application.yaml中的数据源配置和我们的druid绑定起来，让其生效
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean   //  注册到springboot的容器中，我们不用自己配置
    public DataSource dataSource(){
        return new DruidDataSource();
    }

    // 配置druid的后台管理页面
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        HashMap<String, String> map = new HashMap<>();
        // 设置后台监控的登录信息，其key都是固定的
        map.put("loginUsername", "admin");
        map.put("loginPassword", "123456");
        // 允许哪些请求访问，""表示都可以访问
        map.put("allow", "");

        bean.setInitParameters(map);
        return bean;
    }
}

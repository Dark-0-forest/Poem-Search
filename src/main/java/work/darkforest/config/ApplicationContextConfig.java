package work.darkforest.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
// 用来在redisCache中获取redisTemplate的工具类
public class ApplicationContextConfig implements ApplicationContextAware {

    // 保留下来的工厂
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    // 提供在工厂中获取对应的方法,传对象的名字就可以返回对应的对象 redisTemplate
    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }
}
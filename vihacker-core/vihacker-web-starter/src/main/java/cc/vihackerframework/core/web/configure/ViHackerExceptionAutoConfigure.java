package cc.vihackerframework.core.web.configure;

import cc.vihackerframework.core.factory.YamlPropertySourceFactory;
import cc.vihackerframework.core.handler.BaseExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Ranger on 2022/3/13
 */
@Configuration
@ComponentScan(value="cc.vihackerframework.core.handler")
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:vihacker-error.yml")
public class ViHackerExceptionAutoConfigure {

    @Bean
    public BaseExceptionHandler baseExceptionHandler(){
        return new BaseExceptionHandler();
    }
}

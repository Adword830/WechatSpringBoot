package cn.percent.aop;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pengju.zhang
 * @date 2022-10-24 11:40
 */
@Configuration
@Slf4j
public class AopConfig {

    /**
     * 配置日志AOP
     *
     * @return LogAop
     */
    @Bean
    public LogAop logAop() {
        LogAop logAop = new LogAop();
        logAop.setRequestLogFormat(true);
        logAop.setResponseLogFormat(true);
        log.info("init LogAop success");
        return logAop;
    }
}

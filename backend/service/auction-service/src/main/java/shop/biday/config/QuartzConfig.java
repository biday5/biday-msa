package shop.biday.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import shop.biday.scheduler.QuartzJobListener;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

    private final DataSource dataSource;
    private final ApplicationContext applicationContext;
    private final PlatformTransactionManager transactionManager;
    private final QuartzJobListener quartzJobListener;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        AutoWiringSpringBeanJobFactory autoWiringSpringBeanJobFactory = new AutoWiringSpringBeanJobFactory();
        autoWiringSpringBeanJobFactory.setApplicationContext(applicationContext);
        schedulerFactoryBean.setJobFactory(autoWiringSpringBeanJobFactory);

        schedulerFactoryBean.setGlobalJobListeners(quartzJobListener);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setTransactionManager(transactionManager);
        return schedulerFactoryBean;
    }
}

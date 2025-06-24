package com.aspi.docmanage;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 启动程序
 * 
 * @author oaspi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class})
@ComponentScan(basePackages = {"com.aspi","com.oaspi"})
@EnableScheduling
public class OaspiApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(OaspiApplication.class, args);
        System.out.println("  ___                _ \n" +
                "  /___\\__ _ ___ _ __ (_)\n" +
                " //  // _` / __| '_ \\| |\n" +
                "/ \\_// (_| \\__ \\ |_) | |\n" +
                "\\___/ \\__,_|___/ .__/|_|\n" +
                "               |_|     \n " +
                "Oaspi 低代码平台启动成功...  \n");
    }
}

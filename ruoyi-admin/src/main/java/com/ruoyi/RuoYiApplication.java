package com.ruoyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableScheduling
@EnableSwagger2
public class RuoYiApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println(
                "   (♥◠‿◠)ﾉﾞ  启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                        " ............................................\n" +
                        " .                                          .\n" +
                        " .   _    _      _      _      _     _      .\n" +
                        " .  | |  | |    | |    | |    | |   | |     .\n" +
                        " .  | |__| | ___| | ___| | ___| |__ | |__   .\n" +
                        " .  |  __  |/ _ \\ |/ _ \\ |/ _ \\ '_ \\| '_ \\  .\n" +
                        " .  | |  | |  __/ |  __/ |  __/ |_) | | | | .\n" +
                        " .  |_|  |_|\\___|_|\\___|_|\\___|_.__/|_| |_| .\n" +
                        " .                                          .\n" +
                        " .               佛祖保佑 永无BUG             .\n" +
                        " ............................................"
        );
    }
}

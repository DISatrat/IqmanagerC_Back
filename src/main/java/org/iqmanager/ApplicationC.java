package org.iqmanager;


import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;


@SpringBootApplication
@EnableCaching
public class ApplicationC extends SpringBootServletInitializer {

   private static final String URL = "jdbc:mysql://localhost:3306/iqmanager";

//       private static final String URL = "jdbc:mysql://79.174.88.23:18203/iqmanager";
//    private static final String USER = "root123";
    private static final String USER = "root";
//   private static final String PASSWORD = "rOor!23$";
    //Laptop
  private static final String PASSWORD = "";
    private static final Logger logger = LoggerFactory.getLogger(ApplicationC.class);

       //public static final String URL_WEB = "https://selfish-warthog-76.loca.lt";
  public static final String URL_WEB = "http://91.197.98.105:3000";

    public static void main(String[] args) {
        flywayMigrations(URL, USER, PASSWORD);
        SpringApplication.run(ApplicationC.class, args);
    }

    public static void flywayMigrations(String url, String user, String password) {
        logger.info("db migration started..");
        Flyway flyway = Flyway.configure()
                .dataSource(url, user, password)
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
    }

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(10));
        factory.setMaxRequestSize(DataSize.ofMegabytes(10));
        return factory.createMultipartConfig();
    }
}
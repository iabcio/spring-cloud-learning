 /*
  * Copyright 2017-2018 Iabc Co.Ltd.
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *      http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

 package io.iabc.spring.cloud.learning.hello;

 import java.util.Arrays;

 import org.springframework.boot.CommandLineRunner;
 import org.springframework.boot.SpringApplication;
 import org.springframework.boot.autoconfigure.SpringBootApplication;
 import org.springframework.context.ApplicationContext;
 import org.springframework.context.annotation.Bean;

 /**
  * Project: spring
  * TODO:
  *
  * @author <a href="mailto:h@iabc.io">shuchen</a>
  * @version V1.0
  * @since 2018-03-07 11:02
  */
 @SpringBootApplication
 public class HelloApplication {

     public static void main(String[] args) {
         SpringApplication.run(HelloApplication.class, args);
     }

     @Bean
     public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
         return args -> {

             System.out.println("Let's inspect the beans provided by Spring Boot:");

             String[] beanNames = ctx.getBeanDefinitionNames();
             Arrays.sort(beanNames);
             for (String beanName : beanNames) {
                 System.out.println(beanName);
             }

         };
     }
 }

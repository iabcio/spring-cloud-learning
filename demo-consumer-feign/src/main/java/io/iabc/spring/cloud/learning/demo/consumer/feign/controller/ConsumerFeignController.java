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

 package io.iabc.spring.cloud.learning.demo.consumer.feign.controller;

 import javax.annotation.Resource;

 import org.springframework.web.bind.annotation.PathVariable;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RequestMethod;
 import org.springframework.web.bind.annotation.RestController;

 import io.iabc.spring.cloud.learning.demo.consumer.feign.client.DemoServiceClient;

 /**
  * Project: spring
  * TODO:
  *
  * @author <a href="mailto:h@iabc.io">shuchen</a>
  * @version V1.0
  * @since 2018-03-07 22:17
  */
 @RestController
 public class ConsumerFeignController {

     @Resource
     private DemoServiceClient demoServiceClient;

     @RequestMapping(value = "/consumer/hello", method = RequestMethod.GET)
     public String hello() {
         return demoServiceClient.hello();
     }

     @RequestMapping(value = "/consumer/demo/{index}", method = RequestMethod.GET)
     public String demo(@PathVariable Long index) {
         return demoServiceClient.demo(index);
     }

 }

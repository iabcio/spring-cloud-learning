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

 package io.iabc.spring.cloud.learning.demo.consumer.controller;

 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.cloud.client.ServiceInstance;
 import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.PathVariable;
 import org.springframework.web.bind.annotation.RestController;
 import org.springframework.web.client.RestTemplate;

 /**
  * Project: spring
  * TODO:
  *
  * @author <a href="mailto:h@iabc.io">shuchen</a>
  * @version V1.0
  * @since 2018-03-07 19:03
  */
 @RestController
 public class ConsumerController {
     @Autowired
     LoadBalancerClient loadBalancerClient;
     @Autowired
     RestTemplate restTemplate;

     @GetMapping("/consumer/hello")
     public String hello() {
         ServiceInstance serviceInstance = loadBalancerClient.choose("demo-service");
         String url = serviceInstance.getUri() + "/hello";
         System.out.println(url);
         return restTemplate.getForObject(url, String.class);
     }

     @GetMapping("/consumer/demo/{index}")
     public String demo(@PathVariable Long index) {
         ServiceInstance serviceInstance = loadBalancerClient.choose("demo-service");
         String url = serviceInstance.getUri() + "/demo/" + index;
         System.out.println(url);
         return restTemplate.getForObject(url, String.class);
     }
 }

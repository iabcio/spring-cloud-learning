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

 package io.iabc.spring.cloud.learning.eureka.registry.controller;

 import com.alibaba.fastjson.JSON;

 import java.util.List;
 import java.util.concurrent.ConcurrentHashMap;
 import java.util.concurrent.atomic.LongAdder;

 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.cloud.client.ServiceInstance;
 import org.springframework.cloud.client.discovery.DiscoveryClient;
 import org.springframework.web.bind.annotation.PathVariable;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RequestMethod;
 import org.springframework.web.bind.annotation.RestController;

 /**
  * Project: spring
  * TODO:
  *
  * @author <a href="mailto:h@iabc.io">shuchen</a>
  * @version V1.0
  * @since 2018-03-07 15:40
  */
 @RestController
 public class DcController {
     @Autowired
     DiscoveryClient discoveryClient;

     private ConcurrentHashMap<String, LongAdder> counters = new ConcurrentHashMap<>();

     @RequestMapping(value = "/all", method = RequestMethod.GET)
     public String all() {
         String services = "Services: " + discoveryClient.getServices();
         return services;
     }

     @RequestMapping(value = "/instances/{applicationName}", method = RequestMethod.GET)
     public String serviceInstancesByApplicationName(@PathVariable String applicationName) {
         List<ServiceInstance> serviceInstances = this.discoveryClient.getInstances(applicationName);

         return JSON.toJSONString(serviceInstances);
     }

     @RequestMapping(value = "/instance/{applicationName}", method = RequestMethod.GET)
     public String getOneServiceInstanceByApplicationName(@PathVariable String applicationName) {
         List<ServiceInstance> serviceInstances = this.discoveryClient.getInstances(applicationName);

         ServiceInstance serviceInstance = this.selectOneServiceInstance(applicationName, serviceInstances);

         return JSON.toJSONString(serviceInstance);
     }

     @RequestMapping(value = "/uri/{applicationName}", method = RequestMethod.GET)
     public String getOneServiceInstanceUriByApplicationName(@PathVariable String applicationName) {
         List<ServiceInstance> serviceInstances = this.discoveryClient.getInstances(applicationName);

         ServiceInstance serviceInstance = this.selectOneServiceInstance(applicationName, serviceInstances);

         if (serviceInstance != null) {
             return serviceInstance.getUri().toString();
         }

         return "null";
     }

     private ServiceInstance selectOneServiceInstance(String applicationName,
         final List<ServiceInstance> serviceInstances) {
         if (serviceInstances != null && serviceInstances.size() > 0) {
             LongAdder counter = counters.get(applicationName);
             if (counter == null) {
                 counter = new LongAdder();
                 counters.put(applicationName, counter);
             }
             counter.increment();
             return serviceInstances.get(counter.intValue() % serviceInstances.size());
         }

         return null;
     }
 }

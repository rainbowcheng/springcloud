package com.feign.hello;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.service.api.HelloService;

@FeignClient("MY-CLOUD")
public interface RefactorHelloService extends HelloService {

}

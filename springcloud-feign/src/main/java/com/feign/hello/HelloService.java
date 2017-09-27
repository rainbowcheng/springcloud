package com.feign.hello;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("MY-CLOUD")
public interface HelloService {

	@RequestMapping("demo/hello")
	String hello();
	
	@RequestMapping(value="/demo/username",method=RequestMethod.GET)
	String hello(@RequestParam("name") String username);
	
	@RequestMapping(value="/demo/user",method=RequestMethod.GET)
	User hello(@RequestHeader("name") String name ,@RequestHeader("age") Integer age);
	
	@RequestMapping(value="/demo/userinfo",method=RequestMethod.POST)
	String hello(@RequestBody User user);
}

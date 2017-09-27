package cn.consumer.ribbonconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@EnableCircuitBreaker  //注解开启断路器功能
@EnableDiscoveryClient //注Eureka客户端应用，以获得服务发现能力。
@SpringBootApplication
@ComponentScan("cn")
public class RibbonConsumerApplication {
	
	/**
	 * 创建SpringBean实例，并通过@LoadBalanced注解开启客户端负载均衡
	 * @return
	 */
	@Bean
	@LoadBalanced
	RestTemplate restTemplate(){
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(RibbonConsumerApplication.class, args);
		//new SpringApplicationBuilder(RibbonConsumerApplication.class).web(true).run(args);
	}
}

package cn.consumer.ribbonconsumer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import cn.consumer.service.HelloService;

@RestController
public class ConsumerController {
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	HelloService helloService;

	@RequestMapping(value="/ribbon-consumer",method=RequestMethod.GET)
	public String helloConsumer(){
		return restTemplate.getForEntity("http://MY-CLOUD/demo/hello", String.class).getBody();
	}
	
	@RequestMapping(value="/userinfo",method=RequestMethod.GET)
	public String getUserInfo(){
		//如果name= ｛name｝
		Map<String,String> params=new HashMap<>();
		params.put("name", "data");
		//ResponseEntity<String> responseEntity=restTemplate.getForEntity("http://MY-CLOUD/demo/userInfo?name={name}", String.class,params");
		//return restTemplate.getForObject("http://MY-CLOUD/demo/userInfo?name={1}", String.class,"didi");
		ResponseEntity<String> responseEntity=restTemplate.getForEntity("http://MY-CLOUD/demo/userInfo?name={1}", String.class,"didi");
		return responseEntity.getBody();
	}
	
	@RequestMapping(value="/user",method=RequestMethod.POST)
	public String postUserInfo(){
		//User user=new User("didi",30);
		String name="didi";
		ResponseEntity<String> responseEntity=restTemplate.postForEntity("http://MY-CLOUD/demo/userInfo",name, String.class);
		return responseEntity.getBody();
	}
	
	/**
	 * 带有熔断的
	 * @return
	 */
	@RequestMapping(value = "/hys-consumer", method= RequestMethod.GET)
	public String helloService(){
		return helloService.helloService();
	}

}

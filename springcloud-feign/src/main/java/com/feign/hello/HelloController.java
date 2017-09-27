package com.feign.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;

@RestController
public class HelloController {

	@Autowired
	HelloService helloService;
	
	@Autowired
	RefactorHelloService refactorHelloService;
	
	@RequestMapping(value="/feign-consumer",method=RequestMethod.GET)
	public String helloConsumer(){
		return helloService.hello();
	}
	
	@RequestMapping(value="/feign-consumer2",method=RequestMethod.GET)
	public String helloConsumer2(){
		StringBuilder sb=new StringBuilder();
		sb.append(helloService.hello()).append("\n");
		sb.append(helloService.hello("rainbow")).append("\n");
		sb.append(helloService.hello("rain", 30)).append("\n");
		sb.append(helloService.hello(new User("haha",30))).append("\n");
		return sb.toString();
	}
	
	@RequestMapping(value="/feign-consumer3",method=RequestMethod.GET)
	public String helloConsumer3(){
		
		return refactorHelloService.hello("rainApi", 30).toString();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String test = "51e88";
		JSONObject o = new JSONObject();
		o.put("a", test);
		String comp = JSONValue.toJSONString(o, JSONStyle.MAX_COMPRESS);
		//assertEquals("{a:\"51e88\"}", comp);

		o = JSONValue.parse(comp, JSONObject.class);
		//assertEquals(o.get("a"), test);
	}
	
}

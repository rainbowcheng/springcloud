package cn.consumer.service;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

import rx.Observable;
import rx.Subscriber;

/**
 * 带有熔断服务
 * 
 * @author chenghong
 *
 */
@Service
public class HelloService {
	@Autowired
	RestTemplate restTemplate;

	/**
	 * 当方法抛出类型为IOException的异常后，Hystrix会将它包装在HystrixExcepiton中抛出，
	 * 这样就不会触发后续的fallback逻辑
	 * @return
	 */
	//@HystrixCommand(fallbackMethod = "helloFallback",ignoreExceptions={IOException.class}) // 同步
	@HystrixCommand(fallbackMethod = "helloFallback") // 同步
	public String helloService() {
		//throw new RuntimeException("helloService command failed");
		return restTemplate.getForEntity("http://MY-CLOUD/demo/hello", String.class).getBody();
	}

	/**
	 * 在run()执行过程中出现错误、超时、线程池拒绝、断路器熔断等情况
	 * @return
	 */
	public String helloFallback(Throwable e) {
		//assert "helloService command failed".equals(e. getMessage());
		return "error";
	}

	//---------------------------------------------------------以上是熔断基本篇--------------------------
	
	@HystrixCommand(commandKey="getUserByidAsync",groupKey="UserGroup",threadPoolKey="getUserByIdThread") // 异步
	public Future<User> getUserByidAsync(final String id) {
		return new AsyncResult<User>() {
			@Override
			public User invoke() {
				return restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class, id);
			}
		};
	}
	
	//----------------------------------------------------------------------------------------------------
	/**
	 * 所以Hystrix会将该结果置入请求缓存中，缓存 key值会使用所有的参数，也就是这里long类型。
	 * 或通过keyMethod指定key值
	 * 或使用cachekey，不过优先级 低于cachekeymethod（@CacheKey("id") User user）
	 * @param id
	 * @return
	 */
	@CacheResult//(cacheKeyMethod="getUserByIdCacheKey")
	@HystrixCommand
	public User getUserById(@CacheKey("id") Long id){
		return restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class, id);
	}
	
	/**
	 * 注意commandKey属性是必须要指定的，它用来指明需要使用请求缓存的请求命令，因为只有通过该属性的
	 * 配置，Hystrix才能找到 正确的请求命令缓存位置。
	 * @param user
	 */
	@CacheRemove(commandKey="getUserById")
	@HystrixCommand
	public void update(@CacheKey("id") User user){
		 restTemplate.postForObject("http://USER-SERVICE/users", user,User.class);
	}

	//------------------------------------------以上是熔断升级篇-缓存------------------------------------
	
	@HystrixCollapser(batchMethod="findAll",collapserProperties={
			@HystrixProperty(name="timerDelayInMilliseconds",value="100")
	})
	public User find(Long id){
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@HystrixCommand
	public List<User> findAll(List<Long>ids){
		return restTemplate.getForObject("http://USER-SERVICE/users?ids={l}",
				List. class, org.apache.commons.lang.StringUtils. join (ids, ", ")) ;
	}
		
	 
	//------------------------------------------以上是熔断升级篇-合并请求---------------------------------
	@HystrixCommand
	//EAGER 是该参数的模式值， 表示使用observe ()执行方式。
	//HystrixCommand(observableExecutionMode = ObservableExecutionMode.EAGER)
	//表示使用toObservable()执行方式。
	//@HystrixCommand(observableExecutionMode = ObservableExecutionMode.LAZY)
	public Observable<User> getUserByid(final String id) {
		return Observable.create(new Observable.OnSubscribe<User>() {
			@Override
			public void call(Subscriber<? super User> observer) {
				try {
					if (!observer.isUnsubscribed()) {
						User user = restTemplate.getForObject("http://HELLO-SERVICE/users/ {1} ", User.class, id);
						observer.onNext(user);
						observer.onCompleted();
					}
				} catch (Exception e) {
					observer.onError(e);
				}
			}
		});
	}

}

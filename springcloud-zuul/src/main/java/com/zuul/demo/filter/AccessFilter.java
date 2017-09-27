package com.zuul.demo.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

//@Component或在主类中使用@Bean
public class AccessFilter extends ZuulFilter {
	private static Logger log=LoggerFactory.getLogger(AccessFilter.class);

	@Override
	public Object run() {
		// TODO Auto-generated method stub
		RequestContext ctx=RequestContext.getCurrentContext();
		HttpServletRequest request=ctx.getRequest();
		
		try {
			log.info("send {} request to {}",request.getMethod(),request.getRequestURL().toString());
			
			Object accessToken =request.getParameter("accessToken");
			if(accessToken==null){
				log.warn("access token  is empty");
				ctx.setSendZuulResponse(false);//不对其进行路由
				ctx.setResponseStatusCode(401);
				ctx.setResponseBody("{rainbow}");
				return null;
			}
			log.info("access token ok");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ctx.set("error.status_code",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			ctx.set("error.exception",e);
		}
		return null;
	}

	/**
	 * 判断该过滤器是否需要被执行
	 */
	@Override
	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		RequestContext ctx=RequestContext.getCurrentContext();
		ZuulFilter failedFilter=(ZuulFilter)ctx.get("failed.filter");
		if(failedFilter!=null&&failedFilter.filterType().equals("post")){
			return true;
		}
		return false;
	}

	/**
	 * 多个过滤器的执行顺序
	 */
	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	/**
	 * 决定过滤器在请求的哪个生命周期中执行。
	 * <ul>
	 *    <li>
	 *       pre:可以在请求被路由之前调用
	 *    </li>
	 *    <li>
	 *       routing:在路由请求 时被调用
	 *    </li>
	 *    <li>
	 *       post:在routing和error过滤器之后被调用
	 *    </li>
	 *    <li>
	 *       error:处理请求时发生错误时被调用
	 *    </li>
	 * </ul>
	 */
	public String filterType() {
		// TODO Auto-generated method stub
		return "pre";
	}

}

仪表盘
1.在pom里：
	<parent>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-parent</artifactId>
		<version>Camden.SR3</version>
		<relativePath/> 
	</parent>
  	<dependencies>
		<!-- 容错保护 -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-hystrix</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-hystrix-dashboard</artifactId>
		</dependency>
		
		<!-- 监控模块 -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
		
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
  
  2.application:
  spring.application.name=hystrix-dashboard
  server.port=2001
  
3.main:
@EnableHystrixDashboard
  

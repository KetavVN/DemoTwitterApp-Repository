package com.redis.test.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Java based web application context
 * 
 * @author ketav
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages={"com.redis.test.controller"})
public class WebAppConfiguration extends WebMvcConfigurerAdapter{

	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }
	
    /*//override this method if extending WebMvcConfigurationSupport class instead of WebMvcConfigurerAdapter
    @Bean @Override
    public HandlerMapping resourceHandlerMapping() {
        AbstractHandlerMapping handlerMapping = (AbstractHandlerMapping) super.resourceHandlerMapping();
        handlerMapping.setOrder(-1);
        return handlerMapping;
    }*/
	
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    
    @Bean
    public InternalResourceViewResolver viewResolver(){
    	InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    	viewResolver.setPrefix("/WEB-INF/views/");
    	viewResolver.setSuffix(".jsp");
    	return viewResolver;
    }
    
    @Bean
    public CommonsMultipartResolver multipartViewResolver(){
    	CommonsMultipartResolver vr = new CommonsMultipartResolver();
    	vr.setMaxUploadSize(50000);	//bytes
    	return vr;
    }
    
    @Bean
	public ConversionService conversionService() {
		DefaultFormattingConversionService bean = new DefaultFormattingConversionService();
		bean.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());
		return bean;
	}
	
}

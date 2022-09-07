package com.a243.magicbazaar.config.mvc;

import com.a243.magicbazaar.config.handler.interceptor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * mvc
 * 配置mvc的功能选项
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private StaffInterceptor staffInterceptor;
    private NoPageInterceptor noPageInterceptor;
    private PermissionInterceptor permissionInterceptor;
    private NoUsePageInterceptor noUsePageInterceptor;
    private UserInterceptor userInterceptor;


    @Autowired
    public void setStaffInterceptor(StaffInterceptor staffInterceptor) {
        this.staffInterceptor = staffInterceptor;
    }

    @Autowired
    public void setNoPageInterceptor(NoPageInterceptor noPageInterceptor) {
        this.noPageInterceptor = noPageInterceptor;
    }

    @Autowired
    public void setPermissionInterceptor(PermissionInterceptor permissionInterceptor) {
        this.permissionInterceptor = permissionInterceptor;
    }

    @Autowired
    public void setNoUsePageInterceptor(NoUsePageInterceptor noUsePageInterceptor) {
        this.noUsePageInterceptor = noUsePageInterceptor;
    }

    @Autowired
    public void setUserInterceptor(UserInterceptor userInterceptor) {
        this.userInterceptor = userInterceptor;
    }

    /**
     * 拦截器
     * 在mvc中添加拦截器进行拦截
     *
     * @param registry registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(staffInterceptor)
                .addPathPatterns("/url/**",
//                        "/index",
                        "/admin/**")
                .excludePathPatterns("/admin/login",
                        "/admin/token/**",
                        "/url/404",
                        "/static/**",
                        "/api/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/lib/**",
                        "/page/**",
                        "/font/**",
                        "/picture/**",
                        "/image/**",
                        "/**/*.js");
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/reception/url/**",
                        "/reception/commodity/typenumber",
                        "/reception/commodity/addcart",
                        "/reception/commodity/cartnum",
                        "/reception/commodity/loadcart",
                        "/reception/commodity/deletecart",
                        "/reception/commodity/collet",
                        "/reception/alipay/**",
                        "/reception/order/**",
                        "/reception/user/**")
                .excludePathPatterns("/reception/url/register",
                        "/reception/url/index",
                        "/reception/url/register",
                        "/reception/url/cart",
                        "/reception/url/category",
//                        "/reception/commodity/trend",
//                        "/reception/commodity/sale",
//                        "/reception/commodity/{id}",
//                        "/reception/commodity/addcart",
//                        "/reception/commodity/cartnum",
//                        "/reception/commodity/loadcart",
//                        "/reception/commodity/deletecart",
//                        "/reception/commodity/list",
//                        "/reception/commodity/stars",
                        "/reception/user/checkusername",
                        "/reception/user/registercode",
                        "/reception/user/checkregistercode",
                        "/reception/user/register",
                        "/reception/user/checkuser",
                        "/reception/user/logincode",
                        "/reception/user/checklogincode",
                        "/reception/commodity/type/list",
                        "/reception/commodity/brand/list",
                        "/static/**",
                        "/api/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/lib/**",
                        "/page/**",
                        "/font/**",
                        "/picture/**",
                        "/image/**",
                        "/**/*.js");
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login",
                        "/admin/token/**",
                        "/static/**",
                        "/api/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/lib/**",
                        "/page/**",
                        "/font/**",
                        "/picture/**",
                        "/image/**",
                        "/**/*.js");
        registry.addInterceptor(noUsePageInterceptor)
                .addPathPatterns("/url/**")
                .excludePathPatterns("/url/404",
                        "/static/**",
                        "/api/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/lib/**",
                        "/page/**",
                        "/font/**",
                        "/picture/**",
                        "/image/**",
                        "/**/*.js");
        registry.addInterceptor(noPageInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**",
                        "/api/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/lib/**",
                        "/page/**",
                        "/font/**",
                        "/picture/**",
                        "/image/**",
                        "/**/*.js");
    }

    /**
     * 资源映射配置
     *
     * @param registry register
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/api/**").addResourceLocations("classpath:/templates/layuimini/api/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/templates/layuimini/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/templates/layuimini/js/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/templates/layuimini/images/");
        registry.addResourceHandler("/lib/**").addResourceLocations("classpath:/templates/layuimini/lib/");
        registry.addResourceHandler("/page/**").addResourceLocations("classpath:/templates/layuimini/page/");
        registry.addResourceHandler("/font/**").addResourceLocations("classpath:/templates/layuimini/font/");
        registry.addResourceHandler("/picture/**").addResourceLocations("classpath:/templates/layuimini/picture/");
        registry.addResourceHandler("/image/**").addResourceLocations("file:D:/Idea-Project/image/");
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:8080", "http://localhost:8082");
    }
}

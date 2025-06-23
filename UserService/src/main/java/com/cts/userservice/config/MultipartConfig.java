package com.cts.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MultipartConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir; // Inject the same property here

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
//        jsonConverter.setObjectMapper(new ObjectMapper());
//        jsonConverter.setSupportedMediaTypes(Arrays.asList(
//                MediaType.APPLICATION_JSON,
//                MediaType.APPLICATION_OCTET_STREAM,
//                new MediaType("application", "*+json")
//        ));
//        converters.add(0, jsonConverter);
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**") // URL path to access images
                .addResourceLocations("file:" + uploadDir + "/"); // Physical location of images
    }
}
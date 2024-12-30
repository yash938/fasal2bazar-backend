package com.agriRoot.Config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudConfig {

    @Bean
    public Cloudinary cloudinary(){
        Map config = new HashMap();
        config.put("cloud_name","dby9oywcg");
        config.put("api_key","421533583768444");
        config.put("api_secret","GxjRznmhnsDwkNRP_GrSNB2eW-0");
        config.put("secure",true);
        return new Cloudinary(config);
    }

}

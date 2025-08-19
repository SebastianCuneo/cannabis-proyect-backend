package uy.edu.ucu.inventario.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // permite CORS en todas las rutas
                .allowedOrigins("*") // origen del frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // métodos permitidos
                .allowedHeaders("*") // permite todos los headers
        		.allowCredentials(false); // importante: false cuando usás "*"
    }
}
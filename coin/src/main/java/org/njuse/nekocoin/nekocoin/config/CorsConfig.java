package org.njuse.nekocoin.nekocoin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;



    @Configuration

    public class CorsConfig {
        private static String[] originsVal = new String[]{
                "localhost:8000",
                "127.0.0.1:8000",
                "121.5.8.40:8000",
                "127.0.0.1",
                "localhost",
                "localhost:8080",
                "127.0.0.1:8080"
        };

        @Bean
        public CorsFilter corsFilter() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            addAllowedOrigins(corsConfiguration);
//            corsConfiguration.addAllowedOrigin("*");
            corsConfiguration.addAllowedHeader("*");
            corsConfiguration.addAllowedMethod("*");
            // 跨域session共享
            corsConfiguration.setAllowCredentials(true);
            source.registerCorsConfiguration("/**", corsConfiguration);
            return new CorsFilter(source);
        }

        private void addAllowedOrigins(CorsConfiguration corsConfiguration) {
            for (String origin : originsVal) {
                corsConfiguration.addAllowedOrigin("http://" + origin);
                corsConfiguration.addAllowedOrigin("https://" + origin);
            }
        }
    }


package com.example.healthyeatsuserservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@Slf4j
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        log.info("Cors filter .....................................................");
        String productionGateway = "https://healthyeats-gatewayserver-dev.herokuapp.com";
        String localGateway = "http://localhost:8072/";
        if (productionGateway.equalsIgnoreCase(request.getHeader("Origin"))) {
            response.setHeader("Access-Control-Allow-Origin", productionGateway);
        } else if (localGateway.equalsIgnoreCase(request.getHeader("Origin"))) {
            response.setHeader("Access-Control-Allow-Origin", localGateway);
        }

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT,PATCH, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, Authorization, Origin, Accept, Access-Control-Request-Method, Access-Control-Request-Headers");

        chain.doFilter(req, res);
    }
}

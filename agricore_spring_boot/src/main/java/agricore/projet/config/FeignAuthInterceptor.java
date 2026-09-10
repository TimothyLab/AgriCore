package agricore.projet.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;


import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignAuthInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate template) {

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        System.out.println("FEIGN INTERCEPTOR CALLED");
        System.out.println("RequestContext = " + RequestContextHolder.getRequestAttributes());

        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && !authHeader.isEmpty()) {
            template.header("Authorization", authHeader);
        }

        System.out.println("FEIGN INTERCEPTOR CALLED");
        System.out.println("RequestContext = " + RequestContextHolder.getRequestAttributes());
    }
}

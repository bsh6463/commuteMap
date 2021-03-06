package hello.commute.exceptionresolver;

import hello.commute.api.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //IllgalArgsException인 경우 400으로return
        try{
            if (ex instanceof IllegalArgumentException){
                log.info("[MyExceptionResolver] IllegalArgumentException to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return new ModelAndView(); //정상 흐름으로 전환.
            }else if (ex instanceof APIKeyException){
                response.sendError(HttpServletResponse.SC_OK, ex.getMessage());
                return new ModelAndView();
            }else if(ex instanceof APIServerException){
                response.sendError(HttpServletResponse.SC_OK, ex.getMessage());
                return new ModelAndView();
            }else if(ex instanceof EndOfServiceException){
                response.sendError(HttpServletResponse.SC_OK, ex.getMessage());
                return new ModelAndView(); //정상 흐름으로 전환.
            }else if (ex instanceof OutOfServiceException){
                response.sendError(HttpServletResponse.SC_OK, ex.getMessage());
                return new ModelAndView(); //정상 흐름으로 전환.
            }else if (ex instanceof TooCloseException){
                response.sendError(HttpServletResponse.SC_OK, ex.getMessage());
                return new ModelAndView(); //정상 흐름으로 전환.
            }else if (ex instanceof NoResultException){
                response.sendError(HttpServletResponse.SC_OK, ex.getMessage());
                return new ModelAndView(); //정상 흐름으로 전환.
            }

        } catch (IOException e) {
           log.error("[MyExceptionResolver] IOException 발생", e);
        }
        return null;
    }
}

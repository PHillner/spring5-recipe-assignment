package guru.springframework.recipeapp.controllers;

import guru.springframework.recipeapp.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(RuntimeException.class)
//    public ModelAndView handleRuntimeException(Exception exception) {
//        log.error("Handling RuntimeException");
//        log.error(exception.getMessage());
//        ModelAndView mav = new ModelAndView();
//        mav.setViewName("400error");
//        mav.addObject("exception", exception);
//        return mav;
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(NumberFormatException.class)
//    public ModelAndView handleNumberFormatException(Exception exception) {
//        log.error("Handling NumberFormatException");
//        log.error(exception.getMessage());
//        ModelAndView mav = new ModelAndView();
//        mav.setViewName("400error");
//        mav.addObject("exception", exception);
//        return mav;
//    }
//
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(NotFoundException.class)
//    public ModelAndView handleNotFound(Exception exception) {
//        log.error("Handling NotFoundException");
//        log.error(exception.getMessage());
//        ModelAndView mav = new ModelAndView();
//        mav.setViewName("404error");
//        mav.addObject("exception", exception);
//        return mav;
//    }
}

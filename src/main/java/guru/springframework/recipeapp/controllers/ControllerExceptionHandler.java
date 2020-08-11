package guru.springframework.recipeapp.controllers;

import guru.springframework.recipeapp.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.thymeleaf.exceptions.TemplateInputException;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({RuntimeException.class, WebExchangeBindException.class})
    public String handleBadRequest(Exception exception, Model model) {
        log.error("Handling bad request exception");
        log.error(exception.getMessage());
        model.addAttribute("exception", exception);
        return "400error";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class, TemplateInputException.class})
    public String handleNotFound(Exception exception, Model model) {
        log.error("Handling not found exception");
        log.error(exception.getMessage());
        model.addAttribute("exception", exception);
        return "404error";
    }
}

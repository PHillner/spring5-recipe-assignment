package guru.springframework.recipeapp.controllers;

import guru.springframework.recipeapp.commands.RecipeCommand;
import guru.springframework.recipeapp.exceptions.NotFoundException;
import guru.springframework.recipeapp.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping({"recipe/{id}"})
    public String getRecipePage(Model model, @PathVariable("id") String id) {
        model.addAttribute("recipe", recipeService.findById(Long.valueOf(id)));
        log.debug("Returning recipe, id: " + id);
        return "recipe/show";
    }

    @GetMapping({"recipe/new"})
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        log.debug("Returning new recipe form");
        return "recipe/recipe_form";
    }

    @GetMapping({"recipe/{id}/edit"})
    public String editRecipe(Model model, @PathVariable("id") String id) {
        log.debug("Got request for recipe edit page, id: " + id);
        RecipeCommand command = recipeService.findCommandById(Long.valueOf(id));
        model.addAttribute("recipe", command);
        log.debug("Returning recipe edit form, id: " + id);
        return "recipe/recipe_form";
    }

    @PostMapping("recipe")
    public String saveOrUpdateRecipe(@ModelAttribute RecipeCommand command) {
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
        log.debug("Returning saved recipe, id: " + savedCommand.getId());
        return String.format("redirect:recipe/%s", savedCommand.getId());
    }

    @GetMapping({"recipe/{id}/remove"})
    public String removeRecipe(@PathVariable("id") String id) {
        recipeService.deleteById(Long.valueOf(id));
        return "redirect:/";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(Exception exception) {
        log.error("Handling NotFoundException for recipe");
        log.error(exception.getMessage());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("404error");
        mav.addObject("exception", exception);
        return mav;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView handleNumberFormatException(Exception exception) {
        log.error("Handling NumberFormatException for recipe");
        log.error(exception.getMessage());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("400error");
        mav.addObject("exception", exception);
        return mav;
    }
}

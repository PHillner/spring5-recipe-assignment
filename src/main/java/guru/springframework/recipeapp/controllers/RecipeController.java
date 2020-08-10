package guru.springframework.recipeapp.controllers;

import guru.springframework.recipeapp.commands.RecipeCommand;
import guru.springframework.recipeapp.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
public class RecipeController {

    private static final String RECIPE_FORM_URL = "recipe/recipe_form";

    private final RecipeService recipeService;

    private WebDataBinder webDataBinder;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        this.webDataBinder = webDataBinder;
    }

    @GetMapping({"recipe/{id}"})
    public String getRecipePage(Model model, @PathVariable("id") String id) {
        model.addAttribute("recipe", recipeService.findById(id));
        log.debug("Returning recipe, id: " + id);
        return "recipe/show";
    }

    @GetMapping({"recipe/new"})
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        log.debug("Returning new recipe form");
        return RECIPE_FORM_URL;
    }

    @GetMapping({"recipe/{id}/edit"})
    public String editRecipe(Model model, @PathVariable("id") String id) {
        log.debug("Got request for recipe edit page, id: " + id);
        model.addAttribute("recipe", recipeService.findCommandById(id));
        log.debug("Returning recipe edit form, id: " + id);
        return RECIPE_FORM_URL;
    }

    @PostMapping("recipe")
    public String saveOrUpdateRecipe(@ModelAttribute("recipe") RecipeCommand command) {
        webDataBinder.validate();
        BindingResult bindingResult = webDataBinder.getBindingResult();

        if (bindingResult.hasErrors()) {
            // Log all errors
            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });
            return RECIPE_FORM_URL;
        }

        recipeService.saveRecipeCommand(command).subscribe();
        log.debug("Returning saved recipe, id: " + command.getId());
        return String.format("redirect:recipe/%s", command.getId());
    }

    @GetMapping({"recipe/{id}/remove"})
    public String removeRecipe(@PathVariable("id") String id) {
        recipeService.deleteById(id).subscribe();
        return "redirect:/";
    }
}

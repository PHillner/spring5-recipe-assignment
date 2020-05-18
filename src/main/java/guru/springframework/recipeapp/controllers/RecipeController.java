package guru.springframework.recipeapp.controllers;

import guru.springframework.recipeapp.commands.RecipeCommand;
import guru.springframework.recipeapp.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.websocket.server.PathParam;

@Slf4j
@Controller
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"recipe/{id}"})
    public String getRecipePage(Model model, @PathVariable("id") String id) {
        model.addAttribute("recipe", recipeService.findById(Long.valueOf(id)));
        log.debug("Returning recipe, id: " + id);
        return "recipe/show";
    }

    @RequestMapping({"recipe/new"})
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        log.debug("Returning new recipe form");
        return "recipe/recipe_form";
    }

    @RequestMapping({"recipe/{id}/edit"})
    public String editRecipe(Model model, @PathVariable("id") String id) {
        log.debug("Got request for recipe edit page, id: " + id);
        RecipeCommand command = recipeService.findCommandById(Long.valueOf(id));
        model.addAttribute("recipe", command);
        log.debug("Returning recipe edit form, id: " + id);
        return "recipe/recipe_form";
    }

    @PostMapping
    @RequestMapping("recipe")
    public String saveOrUpdateRecipe(@ModelAttribute RecipeCommand command) {
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
        log.debug("Returning saved recipe, id: " + savedCommand.getId());
        return String.format("redirect:recipe/%s", savedCommand.getId());
    }

    @RequestMapping({"recipe/{id}/remove"})
    public String removeRecipe(@PathVariable("id") String id) {
        recipeService.deleteById(Long.valueOf(id));
        return "redirect:/";
    }
}

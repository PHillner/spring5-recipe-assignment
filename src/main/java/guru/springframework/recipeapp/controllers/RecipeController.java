package guru.springframework.recipeapp.controllers;

import guru.springframework.recipeapp.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
        log.debug("Got fetch request for recipe page, id: " + id);
        model.addAttribute("recipe", recipeService.findById(Long.valueOf(id)));
        log.debug("Returning recipe, id: " + id);
        return "recipe/show";
    }
}

package guru.springframework.recipeapp.controllers;

import guru.springframework.recipeapp.commands.IngredientCommand;
import guru.springframework.recipeapp.commands.RecipeCommand;
import guru.springframework.recipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.recipeapp.services.IngredientService;
import guru.springframework.recipeapp.services.RecipeService;
import guru.springframework.recipeapp.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class IngredientController {

    private final IngredientService ingredientService;
    private final RecipeService recipeService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(IngredientService ingredientService, RecipeService recipeService, UnitOfMeasureService unitOfMeasureService) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping({"recipe/{recipeId}/ingredients"})
    public String getIngredientList(Model model, @PathVariable("recipeId") String recipeId) {
        model.addAttribute("recipe", recipeService.findCommandById(recipeId));
        log.debug("Returning ingredients, recipeId: " + recipeId);
        return "recipe/ingredient/list";
    }

    @GetMapping({"recipe/{recipeId}/ingredient/{id}"})
    public String showIngredient(Model model, @PathVariable("recipeId") String recipeId, @PathVariable("id") String id) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id));
        log.debug("Returning ingredient, recipeId: " + recipeId + ", id: " + id);
        return "recipe/ingredient/show";
    }

    @GetMapping({"recipe/{recipeId}/ingredient/new"})
    public String newIngredient(Model model, @PathVariable("recipeId") String recipeId) {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        ingredientCommand.setUom(new UnitOfMeasureCommand());
        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
        log.debug("Returning new ingredient form, recipeId: " + recipeId);
        return "recipe/ingredient/ingredient_form";
    }

    @GetMapping({"recipe/{recipeId}/ingredient/{id}/edit"})
    public String editIngredient(Model model, @PathVariable("recipeId") String recipeId, @PathVariable("id") String id) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id));
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
        log.debug("Returning ingredient edit form, recipeId: " + recipeId + ", id: " + id);
        return "recipe/ingredient/ingredient_form";
    }

    @PostMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdateIngredient(@ModelAttribute("ingredient") IngredientCommand command) {
        log.debug("Saving ingredient, recipeId: " + command.getRecipeId() + ", id: " + command.getId());
        ingredientService.saveIngredientCommand(command).subscribe();
        return String.format("redirect:ingredient/%s", command.getId());
    }

    @GetMapping({"recipe/{recipeId}/ingredient/{id}/remove"})
    public String deleteIngredient(@PathVariable("recipeId") String recipeId, @PathVariable("id") String id) {
        ingredientService.deleteById(recipeId, id).subscribe();
        log.debug("Removed ingredient, recipeId: " + recipeId + ", id: " + id);
        return String.format("redirect:/recipe/%s/ingredients", recipeId);
    }
}

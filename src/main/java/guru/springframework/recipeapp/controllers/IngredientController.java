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
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id).block());
        log.debug("Returning ingredient, recipeId: " + recipeId + ", id: " + id);
        return "recipe/ingredient/show";
    }

    @GetMapping({"recipe/{recipeId}/ingredient/new"})
    public String newIngredient(Model model, @PathVariable("recipeId") String recipeId) {
        // Ensure valid recipeId
        RecipeCommand recipeCommand = recipeService.findCommandById(recipeId);

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        ingredientCommand.setUom(new UnitOfMeasureCommand());
        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms().collectList().block());
        log.debug("Returning new ingredient form, recipeId: " + recipeId);
        return "recipe/ingredient/ingredient_form";
    }

    @GetMapping({"recipe/{recipeId}/ingredient/{id}/edit"})
    public String editIngredient(Model model, @PathVariable("recipeId") String recipeId, @PathVariable("id") String id) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id).block());
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms().collectList().block());
        log.debug("Returning ingredient edit form, recipeId: " + recipeId + ", id: " + id);
        return "recipe/ingredient/ingredient_form";
    }

    @PostMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdateIngredient(@ModelAttribute IngredientCommand command) {
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command).block();
        log.debug("Saved ingredient, recipeId: " + savedCommand.getRecipeId() + ", id: " + savedCommand.getId());
        return String.format("redirect:ingredient/%s", savedCommand.getId());
    }

    @GetMapping({"recipe/{recipeId}/ingredient/{id}/remove"})
    public String deleteIngredient(@PathVariable("recipeId") String recipeId, @PathVariable("id") String id) {
        ingredientService.deleteById(recipeId, id).block();
        log.debug("Removed ingredient, recipeId: " + recipeId + ", id: " + id);
        return String.format("redirect:/recipe/%s/ingredients", recipeId);
    }
}

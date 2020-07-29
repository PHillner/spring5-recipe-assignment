package guru.springframework.recipeapp.services;

import guru.springframework.recipeapp.commands.IngredientCommand;
import guru.springframework.recipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.recipeapp.converters.IngredientCommandToIngredientConverter;
import guru.springframework.recipeapp.converters.IngredientToIngredientCommandConverter;
import guru.springframework.recipeapp.model.Ingredient;
import guru.springframework.recipeapp.model.Recipe;
import guru.springframework.recipeapp.repositories.RecipeRepository;
import guru.springframework.recipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientCommandToIngredientConverter commandToIngredientConverter;
    private final IngredientToIngredientCommandConverter ingredientToIngredientCommandConverter;

    public IngredientServiceImpl(RecipeRepository recipeRepository,
                                 UnitOfMeasureRepository unitOfMeasureRepository,
                                 IngredientCommandToIngredientConverter commandToIngredientConverter,
                                 IngredientToIngredientCommandConverter ingredientToIngredientCommandConverter) {
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.commandToIngredientConverter = commandToIngredientConverter;
        this.ingredientToIngredientCommandConverter = ingredientToIngredientCommandConverter;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();

        IngredientCommand ingredientCommand = ingredientToIngredientCommandConverter.convert(
                recipe.getIngredients()
                        .stream()
                        .filter(ingredient -> ingredient.getId().equals(ingredientId))
                        .findFirst().orElseThrow()
        );
        return ingredientCommand;
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {
        Recipe recipe = recipeRepository.findById(ingredientCommand.getRecipeId()).orElseThrow();

        Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst();

        if (ingredientOptional.isPresent()){
            Ingredient ingredientFound = ingredientOptional.get();
            ingredientFound.setDescription(ingredientCommand.getDescription());
            ingredientFound.setAmount(ingredientCommand.getAmount());
            ingredientFound.setUom(unitOfMeasureRepository
                    .findById(ingredientCommand.getUomCommand().getId())
                    .orElseThrow(() -> new RuntimeException("UOM not found")));
        } else {
            // Add new Ingredient
            recipe.addIngredient(commandToIngredientConverter.convert(ingredientCommand));
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

        Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients()
                .stream()
                .filter(recipeIngredient -> recipeIngredient.getId().equals(ingredientCommand.getId()))
                .findFirst();

        if (savedIngredientOptional.isEmpty()) {
            savedIngredientOptional = savedRecipe.getIngredients()
                    .stream()
                    .filter(recipeIngredient -> recipeIngredient.getDescription().equals(ingredientCommand.getDescription()))
                    .filter(recipeIngredient -> recipeIngredient.getUom().getId().equals(ingredientCommand.getUomCommand().getId()))
                    .filter(recipeIngredient -> recipeIngredient.getAmount().equals(ingredientCommand.getAmount()))
                    .findFirst();
        }
        return ingredientToIngredientCommandConverter.convert(savedIngredientOptional.orElseThrow());
    }

    @Override
    public void deleteById(String recipeId, String ingredientId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();

        Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .findFirst();

        if (ingredientOptional.isPresent()) {
            Ingredient ingredientToDelete = ingredientOptional.get();
//            ingredientToDelete.setRecipe(null);
            recipe.getIngredients().remove(ingredientToDelete);
            recipeRepository.save(recipe);
        }
        else {
            log.debug("Ingredient not found");
        }
    }
}

package guru.springframework.recipeapp.services;

import guru.springframework.recipeapp.commands.IngredientCommand;
import guru.springframework.recipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.recipeapp.converters.IngredientCommandToIngredientConverter;
import guru.springframework.recipeapp.converters.IngredientToIngredientCommandConverter;
import guru.springframework.recipeapp.exceptions.NotFoundException;
import guru.springframework.recipeapp.model.Ingredient;
import guru.springframework.recipeapp.model.Recipe;
import guru.springframework.recipeapp.repositories.RecipeRepository;
import guru.springframework.recipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

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
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(
                () -> new NotFoundException("Recipe not found. ID: " + recipeId)
        );

        IngredientCommand ingredientCommand = ingredientToIngredientCommandConverter.convert(
                recipe.getIngredients()
                        .stream()
                        .filter(ingredient -> ingredient.getId().equals(ingredientId))
                        .findFirst().orElseThrow(
                                () -> new NotFoundException("Ingredient not found. ID: " + ingredientId)
                        )
        );
        assert ingredientCommand != null;
        ingredientCommand.setRecipeId(recipeId);
        return ingredientCommand;
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {
        Recipe recipe = recipeRepository.findById(ingredientCommand.getRecipeId()).orElseThrow(
                () -> new NotFoundException("Recipe not found. ID: " + ingredientCommand.getRecipeId())
        );

        Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst();
        log.debug("ingredientOptional.isPresent(): " + ingredientOptional.isPresent());

        if (ingredientOptional.isPresent()){
            Ingredient ingredientFound = ingredientOptional.get();
            ingredientFound.setDescription(ingredientCommand.getDescription());
            ingredientFound.setAmount(ingredientCommand.getAmount());
            ingredientFound.setUom(unitOfMeasureRepository
                    .findById(ingredientCommand.getUom().getId())
                    .orElseThrow(
                            () -> new RuntimeException("Bad UOM id. Are you fiddling with something there?")
                    )
            );
        } else {
            // Add new Ingredient
            Ingredient ingredient = commandToIngredientConverter.convert(ingredientCommand);
            ingredient.setId(UUID.randomUUID().toString());
            recipe.addIngredient(ingredient);
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

        Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients()
                .stream()
                .filter(recipeIngredient -> recipeIngredient.getId().equals(ingredientCommand.getId()))
                .findFirst();

        // Search using ingredient data, because ingredient may have changed id during saving ¯\_(ツ)_/¯
        if (savedIngredientOptional.isEmpty()) {
            savedIngredientOptional = savedRecipe.getIngredients()
                    .stream()
                    .filter(recipeIngredient -> recipeIngredient.getDescription().equals(ingredientCommand.getDescription()))
                    .filter(recipeIngredient -> recipeIngredient.getUom().getId().equals(ingredientCommand.getUom().getId()))
                    .filter(recipeIngredient -> recipeIngredient.getAmount().equals(ingredientCommand.getAmount()))
                    .findFirst();
        }

        // Enhance with id value
        IngredientCommand ingredientCommandSaved;
        if (savedIngredientOptional.isPresent()) {
            ingredientCommandSaved = ingredientToIngredientCommandConverter.convert(savedIngredientOptional.get());
            assert ingredientCommandSaved != null;
            ingredientCommandSaved.setRecipeId(recipe.getId());
        } else {
            throw new NotFoundException("Unable to identify given ingredient after it being saved.");
        }

        return ingredientCommandSaved;
    }

    @Override
    public void deleteById(String recipeId, String ingredientId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(
                () -> new NotFoundException("Recipe not found. ID: " + recipeId)
        );

        Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .findFirst();

        if (ingredientOptional.isPresent()) {
            Ingredient ingredientToDelete = ingredientOptional.get();
            recipe.getIngredients().remove(ingredientToDelete);
            recipeRepository.save(recipe);
        }
        else {
            log.debug("Ingredient not found");
            throw new NotFoundException("Ingredient not found. ID: " + ingredientId);
        }
    }
}

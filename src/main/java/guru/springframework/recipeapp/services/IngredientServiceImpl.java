package guru.springframework.recipeapp.services;

import guru.springframework.recipeapp.commands.IngredientCommand;
import guru.springframework.recipeapp.converters.IngredientCommandToIngredientConverter;
import guru.springframework.recipeapp.converters.IngredientToIngredientCommandConverter;
import guru.springframework.recipeapp.exceptions.NotFoundException;
import guru.springframework.recipeapp.model.Ingredient;
import guru.springframework.recipeapp.model.Recipe;
import guru.springframework.recipeapp.model.UnitOfMeasure;
import guru.springframework.recipeapp.repositories.reactive.RecipeReactiveRepository;
import guru.springframework.recipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final RecipeReactiveRepository recipeReactiveRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    private final IngredientCommandToIngredientConverter commandToIngredientConverter;
    private final IngredientToIngredientCommandConverter ingredientToIngredientCommandConverter;

    public IngredientServiceImpl(RecipeReactiveRepository recipeReactiveRepository,
                                 UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository,
                                 IngredientCommandToIngredientConverter commandToIngredientConverter,
                                 IngredientToIngredientCommandConverter ingredientToIngredientCommandConverter) {
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
        this.commandToIngredientConverter = commandToIngredientConverter;
        this.ingredientToIngredientCommandConverter = ingredientToIngredientCommandConverter;
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        return recipeReactiveRepository
                .findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
                .single()
                .map(ingredient -> {
                    IngredientCommand command = ingredientToIngredientCommandConverter.convert(ingredient);
                    command.setRecipeId(recipeId);
                    return command;
                });
    }

    @Override
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand ingredientCommand) {
        return recipeReactiveRepository.findById(ingredientCommand.getRecipeId())
                .doOnError((e) -> {
                    throw new NotFoundException("Recipe not found. ID: " + ingredientCommand.getRecipeId());
                })
                .map(recipe -> {
                    prepareSavingOfIngredient(
                            ingredientCommand,
                            recipe);

                    Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();

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
                    if (savedIngredientOptional.isEmpty()) {
                        throw new NotFoundException("Unable to identify given ingredient after it being saved.");
                    }
                    IngredientCommand ingredientCommandSaved = ingredientToIngredientCommandConverter.convert(savedIngredientOptional.get());
                    assert ingredientCommandSaved != null;
                    ingredientCommandSaved.setRecipeId(recipe.getId());

                    return ingredientCommandSaved;
                });
    }

    private void prepareSavingOfIngredient(IngredientCommand ingredientCommand, Recipe recipe) {
        Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst();

        if (ingredientOptional.isPresent()){
            // Case update ingredient
            log.debug("Updating ingredient");
            Ingredient ingredientFound = ingredientOptional.get();
            ingredientFound.setDescription(ingredientCommand.getDescription());
            ingredientFound.setAmount(ingredientCommand.getAmount());

            UnitOfMeasure uomIngredientFound = unitOfMeasureReactiveRepository
                    .findById(ingredientCommand.getUom().getId()).block();
            if (uomIngredientFound == null) {
                throw new RuntimeException("Bad UOM id. Are you fiddling with something there?");
            }
            ingredientFound.setUom(uomIngredientFound);
        } else {
            // Case add new Ingredient
            log.debug("Adding new ingredient");
            Ingredient ingredient = commandToIngredientConverter.convert(ingredientCommand);
            ingredient.setId(UUID.randomUUID().toString());
            recipe.addIngredient(ingredient);
        }
    }

    @Override
    public Mono<Void> deleteById(String recipeId, String ingredientId) {
        Recipe recipe = recipeReactiveRepository.findById(recipeId)
                .blockOptional()
                .orElseThrow(
                        () -> new NotFoundException("Recipe not found. ID: " + recipeId)
                );

        Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .findFirst();

        if (ingredientOptional.isPresent()) {
            Ingredient ingredientToDelete = ingredientOptional.get();
            recipe.getIngredients().remove(ingredientToDelete);
            recipeReactiveRepository.save(recipe).block();
        }
        else {
            log.debug("Ingredient not found");
            throw new NotFoundException("Ingredient not found. ID: " + ingredientId);
        }
        return Mono.empty();
    }
}

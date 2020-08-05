package guru.springframework.recipeapp.services;

import guru.springframework.recipeapp.commands.RecipeCommand;
import guru.springframework.recipeapp.converters.RecipeCommandToRecipeConverter;
import guru.springframework.recipeapp.converters.RecipeToRecipeCommandConverter;
import guru.springframework.recipeapp.exceptions.NotFoundException;
import guru.springframework.recipeapp.model.Recipe;
import guru.springframework.recipeapp.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeCommandToRecipeConverter commandToRecipeConverter;
    private final RecipeToRecipeCommandConverter recipeToRecipeCommandConverter;

    public RecipeServiceImpl(RecipeReactiveRepository recipeReactiveRepository, RecipeCommandToRecipeConverter commandToRecipeConverter, RecipeToRecipeCommandConverter recipeToRecipeCommandConverter) {
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.commandToRecipeConverter = commandToRecipeConverter;
        this.recipeToRecipeCommandConverter = recipeToRecipeCommandConverter;
    }

    @Override
    public Flux<Recipe> getRecipes() {
        log.debug("Fetching recipes");
        return recipeReactiveRepository.findAll();
    }

    @Override
    public Mono<Recipe> findById(String id) {
        return Mono.just(recipeReactiveRepository.findById(id).blockOptional().orElseThrow(
                () -> new NotFoundException("Recipe not found. ID: " + id)
        ));
    }

    @Override
    public Mono<RecipeCommand> findCommandById(String id) {
        return recipeReactiveRepository.findById(id)
                .map(recipe -> {
                    RecipeCommand command = recipeToRecipeCommandConverter.convert(recipe);
                    assert command != null;
                    if (command.getIngredients() != null) {
                        command.getIngredients().forEach(i -> i.setRecipeId(command.getId()));
                    }
                    return command;
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        log.debug("Deleting recipe, id " + id);
        try {
            recipeReactiveRepository.deleteById(id).block();
        } catch (NullPointerException e) {
            throw new NotFoundException("Recipe not found. ID: " + id);
        }
        log.debug("Deleted");
        return Mono.empty();
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) {
        log.debug("Saving recipe, id: " + command.getId());
        return recipeReactiveRepository.save(commandToRecipeConverter.convert(command))
                .map(recipeToRecipeCommandConverter::convert);
    }
}

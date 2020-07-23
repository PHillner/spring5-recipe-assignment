package guru.springframework.recipeapp.services;

import guru.springframework.recipeapp.commands.RecipeCommand;
import guru.springframework.recipeapp.converters.RecipeCommandToRecipeConverter;
import guru.springframework.recipeapp.converters.RecipeToRecipeCommandConverter;
import guru.springframework.recipeapp.exceptions.NotFoundException;
import guru.springframework.recipeapp.model.Recipe;
import guru.springframework.recipeapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipeConverter commandToRecipeConverter;
    private final RecipeToRecipeCommandConverter recipeToRecipeCommandConverter;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipeConverter commandToRecipeConverter, RecipeToRecipeCommandConverter recipeToRecipeCommandConverter) {
        this.recipeRepository = recipeRepository;
        this.commandToRecipeConverter = commandToRecipeConverter;
        this.recipeToRecipeCommandConverter = recipeToRecipeCommandConverter;
    }

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("Fetching recipes");
        Set<Recipe> recipeSet = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
        return recipeSet;
    }

    @Override
    public Recipe findById(Long id) {
        return recipeRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Recipe not found. ID: " + id.toString())
        );
    }

    @Override
    @Transactional
    public RecipeCommand findCommandById(Long id) {
        return recipeToRecipeCommandConverter.convert(findById(id));
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting recipe, id " + id);
        recipeRepository.deleteById(id);
        log.debug("Deleted");
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {
        Recipe savedRecipe = recipeRepository.save(commandToRecipeConverter.convert(command));
        log.debug("Saved recipe, id: " + savedRecipe.getId());
        return recipeToRecipeCommandConverter.convert(savedRecipe);
    }
}

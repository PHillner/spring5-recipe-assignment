package guru.springframework.recipeapp.services;

import guru.springframework.recipeapp.model.Recipe;

public interface RecipeService {

    Iterable<Recipe> findAll();

    Recipe findById(Long id);

    Recipe save(Recipe recipe);

    void deleteById(Long id);

    void delete(Recipe recipe);
}

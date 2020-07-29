package guru.springframework.recipeapp.services;

import guru.springframework.recipeapp.converters.RecipeCommandToRecipeConverter;
import guru.springframework.recipeapp.converters.RecipeToRecipeCommandConverter;
import guru.springframework.recipeapp.exceptions.NotFoundException;
import guru.springframework.recipeapp.model.Recipe;
import guru.springframework.recipeapp.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    RecipeCommandToRecipeConverter commandToRecipeConverter;

    RecipeToRecipeCommandConverter recipeToRecipeCommandConverter;

    @Mock
    RecipeRepository recipeRepository;


    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        recipeService = new RecipeServiceImpl(recipeRepository, commandToRecipeConverter, recipeToRecipeCommandConverter);
    }

    @Test
    public void testGetRecipes() throws Exception {

        Recipe recipe = new Recipe();
        Set<Recipe> receipesData = new HashSet<>();
        receipesData.add(recipe);

        when(recipeRepository.findAll()).thenReturn(receipesData);

        Set<Recipe> recipes = recipeService.getRecipes();

        assertEquals(recipes.size(), 1);
        verify(recipeRepository).findAll();
    }

    @Test
    public void testFindById() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setId("1");
        Optional<Recipe> optRecipe = Optional.of(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(optRecipe);

        Recipe returnedRecipe = recipeService.findById("1");

        assertNotNull(returnedRecipe, "Null recipe returned");
        verify(recipeRepository).findById(anyString());
    }

    @Test
    public void failFindById() throws Exception {
        Optional<Recipe> recipeOptional = Optional.empty();

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        NotFoundException notFoundException = assertThrows(
                NotFoundException.class,
                () -> recipeService.findById("1"),
                "Expected NotFoundException, but got something else."
        );
        assertFalse(notFoundException.getMessage().isEmpty());
    }

    @Test
    public void testDeleteById() throws Exception {

        recipeService.deleteById("2");

        verify(recipeRepository).deleteById(anyString());
    }

}

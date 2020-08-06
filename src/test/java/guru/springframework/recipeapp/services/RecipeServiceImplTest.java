package guru.springframework.recipeapp.services;

import guru.springframework.recipeapp.commands.RecipeCommand;
import guru.springframework.recipeapp.converters.*;
import guru.springframework.recipeapp.exceptions.NotFoundException;
import guru.springframework.recipeapp.model.Recipe;
import guru.springframework.recipeapp.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    public static final String RECIPE_ID = "3";
    public static final String RECIPE_FAIL_ID = "7";

    RecipeServiceImpl recipeService;

    RecipeCommandToRecipeConverter commandToRecipeConverter;

    RecipeToRecipeCommandConverter recipeToRecipeCommandConverter;

    @Mock
    RecipeReactiveRepository recipeReactiveRepository;

    public RecipeServiceImplTest() {
        this.commandToRecipeConverter = new RecipeCommandToRecipeConverter(
                new IngredientCommandToIngredientConverter(new UomCommandToUnitOfMeasureConverter()),
                new NotesCommandToNotesConverter(),
                new CategoryCommandToCategoryConverter());
        this.recipeToRecipeCommandConverter = new RecipeToRecipeCommandConverter(
                new IngredientToIngredientCommandConverter(new UnitOfMeasureToUomCommandConverter()),
                new NotesToNotesCommandConverter(),
                new CategoryToCategoryCommandConverter()
        );
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        recipeService = new RecipeServiceImpl(recipeReactiveRepository, commandToRecipeConverter, recipeToRecipeCommandConverter);
    }

    @Test
    public void testGetRecipes() {
        Recipe recipe = new Recipe();

        when(recipeReactiveRepository.findAll()).thenReturn(Flux.just(recipe));

        List<Recipe> recipes = recipeService.getRecipes().collectList().block();

        assertEquals(recipes.size(), 1);
        verify(recipeReactiveRepository).findAll();
    }

    @Test
    public void testFindById() {
        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        Recipe returnedRecipe = recipeService.findById("1").block();

        assertNotNull(returnedRecipe, "Null recipe returned");
        verify(recipeReactiveRepository).findById(anyString());
    }

    @Test
    public void failFindById() {
        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.empty());

        assertEquals(Mono.empty(), recipeService.findById("1"));
    }

    @Test
    public void testDeleteById() {
        when(recipeReactiveRepository.deleteById(anyString())).thenReturn(Mono.empty());

        recipeService.deleteById("2");

        verify(recipeReactiveRepository).deleteById(anyString());
    }

    @Test
    public void failDeleteById() {
        NotFoundException notFoundException = assertThrows(
                NotFoundException.class,
                () -> recipeService.deleteById("2"),
                "Expected NotFoundException, but got something else."
        );
        assertFalse(notFoundException.getMessage().isEmpty());

        verify(recipeReactiveRepository).deleteById(anyString());
    }

    @Test
    public void testSaveRecipeCommand() {
        Recipe savedRecipe = new Recipe();
        savedRecipe.setId(RECIPE_ID);

        RecipeCommand command = new RecipeCommand();
        command.setId(RECIPE_ID);

        when(recipeReactiveRepository.save(any())).thenReturn(Mono.just(savedRecipe));

        recipeService.saveRecipeCommand(command);

        verify(recipeReactiveRepository).save(any());
    }

    @Test
    public void failSaveRecipeCommand() {
        RecipeCommand command = new RecipeCommand();
        command.setId(RECIPE_ID);

        when(recipeReactiveRepository.save(any())).thenReturn(Mono.empty());

        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command).block();

        verify(recipeReactiveRepository).save(any());
        assertNull(savedCommand);
    }

}

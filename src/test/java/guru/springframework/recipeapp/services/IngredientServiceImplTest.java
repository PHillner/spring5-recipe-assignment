package guru.springframework.recipeapp.services;

import guru.springframework.recipeapp.commands.IngredientCommand;
import guru.springframework.recipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.recipeapp.converters.IngredientCommandToIngredientConverter;
import guru.springframework.recipeapp.converters.IngredientToIngredientCommandConverter;
import guru.springframework.recipeapp.converters.UnitOfMeasureToUomCommandConverter;
import guru.springframework.recipeapp.converters.UomCommandToUnitOfMeasureConverter;
import guru.springframework.recipeapp.model.Ingredient;
import guru.springframework.recipeapp.model.Recipe;
import guru.springframework.recipeapp.repositories.reactive.RecipeReactiveRepository;
import guru.springframework.recipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class IngredientServiceImplTest {

    IngredientServiceImpl ingredientService;

    @Mock
    RecipeReactiveRepository recipeReactiveRepository;

    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    private final IngredientToIngredientCommandConverter ingredientToIngredientCommandConverter;
    private final IngredientCommandToIngredientConverter commandToIngredientConverter;

    static String RECIPE_ID = "1";
    static String INGREDIENT_ID_1 = "1";
    static String INGREDIENT_ID_2 = "2";
    static String INGREDIENT_ID_3 = "3";

    IngredientServiceImplTest() {
        this.ingredientToIngredientCommandConverter = new IngredientToIngredientCommandConverter(new UnitOfMeasureToUomCommandConverter());
        this.commandToIngredientConverter = new IngredientCommandToIngredientConverter(new UomCommandToUnitOfMeasureConverter());
    }


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ingredientService = new IngredientServiceImpl(recipeReactiveRepository, unitOfMeasureReactiveRepository, commandToIngredientConverter, ingredientToIngredientCommandConverter);
    }

    @Test
    void testFindByRecipeIdAndId() {
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(INGREDIENT_ID_1);
        recipe.addIngredient(ingredient1);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(INGREDIENT_ID_2);
        recipe.addIngredient(ingredient2);

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId(INGREDIENT_ID_3);
        recipe.addIngredient(ingredient3);

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        IngredientCommand command = ingredientService.findByRecipeIdAndIngredientId(RECIPE_ID, INGREDIENT_ID_3).block();

        assertNotNull(command);
        assertEquals(RECIPE_ID, command.getRecipeId());
        assertEquals(INGREDIENT_ID_3, command.getId());
        verify(recipeReactiveRepository).findById(anyString());
    }

    @Test
    void testSaveIngredientCommand() {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID_1);
        ingredientCommand.setRecipeId(RECIPE_ID);

        Recipe savedRecipe = new Recipe();
        Ingredient ingredientInSavedRecipe = new Ingredient();
        ingredientInSavedRecipe.setId(INGREDIENT_ID_1);
        savedRecipe.addIngredient(ingredientInSavedRecipe);

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(new Recipe()));
        when(recipeReactiveRepository.save(any())).thenReturn(Mono.just(savedRecipe));

        IngredientCommand savedIngredientCommand = ingredientService.saveIngredientCommand(ingredientCommand).block();

        assertNotNull(savedIngredientCommand);
        assertEquals(INGREDIENT_ID_1, savedIngredientCommand.getId());
        verify(recipeReactiveRepository).findById(anyString());
        verify(recipeReactiveRepository).save(any(Recipe.class));
    }

    @Test
    void failSaveIngredientCommandByBadUom() {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID_1);
        ingredientCommand.setRecipeId(RECIPE_ID);
        UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId("5");
        ingredientCommand.setUom(uomCommand);

        Recipe savedRecipe = new Recipe();
        Ingredient ingredientInSavedRecipe = new Ingredient();
        ingredientInSavedRecipe.setId(INGREDIENT_ID_1);
        savedRecipe.addIngredient(ingredientInSavedRecipe);

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(savedRecipe));
        when(recipeReactiveRepository.save(any())).thenReturn(Mono.just(savedRecipe));
        when(unitOfMeasureReactiveRepository.findById(anyString())).thenReturn(Mono.empty());

        RuntimeException runtimeException = assertThrows(
                RuntimeException.class,
                () -> ingredientService.saveIngredientCommand(ingredientCommand).block(),
                "Expected RuntimeException, but got something else."
        );

        assertNotNull(runtimeException);
        verify(recipeReactiveRepository).findById(anyString());
        verify(recipeReactiveRepository, times(0)).save(any(Recipe.class));
    }

    @Test
    void testDeleteById() {
        Recipe recipe = new Recipe();
        Ingredient ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID_1);
        recipe.addIngredient(ingredient);

        when(recipeReactiveRepository.findById(RECIPE_ID)).thenReturn(Mono.just(recipe));
        when(recipeReactiveRepository.save(any())).thenReturn(Mono.just(recipe));

        ingredientService.deleteById(RECIPE_ID, INGREDIENT_ID_1);

        verify(recipeReactiveRepository).findById(anyString());
        verify(recipeReactiveRepository).save(any(Recipe.class));
    }
}

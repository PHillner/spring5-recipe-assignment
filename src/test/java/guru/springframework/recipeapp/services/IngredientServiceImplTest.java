package guru.springframework.recipeapp.services;

import guru.springframework.recipeapp.commands.IngredientCommand;
import guru.springframework.recipeapp.converters.IngredientCommandToIngredientConverter;
import guru.springframework.recipeapp.converters.IngredientToIngredientCommandConverter;
import guru.springframework.recipeapp.converters.UnitOfMeasureToUomCommandConverter;
import guru.springframework.recipeapp.converters.UomCommandToUnitOfMeasureConverter;
import guru.springframework.recipeapp.model.Ingredient;
import guru.springframework.recipeapp.model.Recipe;
import guru.springframework.recipeapp.repositories.RecipeRepository;
import guru.springframework.recipeapp.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IngredientServiceImplTest {

    IngredientServiceImpl ingredientService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    private final IngredientToIngredientCommandConverter ingredientToIngredientCommandConverter;
    private final IngredientCommandToIngredientConverter commandToIngredientConverter;

    static Long RECIPE_ID = 1L;
    static Long INGREDIENT_ID_1 = 1L;
    static Long INGREDIENT_ID_2 = 2L;
    static Long INGREDIENT_ID_3 = 3L;

    IngredientServiceImplTest() {
        this.ingredientToIngredientCommandConverter = new IngredientToIngredientCommandConverter(new UnitOfMeasureToUomCommandConverter());
        this.commandToIngredientConverter = new IngredientCommandToIngredientConverter(new UomCommandToUnitOfMeasureConverter());
    }


    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ingredientService = new IngredientServiceImpl(recipeRepository, unitOfMeasureRepository, commandToIngredientConverter, ingredientToIngredientCommandConverter);
    }

    @Test
    void findByRecipeIdAndId() {
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

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        IngredientCommand command = ingredientService.findByRecipeIdAndIngredientId(RECIPE_ID, INGREDIENT_ID_3);

        assertNotNull(command);
        assertEquals(RECIPE_ID, command.getRecipeId());
        assertEquals(INGREDIENT_ID_3, command.getId());
        verify(recipeRepository).findById(anyLong());
    }

    @Test
    void saveIngredientCommand() {
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(INGREDIENT_ID_1);
        recipe.addIngredient(ingredient1);

        IngredientCommand command = new IngredientCommand();
        command.setId(INGREDIENT_ID_1);
        command.setRecipeId(RECIPE_ID);

        Optional<Recipe> recipeOptional = Optional.of(new Recipe());

        Recipe savedRecipe = new Recipe();
        Ingredient ingredientInSavedRecipe = new Ingredient();
        ingredientInSavedRecipe.setId(INGREDIENT_ID_1);
        savedRecipe.addIngredient(ingredientInSavedRecipe);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);

        IngredientCommand savedIngredientCommand = ingredientService.saveIngredientCommand(command);

        assertEquals(INGREDIENT_ID_1, savedIngredientCommand.getId());
        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    void testDeleteById() {
        Recipe recipe = new Recipe();
        Ingredient ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID_1);
        recipe.addIngredient(ingredient);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(RECIPE_ID)).thenReturn(recipeOptional);

        ingredientService.deleteById(RECIPE_ID, INGREDIENT_ID_1);

        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository).save(any(Recipe.class));
    }
}

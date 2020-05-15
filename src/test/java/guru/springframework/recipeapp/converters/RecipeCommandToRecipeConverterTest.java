package guru.springframework.recipeapp.converters;

import guru.springframework.recipeapp.commands.CategoryCommand;
import guru.springframework.recipeapp.commands.IngredientCommand;
import guru.springframework.recipeapp.commands.NotesCommand;
import guru.springframework.recipeapp.commands.RecipeCommand;
import guru.springframework.recipeapp.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeCommandToRecipeConverterTest {

    public static final Long RECIPE_ID = 1L;
    public static final String DESCRIPTION = "recipe description";
    public static final Integer PREP_TIME = 5;
    public static final Integer COOK_TIME = 10;
    public static final Integer SERVINGS = 2;
    public static final String SOURCE = "recipe source";
    public static final String URL = "recipe url";
    public static final String DIRECTIONS = "recipe directions";
    public static final Difficulty DIFFICULTY = Difficulty.HARD;
    public static final Long INGREDIENT_ID_1 = 1L;
    public static final Long INGREDIENT_ID_2 = 2L;
    public static final Long NOTES_ID = 1L;
    public static final Long CATEGORY_ID = 1L;

    RecipeCommandToRecipeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new RecipeCommandToRecipeConverter(
                new IngredientCommandToIngredientConverter(new UomCommandToUnitOfMeasureConverter()),
                new NotesCommandToNotesConverter(),
                new CategoryCommandToCategoryConverter()
        );
    }

    @Test
    void testNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObject() {
        assertNotNull(converter.convert(new RecipeCommand()));
    }

    @Test
    void convert() {
        RecipeCommand command = new RecipeCommand();
        command.setId(RECIPE_ID);
        command.setDescription(DESCRIPTION);
        command.setCookTime(COOK_TIME);
        command.setPrepTime(PREP_TIME);
        command.setServings(SERVINGS);
        command.setSource(SOURCE);
        command.setUrl(URL);
        command.setDirections(DIRECTIONS);
        command.setDifficulty(DIFFICULTY);

        IngredientCommand i1 = new IngredientCommand();
        i1.setId(INGREDIENT_ID_1);
        IngredientCommand i2 = new IngredientCommand();
        i2.setId(INGREDIENT_ID_2);
        command.getIngredients().add(i1);
        command.getIngredients().add(i2);

        NotesCommand notes = new NotesCommand();
        notes.setId(NOTES_ID);
        command.setNotes(notes);

        CategoryCommand category = new CategoryCommand();
        category.setId(CATEGORY_ID);
        command.getCategories().add(category);

        Recipe recipe = converter.convert(command);

        assertNotNull(recipe);
        assertEquals(RECIPE_ID, recipe.getId());
        assertEquals(DESCRIPTION, recipe.getDescription());
        assertEquals(PREP_TIME, recipe.getPrepTime());
        assertEquals(COOK_TIME, recipe.getCookTime());
        assertEquals(SERVINGS, recipe.getServings());
        assertEquals(SOURCE, recipe.getSource());
        assertEquals(URL, recipe.getUrl());
        assertEquals(DIRECTIONS, recipe.getDirections());
        assertEquals(DIFFICULTY, recipe.getDifficulty());
        assertEquals(2, recipe.getIngredients().size());
        assertEquals(NOTES_ID, recipe.getNotes().getId());
        assertEquals(1, recipe.getCategories().size());
    }
}

package guru.springframework.recipeapp.converters;

import guru.springframework.recipeapp.commands.RecipeCommand;
import guru.springframework.recipeapp.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeToRecipeCommandConverterTest {

    public static final String RECIPE_ID = "1";
    public static final String DESCRIPTION = "recipe description";
    public static final Integer PREP_TIME = 5;
    public static final Integer COOK_TIME = 10;
    public static final Integer SERVINGS = 2;
    public static final String SOURCE = "recipe source";
    public static final String URL = "recipe url";
    public static final String DIRECTIONS = "recipe directions";
    public static final Difficulty DIFFICULTY = Difficulty.HARD;
    public static final String INGREDIENT_ID_1 = "1";
    public static final String INGREDIENT_ID_2 = "2";
    public static final String NOTES_ID = "1";
    public static final String CATEGORY_ID = "1";

    RecipeToRecipeCommandConverter converter;

    @BeforeEach
    void setUp() {
        converter = new RecipeToRecipeCommandConverter(
                new IngredientToIngredientCommandConverter(new UnitOfMeasureToUomCommandConverter()),
                new NotesToNotesCommandConverter(),
                new CategoryToCategoryCommandConverter()
        );
    }

    @Test
    void testNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObject() {
        assertNotNull(converter.convert(new Recipe()));
    }

    @Test
    void convert() {
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.setDescription(DESCRIPTION);
        recipe.setCookTime(COOK_TIME);
        recipe.setPrepTime(PREP_TIME);
        recipe.setServings(SERVINGS);
        recipe.setSource(SOURCE);
        recipe.setUrl(URL);
        recipe.setDirections(DIRECTIONS);
        recipe.setDifficulty(DIFFICULTY);

        Ingredient i1 = new Ingredient();
        i1.setId(INGREDIENT_ID_1);
        Ingredient i2 = new Ingredient();
        i2.setId(INGREDIENT_ID_2);
        recipe.getIngredients().add(i1);
        recipe.getIngredients().add(i2);

        Notes notes = new Notes();
        notes.setId(NOTES_ID);
        recipe.setNotes(notes);

        Category category = new Category();
        category.setId(CATEGORY_ID);
        recipe.getCategories().add(category);

        RecipeCommand command = converter.convert(recipe);

        assertNotNull(command);
        assertEquals(RECIPE_ID, command.getId());
        assertEquals(DESCRIPTION, command.getDescription());
        assertEquals(PREP_TIME, command.getPrepTime());
        assertEquals(COOK_TIME, command.getCookTime());
        assertEquals(SERVINGS, command.getServings());
        assertEquals(SOURCE, command.getSource());
        assertEquals(URL, command.getUrl());
        assertEquals(DIRECTIONS, command.getDirections());
        assertEquals(DIFFICULTY, command.getDifficulty());
        assertEquals(2, command.getIngredients().size());
        assertEquals(NOTES_ID, command.getNotes().getId());
        assertEquals(1, command.getCategories().size());
    }
}

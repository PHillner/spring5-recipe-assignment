package guru.springframework.recipeapp.converters;

import guru.springframework.recipeapp.commands.IngredientCommand;
import guru.springframework.recipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.recipeapp.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientCommandToIngredientConverterTest {

    public static final String ID_INGREDIENT_VALUE = "1";
    public static final String DESCRIPTION_INGREDIENT_VALUE = "ingredient description";
    public static final BigDecimal AMOUNT = BigDecimal.ONE;
    public static final String UOM_ID = "2";

    IngredientCommandToIngredientConverter converter;

    @BeforeEach
    void setUp() {
        converter = new IngredientCommandToIngredientConverter(new UomCommandToUnitOfMeasureConverter());
    }

    @Test
    void testNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObject() {
        assertNotNull(converter.convert(new IngredientCommand()));
    }

    @Test
    void convert() {
        IngredientCommand command = new IngredientCommand();
        command.setId(ID_INGREDIENT_VALUE);
        command.setDescription(DESCRIPTION_INGREDIENT_VALUE);
        command.setAmount(AMOUNT);
        UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId(UOM_ID);
        command.setUom(uomCommand);

        Ingredient ingredient = converter.convert(command);

        assertNotNull(ingredient);
        assertEquals(ID_INGREDIENT_VALUE, ingredient.getId());
        assertEquals(DESCRIPTION_INGREDIENT_VALUE, ingredient.getDescription());
        assertEquals(AMOUNT, ingredient.getAmount());
        assertNotNull(ingredient.getUom());
        assertEquals(ingredient.getUom().getId(), UOM_ID);
    }
}

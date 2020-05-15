package guru.springframework.recipeapp.converters;

import guru.springframework.recipeapp.commands.IngredientCommand;
import guru.springframework.recipeapp.model.Ingredient;
import guru.springframework.recipeapp.model.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientToIngredientCommandConverterTest {

    public static final Long ID_INGREDIENT_VALUE = 1L;
    public static final String DESCRIPTION_INGREDIENT_VALUE = "ingredient description";
    public static final BigDecimal AMOUNT = BigDecimal.ONE;
    public static final Long UOM_ID = 2L;

    IngredientToIngredientCommandConverter converter;

    @BeforeEach
    void setUp() {
        converter = new IngredientToIngredientCommandConverter(new UnitOfMeasureToUomCommandConverter());
    }

    @Test
    void testNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObject() {
        assertNotNull(converter.convert(new Ingredient()));
    }

    @Test
    void convert() {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ID_INGREDIENT_VALUE);
        ingredient.setDescription(DESCRIPTION_INGREDIENT_VALUE);
        ingredient.setAmount(AMOUNT);
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId(UOM_ID);
        ingredient.setUom(uom);

        IngredientCommand command = converter.convert(ingredient);

        assertNotNull(command);
        assertEquals(ID_INGREDIENT_VALUE, command.getId());
        assertEquals(DESCRIPTION_INGREDIENT_VALUE, command.getDescription());
        assertEquals(AMOUNT, command.getAmount());
        assertNotNull(command.getUomCommand());
        assertEquals(command.getUomCommand().getId(), UOM_ID);
    }
}

package guru.springframework.recipeapp.converters;

import guru.springframework.recipeapp.commands.CategoryCommand;
import guru.springframework.recipeapp.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryCommandToCategoryConverterTest {

    public static final Long ID_VALUE = 1L;
    public static final String DESCRIPTION = "category description";

    CategoryCommandToCategoryConverter converter;

    @BeforeEach
    void setUp() {
        converter = new CategoryCommandToCategoryConverter();
    }

    @Test
    void testNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObject() {
        assertNotNull(converter.convert(new CategoryCommand()));
    }

    @Test
    void convert() {
        CategoryCommand command = new CategoryCommand();
        command.setId(ID_VALUE);
        command.setDescription(DESCRIPTION);

        Category category = converter.convert(command);

        assertNotNull(category);
        assertEquals(ID_VALUE, category.getId());
        assertEquals(DESCRIPTION, category.getDescription());
    }
}

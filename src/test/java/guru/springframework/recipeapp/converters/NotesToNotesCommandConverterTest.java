package guru.springframework.recipeapp.converters;

import guru.springframework.recipeapp.commands.NotesCommand;
import guru.springframework.recipeapp.model.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotesToNotesCommandConverterTest {

    public static final Long ID_VALUE = 1L;
    public static final String RECIPE_NOTES = "recipe notes";

    NotesToNotesCommandConverter converter;

    @BeforeEach
    void setUp() {
        converter = new NotesToNotesCommandConverter();
    }

    @Test
    void testNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObject() {
        assertNotNull(converter.convert(new Notes()));
    }

    @Test
    void convert() {
        Notes notes = new Notes();
        notes.setId(ID_VALUE);
        notes.setRecipeNotes(RECIPE_NOTES);

        NotesCommand command = converter.convert(notes);

        assertNotNull(command);
        assertEquals(ID_VALUE, command.getId());
        assertEquals(RECIPE_NOTES, command.getRecipeNotes());
    }
}

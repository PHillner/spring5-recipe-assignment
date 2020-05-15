package guru.springframework.recipeapp.converters;

import guru.springframework.recipeapp.commands.RecipeCommand;
import guru.springframework.recipeapp.model.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RecipeToRecipeCommandConverter implements Converter<Recipe, RecipeCommand> {

    private IngredientToIngredientCommandConverter toIngredientCommandConverter;
    private NotesToNotesCommandConverter toNotesCommandConverter;
    private CategoryToCategoryCommandConverter toCategoryCommandConverter;

    public RecipeToRecipeCommandConverter(IngredientToIngredientCommandConverter toIngredientCommandConverter,
                                          NotesToNotesCommandConverter toNotesCommandConverter,
                                          CategoryToCategoryCommandConverter toCategoryCommandConverter) {
        this.toIngredientCommandConverter = toIngredientCommandConverter;
        this.toNotesCommandConverter = toNotesCommandConverter;
        this.toCategoryCommandConverter = toCategoryCommandConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public RecipeCommand convert(Recipe source) {
        if (source == null) {
            return null;
        }

        final RecipeCommand command =  new RecipeCommand();
        command.setId(source.getId());
        command.setDescription(source.getDescription());
        command.setPrepTime(source.getPrepTime());
        command.setCookTime(source.getCookTime());
        command.setServings(source.getServings());
        command.setSource(source.getSource());
        command.setUrl(source.getUrl());
        command.setDirections(source.getDirections());
        command.setIngredients(
                source.getIngredients().stream()
                        .map(i -> toIngredientCommandConverter.convert(i)).collect(Collectors.toSet())
        );
        command.setDifficulty(source.getDifficulty());
        command.setNotes(toNotesCommandConverter.convert(source.getNotes()));
        command.setCategories(
                source.getCategories().stream()
                        .map(c -> toCategoryCommandConverter.convert(c)).collect(Collectors.toSet())
        );
        return command;
    }
}

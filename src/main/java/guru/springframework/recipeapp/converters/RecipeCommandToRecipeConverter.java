package guru.springframework.recipeapp.converters;

import guru.springframework.recipeapp.commands.RecipeCommand;
import guru.springframework.recipeapp.model.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RecipeCommandToRecipeConverter implements Converter<RecipeCommand, Recipe> {

    private IngredientCommandToIngredientConverter toIngredientConverter;
    private NotesCommandToNotesConverter toNotesConverter;
    private CategoryCommandToCategoryConverter toCategoryConverter;

    public RecipeCommandToRecipeConverter(IngredientCommandToIngredientConverter toIngredientConverter,
                                          NotesCommandToNotesConverter toNotesConverter,
                                          CategoryCommandToCategoryConverter toCategoryConverter) {
        this.toIngredientConverter = toIngredientConverter;
        this.toNotesConverter = toNotesConverter;
        this.toCategoryConverter = toCategoryConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public Recipe convert(RecipeCommand source) {
        if (source == null) {
            return null;
        }

        final Recipe recipe =  new Recipe();
        recipe.setId(source.getId());
        recipe.setDescription(source.getDescription());
        recipe.setPrepTime(source.getPrepTime());
        recipe.setCookTime(source.getCookTime());
        recipe.setServings(source.getServings());
        recipe.setSource(source.getSource());
        recipe.setUrl(source.getUrl());
        recipe.setDirections(source.getDirections());
        recipe.setIngredients(
                source.getIngredients().stream()
                        .map(i -> toIngredientConverter.convert(i)).collect(Collectors.toSet())
        );
        recipe.setImage(source.getImage());
        recipe.setDifficulty(source.getDifficulty());
        recipe.setNotes(toNotesConverter.convert(source.getNotes()));
        recipe.setCategories(
                source.getCategories().stream()
                        .map(c -> toCategoryConverter.convert(c)).collect(Collectors.toSet())
        );
        return recipe;
    }
}

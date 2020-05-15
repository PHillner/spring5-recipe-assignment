package guru.springframework.recipeapp.converters;

import guru.springframework.recipeapp.commands.IngredientCommand;
import guru.springframework.recipeapp.model.Ingredient;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientCommandToIngredientConverter implements Converter<IngredientCommand, Ingredient> {

    private UomCommandToUnitOfMeasureConverter toUnitOfMeasureConverter;

    public IngredientCommandToIngredientConverter(UomCommandToUnitOfMeasureConverter toUnitOfMeasureConverter) {
        this.toUnitOfMeasureConverter = toUnitOfMeasureConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public Ingredient convert(IngredientCommand source) {
        if (source == null){
            return null;
        }

        Ingredient ingredient = new Ingredient();
        ingredient.setId(source.getId());
        ingredient.setDescription(source.getDescription());
        ingredient.setAmount(source.getAmount());
        ingredient.setUom(toUnitOfMeasureConverter.convert(source.getUomCommand()));
        return ingredient;
    }
}

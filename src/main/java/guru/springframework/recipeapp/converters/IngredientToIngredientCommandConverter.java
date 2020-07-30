package guru.springframework.recipeapp.converters;

import guru.springframework.recipeapp.commands.IngredientCommand;
import guru.springframework.recipeapp.model.Ingredient;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientToIngredientCommandConverter implements Converter<Ingredient, IngredientCommand> {

    private UnitOfMeasureToUomCommandConverter toUomCommandConverter;

    public IngredientToIngredientCommandConverter(UnitOfMeasureToUomCommandConverter toUomCommandConverter) {
        this.toUomCommandConverter = toUomCommandConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public IngredientCommand convert(Ingredient source) {
        if (source == null){
            return null;
        }

        IngredientCommand command = new IngredientCommand();
        command.setId(source.getId());
        command.setDescription(source.getDescription());
        command.setAmount(source.getAmount());
        command.setUom(toUomCommandConverter.convert(source.getUom()));
        return command;
    }
}

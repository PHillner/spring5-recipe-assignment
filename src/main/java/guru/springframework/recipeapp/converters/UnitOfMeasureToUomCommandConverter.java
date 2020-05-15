package guru.springframework.recipeapp.converters;

import guru.springframework.recipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.recipeapp.model.UnitOfMeasure;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UnitOfMeasureToUomCommandConverter implements Converter<UnitOfMeasure, UnitOfMeasureCommand> {

    @Synchronized
    @Nullable
    @Override
    public UnitOfMeasureCommand convert(UnitOfMeasure source) {
        if (source == null) {
            return null;
        }

        final UnitOfMeasureCommand command =  new UnitOfMeasureCommand();
        command.setId(source.getId());
        command.setDescription(source.getDescription());
        return command;
    }
}

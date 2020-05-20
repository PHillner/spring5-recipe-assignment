package guru.springframework.recipeapp.services;

import guru.springframework.recipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.recipeapp.converters.UnitOfMeasureToUomCommandConverter;
import guru.springframework.recipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UnitOfMeasureRepository repository;
    private final UnitOfMeasureToUomCommandConverter unitOfMeasureToUomCommandConverter;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository repository, UnitOfMeasureToUomCommandConverter unitOfMeasureToUomCommandConverter) {
        this.repository = repository;
        this.unitOfMeasureToUomCommandConverter = unitOfMeasureToUomCommandConverter;
    }

    @Override
    public Set<UnitOfMeasureCommand> listAllUoms() {
        return StreamSupport.stream(repository.findAll()
                .spliterator(), false)
                .map(unitOfMeasureToUomCommandConverter::convert)
                .collect(Collectors.toSet());
    }
}

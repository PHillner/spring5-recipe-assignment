package guru.springframework.recipeapp.services;

import guru.springframework.recipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.recipeapp.converters.UnitOfMeasureToUomCommandConverter;
import guru.springframework.recipeapp.repositories.UnitOfMeasureRepository;
import guru.springframework.recipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UnitOfMeasureReactiveRepository repository;
    private final UnitOfMeasureToUomCommandConverter unitOfMeasureToUomCommandConverter;

    public UnitOfMeasureServiceImpl(UnitOfMeasureReactiveRepository repository, UnitOfMeasureToUomCommandConverter unitOfMeasureToUomCommandConverter) {
        this.repository = repository;
        this.unitOfMeasureToUomCommandConverter = unitOfMeasureToUomCommandConverter;
    }

    @Override
    public Flux<UnitOfMeasureCommand> listAllUoms() {
        return repository
                .findAll()
                .map(unitOfMeasureToUomCommandConverter::convert);
    }
}

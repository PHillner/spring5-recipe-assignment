package guru.springframework.recipeapp.bootstrap;

import guru.springframework.recipeapp.model.Category;
import guru.springframework.recipeapp.model.UnitOfMeasure;
import guru.springframework.recipeapp.repositories.CategoryRepository;
import guru.springframework.recipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile({"dev","prod"})
public class DataLoaderMySQL implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public DataLoaderMySQL(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (categoryRepository.count() == 0L) {
            initCategories();
        }
        if (unitOfMeasureRepository.count() == 0L) {
            initUnitOfMeasures();
        }
    }

    private void initCategories() {
        log.debug("Loading categories into database");

        Category cat1 = new Category();
        cat1.setDescription("Scandinavian");
        categoryRepository.save(cat1);

        Category cat2 = new Category();
        cat2.setDescription("French");
        categoryRepository.save(cat2);

        Category cat3 = new Category();
        cat3.setDescription("Italian");
        categoryRepository.save(cat3);

        Category cat4 = new Category();
        cat4.setDescription("Mexican");
        categoryRepository.save(cat4);

        Category cat5 = new Category();
        cat5.setDescription("Asian");
        categoryRepository.save(cat5);

        Category cat6 = new Category();
        cat6.setDescription("Fast Food");
        categoryRepository.save(cat6);
    }

    private void initUnitOfMeasures() {
        log.debug("Loading units of measure into database");

        UnitOfMeasure uof1 = new UnitOfMeasure();
        uof1.setDescription("tsp");
        unitOfMeasureRepository.save(uof1);

        UnitOfMeasure uof2 = new UnitOfMeasure();
        uof2.setDescription("tblsp");
        unitOfMeasureRepository.save(uof2);

        UnitOfMeasure uof3 = new UnitOfMeasure();
        uof3.setDescription("cup");
        unitOfMeasureRepository.save(uof3);

        UnitOfMeasure uof4 = new UnitOfMeasure();
        uof4.setDescription("pint");
        unitOfMeasureRepository.save(uof4);

        UnitOfMeasure uof5 = new UnitOfMeasure();
        uof5.setDescription("ounce");
        unitOfMeasureRepository.save(uof5);

        UnitOfMeasure uof6 = new UnitOfMeasure();
        uof6.setDescription("pinch");
        unitOfMeasureRepository.save(uof6);

        UnitOfMeasure uof7 = new UnitOfMeasure();
        uof7.setDescription("dash");
        unitOfMeasureRepository.save(uof7);

        UnitOfMeasure uof8 = new UnitOfMeasure();
        uof8.setDescription("g");
        unitOfMeasureRepository.save(uof8);

        UnitOfMeasure uof9 = new UnitOfMeasure();
        uof9.setDescription("l");
        unitOfMeasureRepository.save(uof9);

        UnitOfMeasure uof10 = new UnitOfMeasure();
        uof10.setDescription("dl");
        unitOfMeasureRepository.save(uof10);

        UnitOfMeasure uof11 = new UnitOfMeasure();
        uof11.setDescription("ml");
        unitOfMeasureRepository.save(uof11);

        UnitOfMeasure uof12 = new UnitOfMeasure();
        uof12.setDescription("pcs");
        unitOfMeasureRepository.save(uof12);

        UnitOfMeasure uof13 = new UnitOfMeasure();
        uof13.setDescription("each");
        unitOfMeasureRepository.save(uof13);
    }
}

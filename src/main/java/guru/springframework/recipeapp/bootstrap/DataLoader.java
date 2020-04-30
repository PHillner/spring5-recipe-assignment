package guru.springframework.recipeapp.bootstrap;

import guru.springframework.recipeapp.model.*;
import guru.springframework.recipeapp.repositories.CategoryRepository;
import guru.springframework.recipeapp.repositories.RecipeRepository;
import guru.springframework.recipeapp.repositories.UnitOfMeasureRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public DataLoader(CategoryRepository categoryRepository, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        recipeRepository.saveAll(getRecipes());
    }

    private List<Recipe> getRecipes() {

        List<Recipe> recipes = new ArrayList<>();

        // Get UOMs
        Optional<UnitOfMeasure> teaspoonUnitOptional = unitOfMeasureRepository.findByDescription("tsp");
        if (teaspoonUnitOptional.isEmpty()) throw new RuntimeException("Teaspoon unit not in db");
        UnitOfMeasure teaspoonUnit = teaspoonUnitOptional.get();
        Optional<UnitOfMeasure> tablespoonUnitOptional = unitOfMeasureRepository.findByDescription("tblsp");
        if (tablespoonUnitOptional.isEmpty()) throw new RuntimeException("Tablespoon unit not in db");
        UnitOfMeasure tablespoonUnit = tablespoonUnitOptional.get();
        Optional<UnitOfMeasure> dashUnitOptional = unitOfMeasureRepository.findByDescription("dash");
        if (dashUnitOptional.isEmpty()) throw new RuntimeException("Dash unit not in db");
        UnitOfMeasure dashUnit = dashUnitOptional.get();
        Optional<UnitOfMeasure> piecesUnitOptional = unitOfMeasureRepository.findByDescription("pcs");
        if (piecesUnitOptional.isEmpty()) throw new RuntimeException("Pieces unit not in db");
        UnitOfMeasure piecesUnit = piecesUnitOptional.get();

        // Get category
        Optional<Category> mexicanCategoryOptional = categoryRepository.findByDescription("Mexican");
        if (mexicanCategoryOptional.isEmpty()) throw new RuntimeException("Mexican category not in db");
        Category mexCategory = mexicanCategoryOptional.get();

        //// ---------------------------
        //// Add guacamole recipe
        Recipe guacamole = new Recipe();
        guacamole.setDescription("How to Make Perfect Guacamole Recipe");
        guacamole.setPrepTime(10);
        guacamole.setServings(2);
        guacamole.setDifficulty(Difficulty.EASY);
        guacamole.setNotes(new Notes("Be careful handling chiles if using." +
                " Wash your hands thoroughly after handling and do not touch your eyes" +
                " or the area near your eyes with your hands for several hours.", guacamole));

        guacamole.getIngredients().add(new Ingredient("ripe avocados", new BigDecimal(2), piecesUnit, guacamole));
        guacamole.getIngredients().add(new Ingredient("of salt, more to taste", new BigDecimal("0.25"), teaspoonUnit, guacamole));
        guacamole.getIngredients().add(new Ingredient("fresh lime juice or lemon juice", new BigDecimal(1), tablespoonUnit, guacamole));
        guacamole.getIngredients().add(new Ingredient("of minced red onion or thinly sliced green onion", new BigDecimal(2), tablespoonUnit, guacamole));
        guacamole.getIngredients().add(new Ingredient("serrano chiles, stems and seeds removed, minced", new BigDecimal(1), piecesUnit, guacamole));
        guacamole.getIngredients().add(new Ingredient("cilantro (leaves and tender stems), finely chopped", new BigDecimal(2), tablespoonUnit, guacamole));
        guacamole.getIngredients().add(new Ingredient("of freshly grated black pepper", new BigDecimal(1), dashUnit, guacamole));
        guacamole.getIngredients().add(new Ingredient("ripe tomato, seeds and pulp removed, chopped", new BigDecimal("0.5"), piecesUnit, guacamole));
        guacamole.getIngredients().add(new Ingredient("Red radishes or jicama, to garnish", new BigDecimal(1), piecesUnit, guacamole));
        guacamole.getIngredients().add(new Ingredient("Tortilla chips, to serve", new BigDecimal(1), piecesUnit, guacamole));

        guacamole.setDirections("1 Cut the avocado, remove flesh: Cut the avocados in half. Remove the pit. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.\n" +
                "\n" +
                "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)\n" +
                "\n" +
                "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                "\n" +
                "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                "\n" +
                "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                "\n" +
                "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.\n" +
                "\n" +
                "4 Serve: Serve immediately, or if making a few hours ahead, place plastic wrap on the surface of the guacamole and press down to cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.");

        guacamole.setSource("Simply Recipes");
        guacamole.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        guacamole.getCategories().add(mexCategory);

        recipes.add(guacamole);

        return recipes;
    }
}

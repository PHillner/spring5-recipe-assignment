package guru.springframework.recipeapp.bootstrap;

import guru.springframework.recipeapp.model.*;
import guru.springframework.recipeapp.repositories.reactive.CategoryReactiveRepository;
import guru.springframework.recipeapp.repositories.reactive.RecipeReactiveRepository;
import guru.springframework.recipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryReactiveRepository categoryRepository;
    private final RecipeReactiveRepository recipeRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureRepository;

    public DataLoader(CategoryReactiveRepository categoryRepository, RecipeReactiveRepository recipeRepository, UnitOfMeasureReactiveRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.debug("Loading data");
        loadCategories();
        loadUom();
        recipeRepository.saveAll(getRecipes()).blockFirst();
    }

    private void loadCategories(){
        Category cat1 = new Category();
        cat1.setDescription("Scandinavian");
        categoryRepository.save(cat1).block();

        Category cat2 = new Category();
        cat2.setDescription("French");
        categoryRepository.save(cat2).block();

        Category cat3 = new Category();
        cat3.setDescription("Italian");
        categoryRepository.save(cat3).block();

        Category cat4 = new Category();
        cat4.setDescription("Mexican");
        categoryRepository.save(cat4).block();

        Category cat5 = new Category();
        cat5.setDescription("Asian");
        categoryRepository.save(cat5).block();

        Category cat6 = new Category();
        cat6.setDescription("Fast Food");
        categoryRepository.save(cat6).block();
    }

    private void loadUom(){
        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setDescription("tsp");
        unitOfMeasureRepository.save(uom1).block();

        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setDescription("tblsp");
        unitOfMeasureRepository.save(uom2).block();

        UnitOfMeasure uom3 = new UnitOfMeasure();
        uom3.setDescription("cup");
        unitOfMeasureRepository.save(uom3).block();

        UnitOfMeasure uom4 = new UnitOfMeasure();
        uom4.setDescription("pint");
        unitOfMeasureRepository.save(uom4).block();

        UnitOfMeasure uom5 = new UnitOfMeasure();
        uom5.setDescription("ounce");
        unitOfMeasureRepository.save(uom5).block();

        UnitOfMeasure uom6 = new UnitOfMeasure();
        uom6.setDescription("pinch");
        unitOfMeasureRepository.save(uom6).block();

        UnitOfMeasure uom7 = new UnitOfMeasure();
        uom7.setDescription("dash");
        unitOfMeasureRepository.save(uom7).block();

        UnitOfMeasure uom8 = new UnitOfMeasure();
        uom8.setDescription("g");
        unitOfMeasureRepository.save(uom8).block();

        UnitOfMeasure uom9 = new UnitOfMeasure();
        uom9.setDescription("l");
        unitOfMeasureRepository.save(uom9).block();

        UnitOfMeasure uom10 = new UnitOfMeasure();
        uom10.setDescription("dl");
        unitOfMeasureRepository.save(uom10).block();

        UnitOfMeasure uom11 = new UnitOfMeasure();
        uom11.setDescription("ml");
        unitOfMeasureRepository.save(uom11).block();

        UnitOfMeasure uom12 = new UnitOfMeasure();
        uom12.setDescription("pcs");
        unitOfMeasureRepository.save(uom12).block();

        UnitOfMeasure uom13 = new UnitOfMeasure();
        uom13.setDescription("each");
        unitOfMeasureRepository.save(uom13).block();
    }

    private List<Recipe> getRecipes() {

        List<Recipe> recipes = new ArrayList<>();
        log.debug("Fetching units and categories to be used in recipes...");

        // Get UOMs
        UnitOfMeasure teaspoonUnit = unitOfMeasureRepository.findByDescription("tsp").block();
        if (teaspoonUnit == null) throw new RuntimeException("Teaspoon unit not in db");
        UnitOfMeasure tablespoonUnit = unitOfMeasureRepository.findByDescription("tblsp").block();
        if (tablespoonUnit == null) throw new RuntimeException("Tablespoon unit not in db");
        UnitOfMeasure cupUnit = unitOfMeasureRepository.findByDescription("cup").block();
        if (cupUnit == null) throw new RuntimeException("Cup unit not in db");
        UnitOfMeasure pintUnit = unitOfMeasureRepository.findByDescription("pint").block();
        if (pintUnit == null) throw new RuntimeException("Pint unit not in db");
        UnitOfMeasure dashUnit = unitOfMeasureRepository.findByDescription("dash").block();
        if (dashUnit == null) throw new RuntimeException("Dash unit not in db");
        UnitOfMeasure piecesUnit = unitOfMeasureRepository.findByDescription("pcs").block();
        if (piecesUnit == null) throw new RuntimeException("Pieces unit not in db");
        log.debug("Units of measure... Done");

        // Get category
        Category mexCategory = categoryRepository.findByDescription("Mexican").block();
        if (mexCategory == null) throw new RuntimeException("Mexican category not in db");
        log.debug("Categories... Done");

        log.debug("Initializing recipes...");
        //// ---------------------------
        //// Add guacamole recipe
        Recipe guacamole = new Recipe();
        guacamole.setDescription("How to Make Perfect Guacamole Recipe");
        guacamole.setPrepTime(10);
        guacamole.setServings(2);
        guacamole.setDifficulty(Difficulty.EASY);
        guacamole.setNotes(new Notes("Be careful handling chiles if using." +
                " Wash your hands thoroughly after handling and do not touch your eyes" +
                " or the area near your eyes with your hands for several hours."));

        guacamole.addIngredient(new Ingredient("ripe avocados", new BigDecimal(2), piecesUnit));
        guacamole.addIngredient(new Ingredient("of salt, more to taste", new BigDecimal("0.25"), teaspoonUnit));
        guacamole.addIngredient(new Ingredient("fresh lime juice or lemon juice", new BigDecimal(1), tablespoonUnit));
        guacamole.addIngredient(new Ingredient("of minced red onion or thinly sliced green onion", new BigDecimal(2), tablespoonUnit));
        guacamole.addIngredient(new Ingredient("serrano chiles, stems and seeds removed, minced", new BigDecimal(1), piecesUnit));
        guacamole.addIngredient(new Ingredient("cilantro (leaves and tender stems), finely chopped", new BigDecimal(2), tablespoonUnit));
        guacamole.addIngredient(new Ingredient("of freshly grated black pepper", new BigDecimal(1), dashUnit));
        guacamole.addIngredient(new Ingredient("ripe tomato, seeds and pulp removed, chopped", new BigDecimal("0.5"), piecesUnit));
        guacamole.addIngredient(new Ingredient("red radishes or jicama, to garnish", new BigDecimal(1), piecesUnit));
        guacamole.addIngredient(new Ingredient("tortilla chips, to serve", new BigDecimal(1), piecesUnit));

        guacamole.setDirections("1 Cut the avocado, remove flesh: Cut the avocados in half. Remove the pit." +
                " Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.\n" +
                "\n" +
                "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)\n" +
                "\n" +
                "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice." +
                " The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                "\n" +
                "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness." +
                " So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                "\n" +
                "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                "\n" +
                "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.\n" +
                "\n" +
                "4 Serve: Serve immediately, or if making a few hours ahead, place plastic wrap on the surface of the guacamole" +
                " and press down to cover it and to prevent air reaching it." +
                " (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.");

        guacamole.setSource("Simply Recipes");
        guacamole.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        guacamole.getCategories().add(mexCategory);

        recipes.add(guacamole);
        log.debug("Guacamole... Done");

        //// ---------------------------
        //// Add tacos recipe
        Recipe tacos = new Recipe();
        tacos.setDescription("Spicy Grilled Chicken Tacos Recipe");
        tacos.setPrepTime(20);
        tacos.setCookTime(15);
        tacos.setServings(4);
        tacos.setDifficulty(Difficulty.MODERATE);
        tacos.setNotes(new Notes("Look for ancho chile powder with the Mexican ingredients at your grocery store," +
                " on buy it online. (If you can't find ancho chili powder, you replace the ancho chili, the oregano," +
                " and the cumin with 2 1/2 tablespoons regular chili powder, though the flavor won't be quite the same.)"));

        tacos.addIngredient(new Ingredient("ancho chili powder", new BigDecimal(2), tablespoonUnit));
        tacos.addIngredient(new Ingredient("dried oregano", new BigDecimal(1), teaspoonUnit));
        tacos.addIngredient(new Ingredient("dried cumin", new BigDecimal(1), teaspoonUnit));
        tacos.addIngredient(new Ingredient("sugar", new BigDecimal(1), teaspoonUnit));
        tacos.addIngredient(new Ingredient("salt", new BigDecimal("0.5"), teaspoonUnit));
        tacos.addIngredient(new Ingredient("clove garlic, finely chopped", new BigDecimal(1), piecesUnit));
        tacos.addIngredient(new Ingredient("finely grated orange zest", new BigDecimal(1), tablespoonUnit));
        tacos.addIngredient(new Ingredient("fresh-squeezed orange juice", new BigDecimal(3), tablespoonUnit));
        tacos.addIngredient(new Ingredient("olive oil", new BigDecimal(2), tablespoonUnit));
        tacos.addIngredient(new Ingredient("skinless, boneless chicken thighs (1 1/4 pounds)", new BigDecimal(4), piecesUnit));

        tacos.addIngredient(new Ingredient("small corn tortillas", new BigDecimal(8), piecesUnit));
        tacos.addIngredient(new Ingredient("packed baby arugula (3 ounces)", new BigDecimal(3), cupUnit));
        tacos.addIngredient(new Ingredient("medium ripe avocados, sliced", new BigDecimal(2), piecesUnit));
        tacos.addIngredient(new Ingredient("radishes, thinly sliced", new BigDecimal(4), piecesUnit));
        tacos.addIngredient(new Ingredient("cherry tomatoes, halved", new BigDecimal(".5"), pintUnit));
        tacos.addIngredient(new Ingredient("red onion, thinly sliced", new BigDecimal(".25"), piecesUnit));
        tacos.addIngredient(new Ingredient("roughly chopped cilantro", new BigDecimal(1), piecesUnit));
        tacos.addIngredient(new Ingredient("sour cream thinned with 1/4 cup milk", new BigDecimal(".5"), cupUnit));
        tacos.addIngredient(new Ingredient("lime, cut into wedges", new BigDecimal(1), piecesUnit));

        tacos.setDirections("1 Prepare a gas or charcoal grill for medium-high, direct heat.\n" +
                "\n" +
                "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder," +
                " oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste." +
                " Add the chicken to the bowl and toss to coat all over.\n" +
                "\n" +
                "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n" +
                "\n" +
                "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into" +
                " the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
                "\n" +
                "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high heat." +
                " As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\n" +
                "\n" +
                "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
                "\n" +
                "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula." +
                " Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.");

        tacos.setSource("Simply Recipes");
        tacos.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
        tacos.getCategories().add(mexCategory);

        recipes.add(tacos);
        log.debug("Tacos... Done");
        log.debug("Adding recipes... Done");

        return recipes;
    }
}

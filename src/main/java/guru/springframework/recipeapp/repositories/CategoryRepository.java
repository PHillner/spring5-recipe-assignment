package guru.springframework.recipeapp.repositories;

import guru.springframework.recipeapp.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}

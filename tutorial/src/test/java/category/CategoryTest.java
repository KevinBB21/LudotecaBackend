package category;

import com.ccsw.tutorial.category.CategoryRepository;
import com.ccsw.tutorial.category.CategoryServiceImpl;
import com.ccsw.tutorial.category.model.Category;
import com.ccsw.tutorial.category.model.CategoryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    public void findAllShouldReturnAllCategories() {

          List<Category> list = new ArrayList<>();
          list.add(mock(Category.class));

          when(categoryRepository.findAll()).thenReturn(list);

          List<Category> categories = categoryService.findAll();

          assertNotNull(categories);
          assertEquals(1, categories.size());
    }

    public static final String CATEGORY_NAME = "CAT1";

@Test
public void saveNotExistsCategoryIdShouldInsert() {

      CategoryDto categoryDto = new CategoryDto();
      categoryDto.setName(CATEGORY_NAME);

      ArgumentCaptor<Category> category = ArgumentCaptor.forClass(Category.class);

      categoryService.save(null, categoryDto);

      verify(categoryRepository).save(category.capture());

      assertEquals(CATEGORY_NAME, category.getValue().getName());
}

public static final Long EXISTS_CATEGORY_ID = 1L;

@Test
public void saveExistsCategoryIdShouldUpdate() {

  CategoryDto categoryDto = new CategoryDto();
  categoryDto.setName(CATEGORY_NAME);

  Category category = mock(Category.class);
  when(categoryRepository.findById(EXISTS_CATEGORY_ID)).thenReturn(Optional.of(category));

  categoryService.save(EXISTS_CATEGORY_ID, categoryDto);

  verify(categoryRepository).save(category);
}
}
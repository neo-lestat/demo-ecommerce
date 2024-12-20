package org.demo.ecommerce.category;


import org.demo.ecommerce.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new CategoryNotFoundException("Category not found with %s".formatted(id)));
    }

    @Transactional(readOnly = true)
    public Page<Category> getCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Category create(Category category) {
        Category saved = categoryRepository.save(category);
        return saved;
    }

    public Category update(Category categoryDataUpdate) {
        Category category = findById(categoryDataUpdate.getId());
        category.setName(categoryDataUpdate.getName());
        category.setParentId(categoryDataUpdate.getParentId());
        return category;
    }
}

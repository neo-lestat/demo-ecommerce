package org.demo.ecommerce.category;


import org.demo.ecommerce.commons.PaginatedResponse;
import org.demo.ecommerce.model.Category;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryResource {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryService categoryService;

    @GetMapping(path = "/{id}", produces = "application/json")
    public Category getById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @GetMapping(value = "/all", params = {"page", "size"})
    public PaginatedResponse<Category> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable paging = PageRequest.of(page, size);
        Page<Category> categories = categoryService.getCategories(paging);
        PaginatedResponse<Category> response = new PaginatedResponse<>();
        response.setContent(categories.getContent());
        response.setPageNumber(categories.getNumber());
        response.setPageSize(categories.getSize());
        response.setTotalElements(categories.getTotalElements());
        response.setTotalPages(categories.getTotalPages());
        return response;
    }

    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@Valid @RequestBody CategoryDto categoryDto) {
        Category category =  categoryMapper.fromCategoryDto(categoryDto);
        return categoryService.create(category);
    }

    @PutMapping(produces = "application/json")
    public Category update(@Valid @RequestBody CategoryDto categoryDto) {
        Category category = categoryMapper.fromCategoryDto(categoryDto);
        return categoryService.update(category);
    }
}

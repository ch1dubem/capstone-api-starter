package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Category;
import org.yearup.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }

    // returns every category in the database (used by GET /categories)
    public List<Category> getAllCategories()
    {
        return categoryRepository.findAll();
    }

    // returns one category, or null if no category has that id (the controller turns null into a 404)
    public Category getById(int categoryId)
    {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    // inserts a new category; setting the id to 0 forces an INSERT and lets the database assign the id
    public Category create(Category category)
    {
        category.setCategoryId(0);
        return categoryRepository.save(category);
    }

    // loads the existing category, copies the new name/description onto it, then saves (an UPDATE)
    public Category update(int categoryId, Category category)
    {
        Category existing = categoryRepository.findById(categoryId).orElseThrow();
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        return categoryRepository.save(existing);
    }

    // removes the category with this id
    public void delete(int categoryId)
    {
        categoryRepository.deleteById(categoryId);
    }
}

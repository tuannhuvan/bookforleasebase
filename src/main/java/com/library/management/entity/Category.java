package com.library.management.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();

    public Category() {
    }

    public Category(String name, String description, Boolean active, List<Book> books) {
        this.name = name;
        this.description = description;
        this.active = active != null ? active : true;
        if (books != null) this.books = books;
    }

    public static CategoryBuilder builder() {
        return new CategoryBuilder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public static class CategoryBuilder {
        private String name;
        private String description;
        private Boolean active = true;
        private List<Book> books = new ArrayList<>();

        public CategoryBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CategoryBuilder description(String description) {
            this.description = description;
            return this;
        }

        public CategoryBuilder active(Boolean active) {
            this.active = active;
            return this;
        }

        public CategoryBuilder books(List<Book> books) {
            this.books = books;
            return this;
        }

        public Category build() {
            return new Category(name, description, active, books);
        }
    }
}

package com.library.management.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CategoryForm {

    private Long id;

    @NotBlank(message = "Tên thể loại không được để trống")
    private String name;

    private String description;

    private Boolean active = true;

    public CategoryForm() {
    }

    public CategoryForm(Long id, String name, String description, Boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active != null ? active : true;
    }

    public static CategoryFormBuilder builder() {
        return new CategoryFormBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public static class CategoryFormBuilder {
        private Long id;
        private String name;
        private String description;
        private Boolean active = true;

        public CategoryFormBuilder id(Long id) { this.id = id; return this; }
        public CategoryFormBuilder name(String name) { this.name = name; return this; }
        public CategoryFormBuilder description(String description) { this.description = description; return this; }
        public CategoryFormBuilder active(Boolean active) { this.active = active; return this; }

        public CategoryForm build() {
            return new CategoryForm(id, name, description, active);
        }
    }
}

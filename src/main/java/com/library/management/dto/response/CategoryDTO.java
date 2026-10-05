package com.library.management.dto.response;

public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean active;

    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String name, String description, Boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
    }

    public static CategoryDTOBuilder builder() {
        return new CategoryDTOBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public static class CategoryDTOBuilder {
        private Long id;
        private String name;
        private String description;
        private Boolean active;

        public CategoryDTOBuilder id(Long id) { this.id = id; return this; }
        public CategoryDTOBuilder name(String name) { this.name = name; return this; }
        public CategoryDTOBuilder description(String description) { this.description = description; return this; }
        public CategoryDTOBuilder active(Boolean active) { this.active = active; return this; }

        public CategoryDTO build() {
            return new CategoryDTO(id, name, description, active);
        }
    }
}

package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private long id;
    private String name;

    private Category parentCategory;
    private List<Category> childCategories;

    private List<Level> levels;

    public Category (long thisCatNumber, String thisCatName, Category thisParentCategory) {

        id = thisCatNumber;
        name = thisCatName;
        parentCategory = thisParentCategory;
        childCategories = new ArrayList<>();
        levels = new ArrayList<>();
    }

    public String getName () {

        return name;
    }

    public long getId () {

        return id;
    }

    public void setParentCategory (Category parentCategory) {

        this.parentCategory = parentCategory;
    }

    public Category getParentCategory () {

        return parentCategory;
    }

    public boolean hasParent () {

        return parentCategory != null;
    }

    public List<Category> getChildCategories () {

        return childCategories;
    }

    public boolean hasChildren () {

        return !childCategories.isEmpty();
    }

    public void addChildren (Category category) {

        childCategories.add(category);
    }

    public void addAllLevels (List<Level> thisLevels) {

        levels.addAll(thisLevels);
    }

    public List<Level> getLevels () {

        return levels;
    }

    @Override
    public String toString () {

        return getName();
    }
}

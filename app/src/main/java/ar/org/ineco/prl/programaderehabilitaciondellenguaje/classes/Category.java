package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private long catNumber;
    private String catName;
    private long parentCategory;
    private List<Category> childCategories;

    public Category (long thisCatNumber, String thisCatName, long thisParentCategory) {

        catNumber = thisCatNumber;
        catName = thisCatName;
        parentCategory = thisParentCategory;
        childCategories = new ArrayList();
    }

    public String getCatName () {

        return catName;
    }

    public long getCatNumber () {

        return catNumber;
    }

    public Long getParentCategory () {

        return parentCategory;
    }

    public boolean hasParent () {

        return getParentCategory() != null && getParentCategory() != 0;
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

    @Override
    public String toString () {

        return getCatName();
    }
}

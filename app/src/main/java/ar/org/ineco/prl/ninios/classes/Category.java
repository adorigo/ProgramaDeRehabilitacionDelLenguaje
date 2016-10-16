package ar.org.ineco.prl.ninios.classes;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private long id;
    private String name;
    private SoundFile snd;

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

    public SoundFile getSnd() {
        return snd;
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

    public void setSnd(SoundFile snd) {
        this.snd = snd;
    }

    public List<Category> getAllLeafs() {

        List<Category> categories = new ArrayList<>();

        if (getChildCategories().size() == 0) {
            categories.add(this);

        } else {
            for (Category child : this.getChildCategories()) {
                categories.addAll(child.getAllLeafs());
            }
        }

        return categories;
    }

    public String getPath() {

        return (parentCategory != null) ? parentCategory.getPath() +" -> "+ getName() : getName();
    }

    public void reset() {

        for (Level lvl : getLevels()) {
            lvl.resetQuestions();
        }
    }
}

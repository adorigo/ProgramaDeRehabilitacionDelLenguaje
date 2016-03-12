package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

import java.util.ArrayList;
import java.util.List;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.database.DatabaseLoader;

public class Menu {

    private static Menu ourInstance;

    public static Menu getInstance () {

        if (ourInstance == null) {
            ourInstance = new Menu();
        }
        return ourInstance;
    }

    private Menu () {

        DatabaseLoader databaseLoader = DatabaseLoader.getInstance();
        currentCategory = databaseLoader.getCategories();
        currentLevel = null;
    }

    private Category currentCategory;
    private Level currentLevel;

    public List<Category> getCategories () {

        List<Category> cats = new ArrayList<>();

        if (currentCategory != null) {

            cats = currentCategory.getChildCategories();
        }

        return cats;
    }

    public List<Level> getLevels () {

        List<Level> levels = new ArrayList<>();

        if (currentCategory != null) {

            levels = currentCategory.getLevels();
        }

        return levels;
    }

    public void setCurrentCategory (Category currentCategory) {

        this.currentCategory = currentCategory;
    }

    public void setCurrentLevel (Level currentLevel) {

        this.currentLevel = currentLevel;
    }

    public Level getCurrentLevel () {

        return currentLevel;
    }

    public String getLabel () {

        String label = "";

        if (currentCategory != null) {
            label = currentCategory.getName();
        }

        return label;
    }

    public boolean canGoLower () {

        return currentCategory != null && currentCategory.hasChildren();
    }

    public boolean canGoUp () {

        return currentCategory != null && currentCategory.hasParent();
    }

    public void goUp () {

        if (currentCategory != null) {

            this.currentCategory = currentCategory.getParentCategory();
        }
    }

    public String getActivityNameForLevel () {

        String activityName = "ar.org.ineco.prl.programaderehabilitaciondellenguaje.";

        if (currentCategory != null) {

            switch ((int) currentCategory.getId()) {

                case 3:
                case 4:
                case 5:
                case 6:
                case 8:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                    activityName += "PragmaticaActivity";
                    break;
            }
        }

        return activityName;
    }
}

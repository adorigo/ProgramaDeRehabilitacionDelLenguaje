package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        categories = databaseLoader.getCategories();
        levels = databaseLoader.getLevels();
        currentCategory = null;
        currentLevel = null;
    }

    private Map<Long, Category> categories;
    private Map<Long, Level> levels;

    private Category currentCategory;
    private Level currentLevel;

    public List<Category> getCategories () {

        List<Category> cats;

        if (currentCategory == null) {

            cats = new ArrayList<>();

            List<Category> allCategories = new ArrayList<>(categories.values());

            for (Category category : allCategories) {
                Log.d(Menu.class.getName(), category + " " + category.hasParent());
                if (!category.hasParent()) {

                    cats.add(category);
                }
            }

        } else {
            cats = currentCategory.getChildCategories();
        }

        return cats;
    }

    public List<Level> getLevels () {

        List<Level> lvls = new ArrayList<>();
        List<Level> allLvls = new ArrayList<>(levels.values());

        if (currentCategory != null) {

            for (Level level : allLvls) {

                if (level.getCatNumber() == currentCategory.getCatNumber()) {
                    lvls.add(level);
                }
            }
        }

        return lvls;
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
            label = currentCategory.getCatName();
        }

        return label;
    }

    public boolean canGoLower () {

        return currentCategory != null && currentCategory.hasChildren();
    }

    public boolean canGoUp () {

        boolean can = false;

        if (currentCategory != null) {
            Long parent = currentCategory.getParentCategory();
            can = (parent != null);
        }

        return can;
    }

    public void goUp () {

        if (currentCategory != null) {

            Long parent = currentCategory.getParentCategory();

            this.currentCategory = categories.get(parent);
        }
    }

    public String getActivityNameForLevel () {

        String activityName = "ar.org.ineco.prl.programaderehabilitaciondellenguaje.";

        if (currentLevel != null) {

            switch ((int) currentLevel.getCatNumber()) {

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

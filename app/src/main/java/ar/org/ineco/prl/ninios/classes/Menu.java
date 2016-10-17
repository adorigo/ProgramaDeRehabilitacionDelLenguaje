package ar.org.ineco.prl.ninios.classes;

import java.util.ArrayList;
import java.util.List;

import ar.org.ineco.prl.ninios.database.DatabaseLoader;

public class Menu {

    private static Menu ourInstance;
    private DatabaseLoader databaseLoader = DatabaseLoader.getInstance();

    public static Menu getInstance () {

        if (ourInstance == null) {
            ourInstance = new Menu();
        }
        return ourInstance;
    }

    private Menu () {

        currentCategory = databaseLoader.getCategories();
        topCategory = currentCategory;
        currentLevel = null;
    }

    private Category topCategory;
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
    public Category getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentLevel (Level currentLevel) {

        if (currentLevel != null) {
            currentLevel.clearQuestions();
            currentLevel.addAllQuestions(databaseLoader.getAllQuestions(currentLevel));
        }
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

    public SoundFile getAudioCategory() {

        SoundFile out = null;

        if (currentCategory != null) {
            out = currentCategory.getSnd();
        }

        return out;
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

    public void goToTop () {

        currentCategory = topCategory;
        currentLevel = null;
    }

    public String getActivityNameForLevel () {

        String activityName = "ar.org.ineco.prl.ninios.";

        if (currentCategory != null) {

            switch ((int) currentCategory.getId()) {

                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    activityName += "ComprensionActivity";
                    break;
                case 13:
                case 14:
                    activityName += "EmparejamientoActivity";
                    break;
                case 15:
                case 16:
                case 18:
                case 19:
                    activityName += "LocalizacionActivity";
                    break;
                case 12:
                    activityName += "DiccionarioActivity";
                    break;
            }
        }

        return activityName;
    }

    public Category getTopCategory() {
        return topCategory;
    }

    public List<Category> getFirstCategories() {
        return topCategory.getChildCategories();
    }
}

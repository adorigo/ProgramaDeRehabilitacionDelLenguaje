package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.database.DatabaseLoader;

public class Menu {

    private static Menu ourInstance;

    public static Menu getInstance() {

        if(ourInstance == null){
            ourInstance = new Menu();
        }
        return ourInstance;
    }

    private Menu() {

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

    public List<Category> getCategories(){

        List<Category> cats;

        if(currentCategory == null) {

            cats = new ArrayList<Category>();

            List<Category> allCategories = new ArrayList<Category>(categories.values());

            for(Category category : allCategories){
                Log.d(Menu.class.getName(), category+" "+category.hasParent());
                if(!category.hasParent()){

                    cats.add(category);
                }
            }

        } else {
            cats = currentCategory.getChildCategories();
        }

        return cats;
    }

    public List<Level> getLevels(){

        List<Level> lvls = new ArrayList<Level>();
        List<Level> allLvls = new ArrayList<Level>(levels.values());

        if(currentCategory != null){

            for(Level level : allLvls){

                if(level.getCatNumber() == currentCategory.getCatNumber()){
                    lvls.add(level);
                }
            }
        }

        return lvls;
    }

    public void setCurrentCategory(Category currentCategory) {
        this.currentCategory = currentCategory;
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public String getLabel() {

        String label = "";

        if(currentCategory != null){
            label = currentCategory.getCatName();
        }

        return label;
    }

    public boolean canGoLower() {

        return currentCategory.hasChildren();
    }

    public boolean canGoUp() {

        boolean can = false;

        if(currentCategory != null){
            Long parent = currentCategory.getParentCategory();
            can = (parent != null);
        }

        return can;
    }

    public void goUp() {

        Long parent = currentCategory.getParentCategory();

        this.currentCategory = categories.get(parent);
    }

    public String getActivityNameForLevel() {

        String activityName = "LevelsActivity";

        if(currentLevel != null){

            switch ((int) currentLevel.getCatNumber()){

                case 3:
                    activityName = "QuizActivity";
                    break;
            }
        }

        return activityName;
    }
}

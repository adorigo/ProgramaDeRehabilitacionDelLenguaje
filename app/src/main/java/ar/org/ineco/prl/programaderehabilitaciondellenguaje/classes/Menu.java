package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

import java.util.ArrayList;
import java.util.List;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.database.DatabaseLoader;

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

    public void setCurrentLevel (Level currentLevel) {

        if (currentLevel != null && currentLevel.getQuestions().size() == 0) {
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
                case 15:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 33:
                case 34:
                    activityName += "SemanticaActivity";
                    break;
                case 35:
                case 36:
                case 37:
                case 38:
                    activityName += "IdentificacionActivity";
                    break;
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                    activityName += "AsociacionActivity";
                    break;
                case 31:
                case 32:
                    activityName += "IntrusoActivity";
                    break;
                case 45:
                case 46:
                case 47:
                case 48:
                    activityName += "LecturaActivity";
                    break;
                case 50:
                case 51:
                case 52:
                case 54:
                case 55:
                case 56:
                case 58:
                case 59:
                case 60:
                    activityName += "DiscriminacionActivity";
                    break;
                case 61:
                case 62:
                case 63:
                case 72:
                case 73:
                case 74:
                case 75:
                case 76:
                case 77:
                case 78:
                case 79:
                case 80:
                case 81:
                case 82:
                case 83:
                case 69:
                case 70:
                case 71:
                    activityName += "DenominacionActivity";
                    break;
                case 84:
                    activityName += "VerdaderoFalsoActivity";
                    break;
                case 85:
                    activityName += "UnirConFlechasActivity";
                    break;
            }
        }

        return activityName;
    }
}

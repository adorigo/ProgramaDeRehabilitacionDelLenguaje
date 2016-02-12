package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

public class Level {
    private int lvlNumber;

    public Level(int thisLvlNumber) {
        lvlNumber = thisLvlNumber;
    }

    public String getLvlName() {
        return "Level " + String.valueOf(lvlNumber);
    }

    public int getLvlNumber() {
        return lvlNumber;
    }
}

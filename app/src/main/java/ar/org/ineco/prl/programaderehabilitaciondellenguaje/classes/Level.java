package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

public class Level {

    private long lvlId;
    private int lvlNumber;
    private long catNumber;

    public Level (long thisLVLId, int thisLVLNumber, long thisCatNumber) {

        lvlNumber = thisLVLNumber;
        lvlId = thisLVLId;
        catNumber = thisCatNumber;
    }

    public String getLvlName () {

        return "Level " + String.valueOf(lvlNumber);
    }

    public int getLvlNumber () {

        return lvlNumber;
    }

    public long getCatNumber () {

        return catNumber;
    }

    public long getLvlId () {

        return lvlId;
    }
}

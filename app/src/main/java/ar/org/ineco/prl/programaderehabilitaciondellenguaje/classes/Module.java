package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

public class Module {
    private int modNumber;
    private String modName;

    public Module(int thisModNumber, String thisModName) {
        modNumber = thisModNumber;
        modName = thisModName;
    }

    public String getModName() {
        return modName;
    }

    public int getModNumber() {
        return modNumber;
    }
}

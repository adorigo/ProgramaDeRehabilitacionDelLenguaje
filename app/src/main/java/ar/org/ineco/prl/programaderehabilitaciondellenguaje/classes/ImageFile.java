package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

public class ImageFile extends ResourceFile {

    public String txtImg;

    public ImageFile(long thisId, String thisName, String thisTxtImg) {

        super(thisId, thisName);

        txtImg = thisTxtImg;
    }

    public String getTxtImg() {
        return txtImg;
    }
}

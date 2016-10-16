package ar.org.ineco.prl.ninios.classes;

public class ImageFile extends ResourceFile {

    public String txtImg;
    public SoundFile snd;

    public ImageFile(long thisId, String thisName, String thisTxtImg, SoundFile sonido) {

        super(thisId, thisName);

        txtImg = thisTxtImg;
        snd = sonido;
    }

    public SoundFile getSnd(){
        return snd;
    }

    public String getTxtImg() {
        return txtImg;
    }
}

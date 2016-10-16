package ar.org.ineco.prl.ninios.classes;

public class ResourceFile {

    private long Id;
    private String Name;

    public ResourceFile (long thisId, String thisName) {

        this.Id = thisId;
        this.Name = thisName;
    }

    public long getId () {

        return Id;
    }

    public String getName () {

        return Name;
    }
}

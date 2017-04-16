package info.sdstudio.fiicurios;

/**
 * Created by sorin.donosa on 2/2/2017.
 */

public class Curiozitate {
    private String id;
    private String text;
    private String categorie;
    private int index;

    public Curiozitate() {
    }

    public Curiozitate(String id, String text) {
        this.id = id;
        this.text = text;
        this.categorie = categorie;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}

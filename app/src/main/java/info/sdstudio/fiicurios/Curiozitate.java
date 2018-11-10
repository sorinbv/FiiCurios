package info.sdstudio.fiicurios;

public class Curiozitate {
    private String id;
    private String text;
    private int index;

    public Curiozitate() {
    }

    public Curiozitate(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

package dokapon.characters;

public class LatinSprite {

    String image;
    String imageTop;
    String imageBot;

    public LatinSprite(String image) {
        this.image = image;
    }

    public LatinSprite(String imageTop, String imageBot) {
        this.imageTop = imageTop;
        this.imageBot = imageBot;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageTop() {
        return imageTop;
    }

    public void setImageTop(String imageTop) {
        this.imageTop = imageTop;
    }

    public String getImageBot() {
        return imageBot;
    }

    public void setImageBot(String imageBot) {
        this.imageBot = imageBot;
    }

    @Override
    public String toString() {
        return "LatinSprite{" +
                "image='" + image + '\'' +
                ", imageTop='" + imageTop + '\'' +
                ", imageBot='" + imageBot + '\'' +
                '}';
    }
}

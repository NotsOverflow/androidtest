package amc.androidmanagementcheck;

public class Item {

    private String title;
    private String description;
    // Image name (Without extension)
    private String icon;

    public Item(String title, String description, String icon) {
        this.title= title;
        this.description= description;
        this.icon= icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString()  {
        return this.title;
    }
}

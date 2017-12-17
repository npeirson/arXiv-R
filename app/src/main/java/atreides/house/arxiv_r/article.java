package atreides.house.arxiv_r;

/**
 * Created by the Kwisatz Haderach on 12/15/2017.
 */

public class article {
    private int id;
    private String title, summary, authors, published, updated;

    public article(int id, String title, String summary, String authors, String published, String updated) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.authors = authors;
        this.published = published;
        this.updated = updated;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() { return summary; }

    public String getAuthors() {
        return authors;
    }

    public String getPublished() {
        return published;
    }

    public String getUpdated() { return updated; }
}

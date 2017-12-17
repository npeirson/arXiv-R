package atreides.house.arxiv_r;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by obaro on 27/11/2016.
 * Check him out: https://github.com/obaro/
 * Thanks for sharing, obaro
 *
 * Customized by the Kwisatz Haderach on 15/12/2017.
 */

public class RssFeedModel {

    public String title;
    public String summary;
    public String author;
    public String published;
    public String updated;

    //public RssFeedModel(String title, String summary, String author, String published, String updated) {
    public RssFeedModel(ArrayList<String> holdMyDick) {
        this.title = holdMyDick.get(0);
        this.summary = holdMyDick.get(1);
        this.author = holdMyDick.get(2);
        this.published = holdMyDick.get(3);
        this.updated = holdMyDick.get(4);
    }
}

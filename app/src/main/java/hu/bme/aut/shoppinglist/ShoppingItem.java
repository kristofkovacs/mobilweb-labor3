package hu.bme.aut.shoppinglist;

import com.orm.SugarRecord;

/**
 * Created by kristofkovacs-egri on 2016. 10. 08..
 */

public class ShoppingItem extends SugarRecord {
    public String name;
    public String description;
    public Category category;
    public int estimatedPrice;
    public boolean isBought;

    public enum Category {
        FOOD, ELECTRONIC, BOOK;

        public static Category getByOrdinal(int ordinal) {
            Category ret = null;

            for (Category cat : Category.values()) {
                if (cat.ordinal() == ordinal) {
                    ret = cat;
                    break; }
            }
            return ret;
        }
    }
}

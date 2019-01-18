package storefront;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class bean {
    
    DBbean bean;
    ArrayList<CartItem> cart;    //Stores list of items in cart
    
    public bean() {
        bean = new DBbean(); //TODO, it's a comment because the DBbean was not used
        cart = new ArrayList<CartItem>();
    }
    
    public void buyItem(String item) throws SQLException {    
        bean.update("QUANTITY","ITEM_AMOUNT","ITEM_AMOUNT = ITEM_AMOUNT - 1","item");
        
    }
    
    public String getItem(String item) throws SQLException{
        String res = bean.readOneItem("items",item);
        
        return res;
    }
  
}

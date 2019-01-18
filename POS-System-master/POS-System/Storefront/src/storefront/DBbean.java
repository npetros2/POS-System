package storefront;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBbean {
    Connection con = null;
    Statement state = null;
    ResultSet result = null;
    
    
    public DBbean(){
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/store", "root", "password");
            state = con.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DBbean.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (state != null) {
                    state.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
              Logger.getLogger(DBbean.class.getName()).log(Level.SEVERE, null, ex);

                            }
        }
    }
    public int insert(String Table, ArrayList input) throws SQLException{
        String query="insert into "+Table+" ";
        query+= "values (";
        query+="'"+input.get(0)+"'";
        for(int i=1; i<input.size() ;i++){
            query+=", '"+input.get(i)+"'";
        }
        query+= " )";
        state.executeUpdate(query);
        
        return 0;
    }
    
    public String readOneItem(String table, String item ) throws SQLException{
        String query="SELECT * from "+table +" where ITEM_NAME = '" + item + "'; ";       
        String res;
        result = state.executeQuery(query);
        res = "" + result;
        
        return res;
    }
    
    
    public int update(String Table, String col, String value, String valueID) throws SQLException{
        String query="Update "+Table+" SET "+col+" = "+value+" WHERE ITEM_NAME = "+valueID+";";
        state.executeUpdate(query);
        int out =0;
        return out;
    }
    
    public int delete(String Table, ArrayList input) throws SQLException{
        String query="";
        
       state.executeUpdate(query);

        return 0;
    }
    
}

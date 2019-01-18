package storefront;

public class Main {
    public static void main (String[]args){
        
        MyView view = new MyView();
        bean bean = new bean();
        MyController controller = new MyController(bean, view);
        
        view.setVisible(true);
    }
}

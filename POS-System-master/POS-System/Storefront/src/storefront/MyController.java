package storefront;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import javax.swing.JButton;
import javax.swing.JList;

public class MyController {

    MyView view;
    bean model;
    
    public MyController(bean m, MyView v){
        view = v;
        model = m;
        addListeners();
    }
    
    private void addListeners() {
        view.addItemListener((java.awt.event.ActionEvent evt) -> {
            addToCart((JButton) evt.getSource());
        });
        view.addCardListener((java.awt.event.ActionEvent evt) -> {
            printReceipt("Paid with Card");
        });
        view.addCashListener((java.awt.event.ActionEvent evt) -> {
            String input = javax.swing.JOptionPane.showInputDialog(view, "How much cash will you give? ($#.## format recommended)", "0");
            NumberFormat n = NumberFormat.getCurrencyInstance();
            double change;
            double cash;
    
            //If input is in the right format
            if (input != null ) {
                if (input.matches("[$]?[0-9]*.?[0-9]{0,2}")) {
                    if (input.charAt(0) != '$')
                        cash = Double.parseDouble(input);
                    else
                        cash = Double.parseDouble(input.substring(1));
                    double total = Double.parseDouble(view.getTotal().getText().substring(1));

                    if (cash >= total) {
                        change = cash - total;
                        printReceipt("Cash Paid: "+n.format(cash)+"\tChange: "+n.format(change));
                    }
                    else
                        javax.swing.JOptionPane.showMessageDialog(view, "Not enough cash. Try again later.", "Storefront", javax.swing.JOptionPane.WARNING_MESSAGE);
                }
            }
    
            else 
                javax.swing.JOptionPane.showMessageDialog(view, "Invalid Input. Try again later.", "Storefront", javax.swing.JOptionPane.ERROR_MESSAGE);
        });
        view.addSuspendListener((java.awt.event.ActionEvent evt) -> {
            //TODO unsure about what the suspend button should do. Should it exit the application or clear the list? Currently has a confirmation dialog box and clears the list.
            int answer = javax.swing.JOptionPane.showConfirmDialog(view, "Are you sure you want to suspend your purchase?", "Storefront", javax.swing.JOptionPane.YES_NO_OPTION);

            if (answer == 0) {
                //
                model.cart.clear();
                view.dlm.clear();
                updatePrice();
                //TODO refer to above TODO. If suspend button is for exiting application, delete above up to the "//". Otherwise, delete this TODO block
                //System.exit(0);
            }
        });
        view.addCloseListener((java.awt.event.ActionEvent evt) -> {
            System.exit(0);
        });
        view.addDeleteListener((java.awt.event.ActionEvent evt) -> {
            removeFromCart(view.getCart());
        });
        
    }
    
    public void addToCart(JButton b) {
        String input = javax.swing.JOptionPane.showInputDialog(view, "How many do you want to add to the cart?", "1");
        boolean itemExists = false;

        if (input.matches("[0-9]*")) {
            int qty = Integer.parseInt(input);
            if (qty == 0)
                return;
            CartItem newItem = new CartItem(b.getText(), qty);
            for (CartItem c : model.cart) {
                if (c.getName().equals(newItem.getName())){
                    c.increaseQty(newItem.getQty());
                    itemExists = true;
                }
            }
            if (!itemExists)
                model.cart.add(newItem);
            else
                javax.swing.JOptionPane.showMessageDialog(view, "This item already exists in your cart and will be added accordingly.", "Storefront", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            updatePrice();
        }
        else
            javax.swing.JOptionPane.showMessageDialog(view, "Invalid Input. Try again later.", "Storefront", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    
    public void removeFromCart(JList l) {
        for (CartItem c : model.cart) {
            if (c.toString().equals(l.getSelectedValue())) {
                model.cart.remove(c);
                break;
            }
        }
        updatePrice();
    }
    
    public void updatePrice() {
        NumberFormat n = NumberFormat.getCurrencyInstance();
        double subtotal = 0;
        double tax;
        
        view.dlm.clear();
        for (CartItem c : model.cart) {
            subtotal += c.getPrice() * c.getQty();
            view.dlm.addElement(c.toString());System.out.println(c.getTotal());
        }
        tax = subtotal * 0.06;
        double total = subtotal + tax;
        
        view.getSubtotal().setText(n.format(subtotal));
        view.getTax().setText(n.format(tax));
        view.getTotal().setText(n.format(total));
    }
    
    public void printReceipt(String paid) {
        try {
            File receipt = new File("receipt.txt");
            FileWriter w = new FileWriter("receipt.txt");

            try (PrintWriter writer = new PrintWriter(w)) {
                
                writer.println("Receipt");
                writer.println();
                
                for (CartItem c : model.cart) {
                    writer.println(c.toString());
                }
                if (model.cart.isEmpty()) {
                    writer.println("You did not buy anything.");
                }
                
                writer.println();
                writer.println("Subtotal: "+view.getSubtotal().getText());
                writer.println("Tax: "+view.getTax().getText());
                writer.println("Total: "+view.getTotal().getText());
                writer.println();
                
                writer.println(paid);
            }
            
            javax.swing.JOptionPane.showMessageDialog(view, "Your receipt is located in: "+receipt.getAbsolutePath()+"\n\nHave a good day.", "Storefront", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        catch(IOException i) {
            System.out.println(i.getMessage());
        }
    }
    
}

class CartItem {
    
    private int id;
    private double price = 0.21;    //TODO default price is 21 cents, used for debugging without model
    private int qty;
    private String name;
    
    public CartItem(String name, int qty) {
        this.name = name;
        this.qty = qty;
        
        //TODO get price from database
        //this.price = ;
    }
    
    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }
    
    public int getQty() {
        return qty;
    }
    
    public void increaseQty(int i) {
        qty += i;
    }
    
    public double getTotal() {
        return price * qty;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPriceString() {
        NumberFormat n = NumberFormat.getCurrencyInstance();
        return n.format(price);
    }
    
    public String getTotalString() {
        NumberFormat n = NumberFormat.getCurrencyInstance();
        return n.format(getTotal());
    }
    
    @Override
    public String toString() {
        //Return name with original price times quantity equals total price
        return getName()+" ("+getPriceString()+") x "+getQty()+" = "+getTotalString();
    }
    
}

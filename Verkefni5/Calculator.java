import net.miginfocom.swing.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
public class Calculator implements ActionListener{
    private String currentNum = "0";
    private String expression = "";
    private JFrame frame = new JFrame("Calculator");
    private JLabel numLabel = new JLabel(currentNum);
    private JLabel lastOpLabel = new JLabel(expression);
    private boolean shouldChange = true;
    public Calculator(){
        makeUI();
    }
    private void makeUI(){
        MigLayout layout = new MigLayout("fill, wrap 4", "grow, 60:60:", 
        "[grow, 10:10:] [grow, 40:40:] [grow, 40:40:] [grow, 40:40:][grow, 40:40:][grow, 40:40:][grow, 40:40:]");
        frame.setLayout(layout);
        // numLabel.setText(currentNum);
        numLabel.setFont(numLabel.getFont().deriveFont(30.0f));
        JButton clear = makeButton("Clear");
        JButton del = makeButton("⌫");
        JButton[] nums = new JButton[10];
        for(int i = 0; i<10; i++){
            nums[i] = makeButton(Integer.toString(i));
        }
        JButton divide = makeButton("/");
        JButton multiply = makeButton("*");
        JButton add = makeButton("+");
        JButton minus = makeButton("-");
        JButton dot = makeButton(".");
        JButton equals = makeButton("=");

        frame.add(lastOpLabel, "span, grow, gapright 10");
        lastOpLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        frame.add(numLabel, "span, grow, gapright 10");
        numLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        frame.add(clear, "grow, span 2");
        frame.add(del, "grow");
        frame.add(divide,"grow");
        for(int i = 7; i<10; i++) frame.add(nums[i],"grow");
        frame.add(multiply,"grow");
        for(int i = 4; i<7; i++) frame.add(nums[i],"grow");
        frame.add(minus,"grow");
        for(int i = 1; i<4; i++) frame.add(nums[i],"grow");
        frame.add(add,"grow");
        frame.add(dot,"grow");
        frame.add(nums[0], "grow");
        frame.add(equals,"grow, span 2");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    private JButton makeButton(String s){
        JButton retButton = new JButton(s);
        retButton.addActionListener(this);
        return retButton;
    }
    private void backspace(){
        if(currentNum.length() > 1){
            currentNum = currentNum.substring(0, currentNum.length()-1);
        }
    }
    private void clear(){
        currentNum = "0";
        if(shouldChange == true)
            expression = "";
        shouldChange = true;
    }
    private void addOp(String s){
        expression = expression.concat(currentNum + " " + s + " ");
        shouldChange = true;
    }
    private void addDot(){
        currentNum = currentNum.concat(".");
        shouldChange = false;
    }
    private void addNum(String num){
        if(shouldChange){
            currentNum = num;
        }
        else currentNum = currentNum.concat(num);
        shouldChange = false;
    }
    private void equals(){
        shouldChange = true;
        expression = expression.concat(currentNum + " " + "=");
        lastOpLabel.setText(expression);
        Evaluate eval;
        try{
        eval = new Evaluate(expression);
        currentNum = eval.getNumber();
        }
        catch(Exception e){
            errorMessage("Invalid Expression");
            return;
        }
        expression = "";
    }
    private void errorMessage(String s){
        JOptionPane.showMessageDialog(frame, s,
        "Error",JOptionPane.ERROR_MESSAGE);
    }
    private boolean isNumber(String num){
        try{
            Integer.parseInt(num);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
    public void actionPerformed(ActionEvent e){
        String actionName = ((JButton)(e.getSource())).getText();
        if(actionName.equals("⌫")) backspace();
        else if(actionName.equals("Clear")) clear();
        else if(actionName.equals(".")) addDot();
        else if(actionName.equals("=")) equals();
        else if(isNumber(actionName)) addNum(actionName);
        else addOp(actionName);
        numLabel.setText(currentNum);
        if(!actionName.equals("="))
            lastOpLabel.setText(expression);
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args){
        Runnable evt = ()->{
            new Calculator();
        };
        EventQueue.invokeLater(evt);
    }
}
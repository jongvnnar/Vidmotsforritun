
import net.miginfocom.swing.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
public class Calculator implements ActionListener{
    private String currentNum = "0";
    private String expression = "";
    private JFrame frame = new JFrame("Calculator");
    private JPanel mainPanel = new JPanel();
    private JPanel calculatorPanel = new JPanel();
    private JPanel hisMemPanel = new JPanel();
    private JPanel hisMemScroll = new JPanel();
    private JLabel numLabel = new JLabel(currentNum);
    private JTextField expressionArea = new JTextField(expression);
    private boolean shouldChange = true;
    private boolean isHistory = true;
    public Calculator(){
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBackground(new Color(0,200,200));
        mainPanel.setLayout(new MigLayout("fill", "[grow] [] [grow]"));
        standardCalculatorPanel();
        mainPanel.add(calculatorPanel, "grow");
        mainPanel.add(new JSeparator(1), "pushy, growy");
        hisMemPanel();
        mainPanel.add(hisMemPanel, "grow");
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);

    }
    private void standardCalculatorPanel(){
        calculatorPanel.removeAll();
        MigLayout layout = new MigLayout("fill, wrap 4", "grow, 60:60:", 
        "[grow, 20:20:] [grow, 40:40:] [grow, 40:40:] [grow, 40:40:][grow, 40:40:][grow, 40:40:][grow, 40:40:]");
        calculatorPanel.setLayout(layout);
        numLabel.setFont(numLabel.getFont().deriveFont(30.0f));

        JRadioButton standard = new JRadioButton("Standard");
        standard.setSelected(true);
        standard.addActionListener(this);
        JRadioButton scientific = new JRadioButton("Scientific");
        scientific.addActionListener(this);
        ButtonGroup radioButtons = new ButtonGroup();
        radioButtons.add(standard);
        radioButtons.add(scientific);
        calculatorPanel.add(standard, "growx 0");
        calculatorPanel.add(scientific, "growx 0, span 3, gapleft 10");
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

        calculatorPanel.add(expressionArea, "span, grow");
        expressionArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        calculatorPanel.add(numLabel, "span, grow, gapright 10");
        numLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        calculatorPanel.add(clear, "grow, span 2");
        calculatorPanel.add(del, "grow");
        calculatorPanel.add(divide,"grow");
        for(int i = 7; i<10; i++) calculatorPanel.add(nums[i],"grow");
        calculatorPanel.add(multiply,"grow");
        for(int i = 4; i<7; i++) calculatorPanel.add(nums[i],"grow");
        calculatorPanel.add(minus,"grow");
        for(int i = 1; i<4; i++) calculatorPanel.add(nums[i],"grow");
        calculatorPanel.add(add,"grow");
        calculatorPanel.add(dot,"grow");
        calculatorPanel.add(nums[0], "grow");
        calculatorPanel.add(equals,"grow, span 2");
        calculatorPanel.revalidate();
    }
    private void scientificCalculatorPanel(){
        calculatorPanel.removeAll();
        MigLayout layout = new MigLayout("fill, wrap 6", "grow, 60:60:", 
        "[grow, 20:20:] [grow, 40:40:] [grow, 40:40:] [grow, 40:40:][grow, 40:40:][grow, 40:40:][grow, 40:40:]");
        calculatorPanel.setLayout(layout);
        numLabel.setFont(numLabel.getFont().deriveFont(30.0f));

        JRadioButton standard = new JRadioButton("Standard");
        standard.addActionListener(this);
        JRadioButton scientific = new JRadioButton("Scientific");
        scientific.setSelected(true);
        scientific.addActionListener(this);
        calculatorPanel.add(standard, "growx 0");
        calculatorPanel.add(scientific, "growx 0, span, gapleft 10");

        JButton powY = makeButton("x^y");
        JButton pow2 = makeButton("x^2");
        JButton leftSvigi = makeButton("(");
        JButton rightSvigi = makeButton(")");
        JButton hrop = makeButton("n!");
        JButton ln = makeButton("ln");
        JButton mod = makeButton("mod");
        JButton sqrt = makeButton("√x");
        JButton tenPow = makeButton("10^");
        JButton absolute = makeButton("|x|");
        JButton e = makeButton("e");
        JButton pi = makeButton("π");

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

        calculatorPanel.add(expressionArea, "span, grow");
        expressionArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        calculatorPanel.add(numLabel, "span, grow, gapright 10");
        numLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        calculatorPanel.add(powY, "grow");
        calculatorPanel.add(pow2, "grow");
        calculatorPanel.add(leftSvigi, "grow");
        calculatorPanel.add(rightSvigi, "grow");
        calculatorPanel.add(clear, "grow");
        calculatorPanel.add(del, "grow");
        calculatorPanel.add(hrop, "grow");
        calculatorPanel.add(ln, "grow");
        for(int i = 7; i<10; i++) calculatorPanel.add(nums[i],"grow");
        calculatorPanel.add(divide,"grow");
        calculatorPanel.add(mod, "grow");
        calculatorPanel.add(sqrt, "grow");
        for(int i = 4; i<7; i++) calculatorPanel.add(nums[i],"grow");
        calculatorPanel.add(multiply,"grow");
        calculatorPanel.add(tenPow, "grow");
        calculatorPanel.add(absolute, "grow");
        for(int i = 1; i<4; i++) calculatorPanel.add(nums[i],"grow");
        calculatorPanel.add(minus,"grow");
        calculatorPanel.add(e, "grow");
        calculatorPanel.add(pi, "grow");
        calculatorPanel.add(dot,"grow");
        calculatorPanel.add(nums[0], "grow");
        calculatorPanel.add(equals,"grow");
        calculatorPanel.add(add,"grow");
        calculatorPanel.revalidate();
    }
    private void hisMemPanel(){
        hisMemPanel.removeAll();
        MigLayout layout = new MigLayout("fill, wrap 2", "grow, 80::", 
        "[grow, 30:30:] [grow, 40:40:] [grow, 40:40:] [grow, 40:40:][grow, 40:40:][grow, 40:40:][grow, 40:40:]");
        hisMemPanel.setLayout(layout);
        JButton history = makeButton("History");
        JButton memory = makeButton("Memory");
        JScrollPane scroller = new JScrollPane();

        hisMemPanel.add(history, "grow");
        hisMemPanel.add(memory, "grow");

        if(this.isHistory){
            scroller.setViewportView(new JTextArea("history"));
            history.setBackground(new Color(255,99,71));
            history.setForeground(Color.BLACK);
        }
        else{
            scroller.setViewportView(new JTextArea("memory"));
            memory.setBackground(new Color(255,99,71));
            memory.setForeground(Color.BLACK);

        }
        hisMemPanel.add(scroller, "grow, span 2 7");
        hisMemPanel.revalidate();
    }
    /**
     * Hjálparfall sem skilar JButton takka með actionListener sem bendir á Calculator hlutinn sjálfan.
     * @param s er strengur sem lýsir titlinum á takkanum
     * @return JButton með actionListener sem bendir á hlutinn sjálfan með titil s.
     */
    private JButton makeButton(String s){
        JButton retButton = new JButton(s);
        try{
            Integer.parseInt(s);
            retButton.setBackground(new Color(210,210,210));
        }
        catch(Exception e){
            retButton.setBackground(Color.WHITE);
        }
        retButton.addActionListener(this);
        return retButton;
    }

    /**
     * Hjálparfall sem gerir manni kleift að eyða aftasta gildi currentNum.
     * Fyrir: currentNum er strengur af lengd >= 1.
     * Eftir: ef currentNum var strengur af lengd > 1 er currentNum nú sami 
     * strengur án aftasta characters.
     */
    private void backspace(){
        if(currentNum.length() > 1){
            currentNum = currentNum.substring(0, currentNum.length()-1);
        }
    }
    /**
     * Hjálparfall við til að styðja clear aðgerðina.
     * Fyrir: Tilviksbreytur geta verið hvernig sem er.
     * Eftir: currentNum er "0". expression er
     * nú tómi strengurinn. shouldChange er nú með gildið true.
     */
    private void clear(){
        currentNum = "0";
        expression = "";
        shouldChange = true;
    }
    /**
     * Hjálparfall við að bæta aðgerðum í reiknisegð.
     * @param s strengur sem lýsir aðgerðinni sem á að bæta við.
     * Eftir: s hefur verið bætt við aftan við expression tilviksbreytuna. shouldChange
     * er nú stillt sem true.
     */
    private void addOp(String s){
        expression = expression.concat(currentNum + " " + s + " ");
        shouldChange = true;
    }
    /**
     * Hjálparfall við að bæta punktum við tölur til að birta kommutölur.
     * Fyrir: Tilviksbreytur geta verið hvernig sem er.
     * Eftir: Punkti hefur verið bætt aftast í currentNum strenginn.
     * shouldChange er nú stillt sem false.
     */
    private void addDot(){
        currentNum = currentNum.concat(".");
        shouldChange = false;
    }
    /**
     * Hjálparfall við að bæta heiltölum við currentNum.
     * @param num strengur sem lýsir heiltölu.
     * Fyrir: currentNum er strengur sem lýsir heiltölu. shouldChange er annaðhvort true
     *  eða false.
     * Eftir: Ef shouldChange var true er currentNum nú heiltalan sem bætt var við.
     *  Ef shouldChange var false er currentNum nú sami strengur að viðbættri með num strenginn
     *  skeyttan aftan við.
     *  shouldChange er nú stillt sem false.
     */
    private void addNum(String num){
        if(shouldChange){
            currentNum = num;
        }
        else currentNum = currentNum.concat(num);
        shouldChange = false;
    }
    /**
     * Hjálparfall sem styður við equals takkann.
     * Fyrir: expression strengur sem lýsir gildri eða ógildri reiknisegð. currentNum
     *  er strengur sem lýsir síðustu tölu sem sett var inn í reiknivélina.
     * Eftir: expressionArea sýnir gamla expression strenginn að viðbættu jafnaðarmerki.
     *  expression er nú tómistrengurinn. Hafi expression lýst gildri reiknisegð hefur currentNum
     *  verið breytt í svarið við þeirri reiknisegð. Annars er currentNum hið sama og áður og kallað
     *  hefur verið á errorMessage fall með strengnum "Invalid expression". shouldChange er nú stillt
     *  sem true
     */
    private void equals(){
        shouldChange = true;
        String evalExpression = expression.concat(currentNum + " " + "=");
        expressionArea.setText(evalExpression);
        Evaluate eval = new Evaluate(evalExpression);
        try{
        currentNum = eval.getNumber();
        }
        catch(Exception e){
            errorMessage("Invalid Expression");
        }
        expression = "";
    }
    /**
     * Hjálparfall við að birta villuboð fyrir notanda
     * @param s er strengur sem inniheldur villuboðin sem birta á
     */
    private void errorMessage(String s){
        JOptionPane.showMessageDialog(frame, s,
        "Error",JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Hjálparfall við að meta hvort strengur lýsi heiltölu eða ekki.
     * @param num strengur sem lýsir mögulega heiltölu.
     * @return boolean gildi sem er true ef strengurinn num lýsti heiltölu og false ef ekki.
     */
    private boolean isNumber(String num){
        try{
            Integer.parseInt(num);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
    /**
     * Framkvæmir aðgerðirnar lýst að ofan þegar ýtt er á samsvarandi takka.
     * Uppfærir numLabel og uppfærir expressionArea ef ekki var ýtt á jafnaðarmerki.
     */
    public void actionPerformed(ActionEvent e){
        String actionName;
        try{
            actionName = ((JButton)(e.getSource())).getText();
        }
        catch(Exception exc){
            actionName = ((JRadioButton)(e.getSource())).getText();
        }
        System.out.println(actionName);
        if(actionName.equals("⌫")) backspace();
        else if(actionName.equals("Standard")){
            standardCalculatorPanel();
            mainPanel.repaint();
            frame.pack();
        }
        else if(actionName.equals("Scientific")){
            scientificCalculatorPanel();
            mainPanel.repaint();
            frame.pack();
        }
        else if(actionName.equals("History")){
            this.isHistory = true;
            hisMemPanel();
            mainPanel.repaint();
        }
        else if(actionName.equals("Memory")){
            this.isHistory = false;
            hisMemPanel();
            mainPanel.repaint();
        }
        else if(actionName.equals("Clear")) clear();
        else if(actionName.equals(".")) addDot();
        else if(actionName.equals("=")) equals();
        else if(isNumber(actionName)) addNum(actionName);
        else addOp(actionName);
        numLabel.setText(currentNum);
        if(!actionName.equals("="))
            expressionArea.setText(expression);
    }
    public static void main(String[] args){
        Runnable evt = ()->{
            new Calculator();
        };
        EventQueue.invokeLater(evt);
    }
}
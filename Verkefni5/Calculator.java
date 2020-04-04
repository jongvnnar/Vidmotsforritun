/**
 * Klasi sem birtir einfalda reiknivél.
 * Má keyra með því að gera eftirfarandi skipanir:
 * SET CLASSPATH=.;miglayout-core-5.3-20190920.220054-302.jar;miglayout-swing-5.3-20190918.220057-300.jar
 * javac -encoding utf8 Calculator.java
 * java Calculator
 * fyrir Windows OS.
 * 
 * Sé maður að nota LINUX skal keyra
 * CLASSPATH=.:miglayout-core-5.3-20190920.220054-302.jar:miglayout-swing-5.3-20190918.220057-300.jar
 * javac -encoding utf8 Calculator.java
 * java Calculator
 * 
 * Reiknivélin styður hvorki svigasetningu né RPN, heldur er nýjasta talan sem reiknuð er geymd og hægt
 * er að vinna með hana. Reiknisegðir eru metnar með klasanum Evaluate samkvæmt skilum þess klasa, frá 
 * vinstri til hægri án þess að miða við operator precedence. Reiknivélin styður margföldun, deilingu,
 * plús og mínus ásamt því að styðja eyðingu og 'backspace'.
 * 
 * Til að reikna segðir sem myndu annars krefjast svigasetningar, t.d. '(1+2)*3' má styðja á hnappa
 *  '1', '+', '2', '=', '*', '3','=' til að fá útkomuna 9. 
 * 
 * @author Jón Gunnar Hannesson
 * @author jgh12@hi.is
 */
import net.miginfocom.swing.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
public class Calculator implements ActionListener{
    /**
     * currentNum er strengur sem heldur utan um svar síðustu gildu reiknisegðar.
     * currentNum er grunnstillt sem "0".
     * expression er strengur sem lýsir reiknisegðinni sem verið er að vinna í
     * hverju sinni. Grunnstillt sem tómistrengurinn.
     * frame er JFrame rammi sem inniheldur þá takka sem reiknivélin notar ásamt
     * tveimur JLabel hlutum lýst neðar. Titlaður "Calculator".
     * numLabel er JLabel merkjari sem inniheldur currentNum strenginn.
     * lastOpLabel er JLabel merkjari sem inniheldur expression strenginn.
     * shouldChange er boolean breyta sem lýsir því hvort ætti að skrifa yfir currentNum
     * eða hvort ætti að bæta við aftan á currentNum strenginn. Hún er einnig notuð
     * til að sjá hvort eigi að skrifa yfir expression strenginn eða bæta við hann. 
     * Grunnstillt sem true.
     */
    private String currentNum = "0";
    private String expression = "";
    private JFrame frame = new JFrame("Calculator");
    private JLabel numLabel = new JLabel(currentNum);
    private JLabel lastOpLabel = new JLabel(expression);
    private boolean shouldChange = true;
    /**
     * Smiður fyrir tilvik af Calculator.
     * Eftir: Ný reiknivél birtist með breytur grunnstilltar eins og lýst var í 
     * fastayrðingu gagna.
     */
    public Calculator(){
        makeUI();
    }
    /**
     * Hjálparfall sem byggir reiknivélina innan frame og birtir hana. Reiknivélin er byggð upp sem grid þar sem 
     * hver sella hefur minnstu stærð sem og preferred size 40x60, nema í fyrstu röð þar sem gildin eru 10x60.
     * Í hverri röð eru fjórar sellur. Reiknivélin er skalanleg en þó er hægt að skrifa tölur og 
     * segðir nógu langar til að fara út fyrir rammann. Hægt er að loka reiknivél
     * með því að ýta á krossinn í efra vinstra horni.
     */
    private void makeUI(){
        MigLayout layout = new MigLayout("fill, wrap 4", "grow, 60:60:", 
        "[grow, 10:10:] [grow, 40:40:] [grow, 40:40:] [grow, 40:40:][grow, 40:40:][grow, 40:40:][grow, 40:40:]");
        frame.setLayout(layout);
        frame.setMinimumSize(new Dimension(240,250));
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
    /**
     * Hjálparfall sem skilar JButton takka með actionListener sem bendir á Calculator hlutinn sjálfan.
     * @param s er strengur sem lýsir titlinum á takkanum
     * @return JButton með actionListener sem bendir á hlutinn sjálfan með titil s.
     */
    private JButton makeButton(String s){
        JButton retButton = new JButton(s);
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
     * Eftir: lastOpLabel sýnir gamla expression strenginn að viðbættu jafnaðarmerki.
     *  expression er nú tómistrengurinn. Hafi expression lýst gildri reiknisegð hefur currentNum
     *  verið breytt í svarið við þeirri reiknisegð. Annars er currentNum hið sama og áður og kallað
     *  hefur verið á errorMessage fall með strengnum "Invalid expression". shouldChange er nú stillt
     *  sem true
     */
    private void equals(){
        shouldChange = true;
        String evalExpression = expression.concat(currentNum + " " + "=");
        lastOpLabel.setText(evalExpression);
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
     * Uppfærir numLabel og uppfærir lastOpLabel ef ekki var ýtt á jafnaðarmerki.
     */
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
    }
    public static void main(String[] args){
        Runnable evt = ()->{
            new Calculator();
        };
        EventQueue.invokeLater(evt);
    }
}
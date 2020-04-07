/**
 * Klasi sem birtir reiknivél.
 * Má keyra með því að gera eftirfarandi skipanir:
 * SET CLASSPATH=.;miglayout-core-5.3-20190920.220054-302.jar;miglayout-swing-5.3-20190918.220057-300.jar;MathParser.org-mXparser-v.4.4.0-jdk11.jar
 * javac -encoding utf8 Calculator.java
 * java Calculator
 * fyrir Windows OS.
 * 
 * Sé maður að nota LINUX skal keyra
 * CLASSPATH=.:miglayout-core-5.3-20190920.220054-302.jar:miglayout-swing-5.3-20190918.220057-300.jar:MathParser.org-mXparser-v.4.4.0-jdk11.jar
 * javac -encoding utf8 Calculator.java
 * java Calculator
 * 
 * Reiknivélin styður svigasetningu sem og margföldun, deilingu, samlagningu, frádrátt, veldisreikning og leifareikning.
 * Ásamt því er stuðningur við náttúrulega logrann, algildi og kvaðratrót. 
 * Reiknivélin inniheldur takkaviðmót sem styður alla reikninga að ofan þar sem að hægt er að skipta á milli "standard"
 * reiknivélar með viðmóti sem styður sviga, margföldun, deilingu, frádrátt og samlagningu og "Scientific"
 * reiknivélar sem inniheldur sömu virkni og "standard" að viðbættum stuðningi við virkjana nefnda að ofan.
 * "Scientific" reiknivélin býður einnig upp á að nota fastana e og pí.
 * 
 * Reiknivélin metur segðir eins og lýst er í skjölun Evaluate klasa.
 * 
 * Einnig geymir reiknivélin fyrri reiknisegðir undir "History" flipa, sem hægt er að ýta á til að vinna
 * aftur með í reiknivélinni. Ásamt þessu er "Memory" flipi þar sem hægt er að vista fasta á meðan reiknivél er
 * virk og nota þá í reiknisegðum.
 * 
 * Reiknivélin styður einnig að rita inn reiknisegðir með lyklaborði í reiknisegðarglugga. Þar er hægt að beyta
 * örvatökkunum upp og niður til að fletta í gegnum fyrri reiknisegðir og jafngildir enter takkinn því að
 * ýta á jafnaðarmerki með takkaviðmóti til að fá svar við reiknisegð.
 * 
 * Sé reiknisegð ekki gild birtir reiknivélin villuboð og skilar NaN(Not a Number) 
 * 
 * @author Jón Gunnar Hannesson
 * @author jgh12@hi.is
 */
import net.miginfocom.swing.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import java.util.regex.*;
public class Calculator implements ActionListener{
    /**
     * currentNum er strengur sem heldur utan um svar síðustu gildu reiknisegðar.
     * currentNum er grunnstillt sem "0".
     * expression er strengur sem lýsir reiknisegðinni sem verið er að vinna í
     * hverju sinni. Grunnstillt sem tómistrengurinn.
     * history er LinkedList fyrir strengi grunnstilltur með engu innihaldi. Notaður til að geyma
     * eldri reiknisegðir.
     * memory er LinkedHashMap fyrir strengi til að geyma fasta. Þar er nafn fastans key og gildi hans value.
     * Grunnstillt sem tómt LinkedHashMap.
     * frame er JFrame rammi sem inniheldur mainPanel og þar með allt viðmót, titlaður "Calculator".
     * mainPanel er JPanel sem á að innihalda alla viðmótshluti reiknivélar.
     * calculatorPanel er JPanel sem inniheldur viðmót fyrir reikniaðgerðir, reiknisegðarglugga o.s.frv.
     * expressionArea er JTextField sem inniheldur expression.
     * hisMemPanel er JPanel sem inniheldur history og memory flipa lýst að ofan.
     * hisMemScrollPanel er JPanel sem inniheldur takka sem lýsa innihaldi history og memory.
     * scroller er JScroller sem inniheldur hisMemScrollPanel.
     * shouldChange er boolean breyta sem lýsir því hvort ætti að skrifa yfir currentNum
     * eða hvort ætti að bæta við aftan á currentNum strenginn. Hún er einnig notuð
     * til að sjá hvort eigi að skrifa yfir expression strenginn eða bæta við hann. 
     * Grunnstillt sem true.
     * isHistory er boolean breyta sem segir til um hvort verið sé að birta history eða memory flipa
     * historyButton er JButton sem birtir innihald history í hisMemScrollPanel.
     * Gerir ekkert ef isHistory er stillt sem true.
     * memoryButton er takki sem birtir innihald memory í hisMemScrollPanel ásamt viðmóti til að vista
     * nýja fasta. Gerir ekkert ef isHistory er stillt sem false.
     * nameField er JTextField sem notað er til að rita inn nafn á fasta sem á að vista.
     * valueField er JTextField sem notað er til að rita inn gildi á nýjum fasta sem á að vista.
     * i er heiltölubreyta sem notuð er til að fletta í gegnum history með örvatökkum í expressionArea.
     */
    private String currentNum = "0";
    private String expression = "";
    private LinkedList<String> history = new LinkedList<String>();
    private LinkedHashMap<String, String> memory = new LinkedHashMap<String, String>();
    private JFrame frame = new JFrame("Calculator");
    private JPanel mainPanel = new JPanel();
    private JPanel calculatorPanel = new JPanel();
    private JPanel hisMemPanel = new JPanel();
    private JPanel hisMemScrollPanel = new JPanel(new MigLayout("fillx, wrap 1", "grow","20:20:"));
    private JScrollPane scroller = new JScrollPane();
    private JLabel numLabel = new JLabel(currentNum);
    private JTextField expressionArea = new JTextField(expression);
    private boolean shouldChange = true;
    private boolean isHistory = true;
    private JButton historyButton;
    private JButton memoryButton;
    private JTextField nameField = new JTextField();
    private JTextField valueField = new JTextField();
    private int i = -1;
    /**
     * Smiður fyrir tilvik af Calculator. 
     * Notkun: Calculator calc = new Calculator() eða einfaldlega new Calculator()
     * Eftir: Reiknivél birt eins og lýst var í fastayrðingu gagna með skiptingu á milli hisMemPanel og calculatorPanel.
     * 
     */
    public Calculator(){
        ImageIcon icon = new ImageIcon("./lightningIcon.png");
        frame.setIconImage(icon.getImage());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBackground(new Color(0,200,200));
        mainPanel.setLayout(new MigLayout("fill", "[grow] [] [grow]"));
        standardCalculatorPanel();
        mainPanel.add(calculatorPanel, "grow");
        mainPanel.add(new JSeparator(SwingConstants.VERTICAL), "pushy, growy");
        hisMemPanel();
        mainPanel.add(hisMemPanel, "grow");
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
        setupTextFields();
    }
    /**
     * Hjálparfall við uppsetningu textField tilviksbreytna notaður í smið.
     * Fyrir: tilviksbreytur af tagi textField eru grunnstilltar eins og lýst er að ofan.
     * Eftir: expressionArea hefur hlustara sem kalla á equals() hjálparfall ef ýtt er á enter, upPressed() ef
     *   ýtt er á upp örvartakkann og downPressed() ef ýtt er á niður örvatakkan.
     *   valueField hefur hlustara sem kallar á saveToMemory() fallið ef ýtt er á enter.
     *   nameField hefur nú ToolTip texta sem útskýrir hvers lags skilyrði nöfn á föstum þurfa að uppfylla, þ.e.
     *   að fastanöfn megi innihalda bókstafi og tölustafi en verði að byrja á bókstaf.
     */
    private void setupTextFields(){
        InputMap iM = expressionArea.getInputMap();
        ActionMap aM = expressionArea.getActionMap();
        iM.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "enter");
        iM.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,0), "up");
        iM.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,0), "down");
        expressionArea.addActionListener(this);
        aM.put("enter", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                eq();
                numLabel.setText(currentNum);
            }
        });
        aM.put("down", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                downPressed();
                numLabel.setText(currentNum);
                expressionArea.setText(expression);
            }
        });
        aM.put("up", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                upPressed();
                numLabel.setText(currentNum);
                expressionArea.setText(expression);
            }
        });
        nameField.setToolTipText("Names can contain letters and numbers, but must start with a letter");
        valueField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "enter");
        valueField.getActionMap().put("enter", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                saveToMemory();
                nameField.requestFocus();
            }
        });
    }
    /**
     * Hjálparfall sem byggir "Standard" reiknivélina innan frame og birtir hana. Reiknivélin er byggð upp sem grid þar 
     * sem hver sella hefur minnstu stærð sem og preferred size 40x60, nema í fyrstu röð þar sem gildin eru 20x60.
     * Í hverri röð eru fjórar sellur. Reiknivélin er skalanleg en þó er hægt að skrifa tölur og 
     * segðir nógu langar til að fara út fyrir rammann. Hægt er að loka reiknivél
     * með því að ýta á krossinn í efra vinstra horni.
     * Tveir JRadioButton eru efst, Scientific og Standard notaðir til að skipta á milli viðmóta.
     * Scientific radio button er settur sem Selected.
     */
    private void standardCalculatorPanel(){
        calculatorPanel.removeAll();
        MigLayout layout = new MigLayout("fill, wrap 4", "grow, 60:60:", 
        "[grow, 20:20:] [grow, 40:40:] [grow, 40:40:] [grow, 40:40:][grow, 40:40:][grow, 40:40:][grow, 40:40:][grow, 40:40:]");
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
        JButton leftSvigi = makeButton("(");
        JButton rightSvigi = makeButton(")");
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
        expressionArea.setHorizontalAlignment(JTextField.RIGHT);
        calculatorPanel.add(numLabel, "span, grow, gapright 10");
        numLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        calculatorPanel.add(leftSvigi, "grow");
        calculatorPanel.add(rightSvigi, "grow");
        calculatorPanel.add(clear, "grow");
        calculatorPanel.add(del, "grow");
        for(int i = 7; i<10; i++) calculatorPanel.add(nums[i],"grow");
        calculatorPanel.add(divide,"grow");
        for(int i = 4; i<7; i++) calculatorPanel.add(nums[i],"grow");
        calculatorPanel.add(multiply,"grow");
        for(int i = 1; i<4; i++) calculatorPanel.add(nums[i],"grow");
        calculatorPanel.add(minus,"grow");
        calculatorPanel.add(dot,"grow");
        calculatorPanel.add(nums[0], "grow");
        calculatorPanel.add(equals,"grow");
        calculatorPanel.add(add,"grow");
        calculatorPanel.revalidate();
    }
    /**
     * Hjálparfall sem byggir "Scientific" reiknivélina innan frame og birtir hana. Reiknivélin er byggð upp sem grid þar 
     * sem hver sella hefur minnstu stærð sem og preferred size 40x60, nema í fyrstu röð þar sem gildin eru 20x60.
     * Í hverri röð eru fjórar sellur. Reiknivélin er skalanleg en þó er hægt að skrifa tölur og 
     * segðir nógu langar til að fara út fyrir rammann. Hægt er að loka reiknivél
     * með því að ýta á krossinn í efra vinstra horni.
     * Tveir JRadioButton eru efst, Scientific og Standard notaðir til að skipta á milli viðmóta.
     * Scientific radio button er settur sem Selected.
     */

    private void scientificCalculatorPanel(){
        calculatorPanel.removeAll();
        MigLayout layout = new MigLayout("fill, wrap 6", "grow, 60:60:", 
        "[grow, 20:20:] [grow, 40:40:] [grow, 40:40:] [grow, 40:40:][grow, 40:40:][grow, 40:40:][grow, 40:40:][grow, 40:40:]");
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
        expressionArea.setHorizontalAlignment(JTextField.RIGHT);
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
    /**
     * Hjálparfall sem byggir hægri hlið reiknivélar, þ.e. hisMemPanel.
     * Eftir inniheldur hisMemPanel historyButton og memoryButton ásamt hisMemScrollPanel og scroller.
     * innan hisMemScroller eru birtir takkar sem lýsa innihaldi history Deque tilviksbreytunnar.
     * isHistory er sett sem true.
     */
    private void hisMemPanel(){
        hisMemPanel.removeAll();
        hisMemScrollPanel.removeAll();
        this.isHistory = true;
        MigLayout hisMemLayout = new MigLayout("fill, wrap 2", "grow, 60::", 
        "[grow, 30:30:] [grow, 60::] [grow, 40::] [grow, 60::][grow, 20::][grow, 20::][grow, 20::]");
        hisMemPanel.setLayout(hisMemLayout);
        historyButton = makeButton("History");
        memoryButton = makeButton("Memory");

        hisMemPanel.add(historyButton, "grow");
        hisMemPanel.add(memoryButton, "grow");
        ListIterator i = history.listIterator();
        while(i.hasNext()){
            hisMemScrollPanel.add(makeButton((String)i.next()), "grow, span");
        }
        scroller.setViewportView(hisMemScrollPanel);
        historyButton.setBackground(new Color(255,99,71));
        hisMemPanel.add(scroller, "grow, span 2 6");
        hisMemPanel.revalidate();
    }
    /**
     * Fall sem hækkar gildi á i ef i < history.size()-1 og histoyy er ekki tómt og setur expression sem 
     * reiknisegðina sem það stak lýsir og currentNum sem svarið við þeirri reiknisegð.
     */
    private void upPressed(){
        if(i == history.size()-1|| history.isEmpty()) return;
        i++;
        String full = history.get(i);
        expression = full.substring(0,full.indexOf("=") + 1);
        currentNum = full.substring(full.indexOf("=") + 1, full.length());
    }
    /**
     * Fall sem lækkar gildi á i ef i > 0 og setur expression sem 
     * reiknisegðina sem það stak lýsir og currentNum sem svarið við þeirri reiknisegð.
     */
    private void downPressed(){
        if(i <= 0) return;
        i--;
        String full = history.get(i);
        expression = full.substring(0,full.indexOf("=") + 1);
        currentNum = full.substring(full.indexOf("=") + 1, full.length());
    }
    /**
     * Hjálparfall sem fyrir memoryButton(). Birtir stök innan memory tilviksbreytunnar sem takka innan
     * hisMemScrollPanel ásamt því að birta nameField, textField og save takka og þrenn JLabel til að útskýra
     * virkni nameField, textField og save takkans.
     */
    private void memory(){
        if(!isHistory) return;
        isHistory = false;
        memoryButton.setBackground(new Color(255,99,71));
        historyButton.setBackground(Color.WHITE);
        hisMemPanel.remove(scroller);
        hisMemScrollPanel.removeAll();
        for(Map.Entry<String,String> entry: memory.entrySet()){
            hisMemScrollPanel.add(makeButton(entry.getKey() +":  " + entry.getValue()), "grow, span");
        }
        scroller.setViewportView(hisMemScrollPanel);
        hisMemPanel.add(scroller, "grow, span 2 4");
        JLabel label = new JLabel("Save new number:");
        label.setVerticalAlignment(SwingConstants.BOTTOM);
        label.setFont(label.getFont().deriveFont(17.0f));
        hisMemPanel.add(label, "grow, span");
        hisMemPanel.add(new Label("Name"), "grow");
        hisMemPanel.add(new Label("Value"), "grow");
        hisMemPanel.add(nameField, "grow");
        hisMemPanel.add(valueField, "grow");
        hisMemPanel.add(makeButton("Save"), "grow, span");
        hisMemScrollPanel.revalidate();
        hisMemPanel.revalidate(); 
    }
    /**
     * Hjálparfall sem tekur innihald nameField og valueField, uppfylli þeir strengir þær kröfur að innihald nameField
     * byrji á bókstaf og innihaldi aðeins bókstafi og tölustafi og að innihald valueField lýsi tölu þá er þessu bætt
     * við memory með innihald nameField sem key og innihald valueField sem value. Birtir einnig takka fyrir þetta gildi
     * innan history flipans.
     */
    private void saveToMemory(){
        Pattern p = Pattern.compile("[A-Za-z]{1}[A-Za-z0-9]*");
        String name = nameField.getText();
        Matcher m = p.matcher(name);
        if(!m.matches()){
            errorMessage("Invalid name");
            return;
        }
        String value = valueField.getText();
        try{
            Double.parseDouble(value);
        }
        catch(Exception e){
            errorMessage("Invalid number");
            return;
        }
        memory.put(name,value);
        hisMemScrollPanel.add(makeButton(name + ": " + value), "grow, span");
        nameField.setText("");
        valueField.setText("");
        hisMemScrollPanel.revalidate();
        hisMemPanel.revalidate();
    }
    /**
     * Hjálparfall sem skilar JButton takka með actionListener sem bendir á Calculator hlutinn sjálfan.
     * Takkinn er hvítur almennt en grár ef innihald hans er tala.
     * @param s er strengur sem lýsir titlinum á takkanum
     * @return JButton með actionListener sem bendir á hlutinn sjálfan með titil s.
     */
    private JButton makeButton(String s){
        JButton retButton = new JButton(s);
        if(isNumber(s))
            retButton.setBackground(new Color(210,210,210));
        else
            retButton.setBackground(Color.WHITE);
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
        if(expression.length() > 0){
            expression = expression.substring(0, expression.length()-1);
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
        expression = expression.concat(" "+ s +" ");
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
     * Fyrir: currentNum er strengur sem lýsir heiltölu. expression er strengur. shouldChange er annaðhvort true
     *  eða false.
     * Eftir: Ef shouldChange var true er currentNum nú heiltalan sem bætt var við.
     *  Ef shouldChange var false er currentNum nú sami strengur að viðbættri með num strenginn
     *  skeyttan aftan við. num hefur verið bætt aftan við expression.
     *  shouldChange er nú stillt sem false.
     */
    private void addNum(String num){
        expression = expression.concat(num);
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
     *  sem true. Reiknisegðarstrengur að viðskeyttu svari hefur bæst við history og birtur í history flipa.
     */
    private void eq(){
        shouldChange = true;
        expression = expressionArea.getText();
        String evalExpression = "";
        if(!expression.contains("=")) evalExpression = expression.concat(" = ");
        else{
            evalExpression = expression;
            expression = expression.substring(0,expression.indexOf("="));
        } 
        expressionArea.setText(evalExpression);
        Evaluate eval = new Evaluate(expression, memory);
        currentNum = eval.getNumber();
        if(currentNum.equals("NaN")){
            errorMessage("Invalid Expression");
            return;
        }
        history.addFirst(evalExpression + currentNum);
        hisMemPanel();
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
     * Hjálparfall sem bætir upphrópunarmerki aftan við expression og setur shouldChange sem true.
     */
    private void hrop(){
        expression = expression.concat("!");
        shouldChange = true;
    }
    /**
     * Hjálparfall við takkana fyrir föllin tölugildi, kvaðratrót og ln bætir við expression.
     * @param s er strengur sem lýsir því hvaða fall á að bæta í expression.
     * Eftir: Við expression hefur bæst strengurinn s ásamt vinstri sviga. shouldChange er nú true.
     */
    private void addFunc(String s){
        expression = expression.concat(s + "(");
        shouldChange = true;
    }
    /**
     * Hjálparfall við að bæta við aðferðum sem beita veldistákni.
     * @param s innihald takkans sem ýtt var á. Annaðhvort x^y, x^2 eða 10^
     * Eftir: við expression hefur bæst ^ ef s var x^y, ^2 ef s var á x^2 eða 10^ ef s var
     * 10^. shouldChange nú stillt sem true.
     */
    private void addPow(String s){
        if(s.contains("x")&&s.contains("y")){
            expression = expression.concat(s.substring(1,s.length()-1));
        }
        else if(s.contains("x")) expression = expression.concat(s.substring(1,s.length()));
        else expression = expression.concat("10^");
        shouldChange = true;
    }
    /**
     * Hjálparfall sem bætir lokunarsviga aftan við expression.
     * shouldChange nú stillt sem true.
     */
    private void rightApostrophe(){
        expression = expression.concat(")");
        shouldChange = true;
    }
    /**
     * Hjálparfall fyrir takka innan hisMemScrollPanel ef history er opið.
     * @param expression er strengur sem lýsir reiknisegð sem á að skoða aftur.
     * Birtir reiknisegðarhluta expression innan expressionArea og setur currentNum sem svarið
     * við reiknisegðinni.
     */
    private void historyPressed(String expression){
        expressionArea.setText(expression.substring(0, expression.indexOf("=")));
        currentNum = expression.substring(expression.indexOf("=")+1, expression.length());
        shouldChange = true;
        expression = "";
    }
    /**
     * Bætir fasta við reiknisegð
     * @param constant er strengur sem lýsir fasta sem á að bæta við reiknisegð
     * Ef shouldChange var true er expression nú fastinn. Ef shouldChange var false hefur constant
     * bæst aftan við expression.
     * shouldChange er nú true.
     */
    private void addConstant(String constant){
        if(shouldChange == true) expression = constant;
        else expression = expression.concat(constant + " ");
        currentNum = constant;
        shouldChange = true;
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
        i = 0;
        String actionName;
        try{
            actionName = ((JButton)(e.getSource())).getText();
        }
        catch(Exception exc){
            actionName = ((JRadioButton)(e.getSource())).getText();
        }
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
            hisMemPanel();
            mainPanel.repaint();
        }
        else if(actionName.equals("Memory")){
            memory();
            mainPanel.repaint();
        }
        else if(actionName.equals("Clear")) clear();
        else if(actionName.contains("^")) addPow(actionName);
        else if(actionName.equals("n!")) hrop();
        else if(actionName.equals("(")) addFunc("");
        else if(actionName.equals(")")) rightApostrophe();
        else if(actionName.equals(".")) addDot();
        else if(actionName.equals("=")) eq();
        else if(actionName.contains(" = ")) historyPressed(actionName);
        else if(actionName.equals("√x")) addFunc("sqrt");
        else if(actionName.equals("ln")) addFunc("ln");
        else if(actionName.equals("|x|")) addFunc("abs");
        else if(isNumber(actionName)) addNum(actionName);
        else if(actionName.equals("Save")) saveToMemory();
        else if(actionName.contains(":")) addConstant(actionName.substring(0,actionName.indexOf(":")));
        else if(actionName.equals("e")||actionName.equals("π")) addConstant(actionName);
        else addOp(actionName);
        numLabel.setText(currentNum);
        if(!actionName.contains("="))
            expressionArea.setText(expression);
    }
    public static void main(String[] args){
        Runnable evt = ()->{
            new Calculator();
        };
        EventQueue.invokeLater(evt);
    }
}
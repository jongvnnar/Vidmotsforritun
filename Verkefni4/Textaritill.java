/**
 * Klasi sem birtir einfaldan textaritil.
 * Má keyra með því að gera eftirfarandi skipanir:
 * SET CLASSPATH=.;miglayout-core-5.3-20190920.220054-302.jar;miglayout-swing-5.3-20190918.220057-300.jar
 * javac Textaritill.java
 * java Textaritill
 * fyrir Windows OS.
 * 
 * Sé maður að nota LINUX skal keyra
 * CLASSPATH=.:miglayout-core-5.3-20190920.220054-302.jar:miglayout-swing-5.3-20190918.220057-300.jar
 * javac Textaritill.java
 * java Textaritill
 * 
 * Vilji maður opna ákveðan skrá í textaritlinum við keyrslu má bæta við slóð á skránna aftan við 'Textaritill'
 * við keyrslu.
 * Textaritillinn styður aðgerðirnar að vista skrá benit, opna nýja skrá, vista núverandi skrá
 * á nýjan stað og að opna nýja skrá.
 * 
 * @author Jón Gunnar Hannesson
 * @author jgh12@hi.is
 */
import java.io.*;
import java.nio.file.*;
import javax.swing.*;
import java.awt.*;
import net.miginfocom.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
public class Textaritill implements ActionListener{
    /**
     * Fastayrðing gagna:
     * frame er JFrame rammi sem inniheldur textArea sem birtir innihald skráa ásamt.
     * currentPath vísar á þá skrá sem notandi skrifar á skipanalínu í upphafi og currentFile einnig.
     * Ef ekki er vísað á neina skrá vísar currentPath á tómastrengsskrá og currentFile á null.
     * Sé umbeðin skrá ekki til vísa currentPath og currentFile á þá skrá, en athugað er hvort skrá sé til
     * áður en aðgerðir sem það varða eru framkvæmdar.
     */
    private JFrame frame = new JFrame("Textaritill");
    private JTextArea textArea = new JTextArea();
    private Path currentPath;
    private File currentFile;

    /**
     * Smiður fyrir tilvik af Textaritli.
     * @param path er strengur sem vísar á það skjal sem opnast á við keyrslu.
     * Eftir: Nýr textaritill opnast með nafn skráar efst í glugga ef skrá finnst
     * Sé umbeðin skrá ekki til opnast textaritill með "New File" sem nafn og upp kemur villuboð á skjá.
     */
    public Textaritill(String path){
        currentPath = Paths.get(path);
        currentFile = currentPath.toFile();
        String fileName = currentPath.getFileName().toString();
        if(currentFile.exists()){
            makeEditor(fileName);
        }
        else{
            makeEditor("New File");
            errorMessage("Could not find file");
        }
        write();
    }

    /**
     * Smiður fyrir tilvik af textaritli.
     * Eftir: Nýr textaritill opnast í skrá að nafni "New File", sem bendir ekki neitt innan minnis.
     */
    public Textaritill(){
        makeEditor("New File");
        currentPath = Paths.get("");
        currentFile = null;
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
     * Hjálparfall við að búa til valblað
     * @return valblað af tagi JMenuBar sem inniheldur takka til að vista, vista sem, opna og búa til nýja skrá
     * Allir takkar benda á sjálfan textaritilshlutinn sem actionListener og eru þau boð útfærð neðar til að
     * framkvæma aðgerðirnar sem notandi áætlar að takkarnir geri.
     */
    private JMenuBar makeMenu(){
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(this);
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(this);
        JMenuItem saveAs = new JMenuItem("Save As");
        saveAs.addActionListener(this);
        JMenuItem newFile = new JMenuItem("New");
        newFile.addActionListener(this);
        menu.add(save);
        menu.add(saveAs);
        menu.add(newFile);
        menu.add(open);
        menuBar.add(menu);
        return menuBar;
    }
    /**
     * Hjálparfall til að lesa úr skránni sem currentPath bendir á og skrifa inn í textArea textaritils.
     * Sé skráin ekki til, eða sé hún tóm er tómistrengurinn skrifaður inn í textArea.
     */
    private void write(){
        String content;
        try{
            content = new String(Files.readAllBytes(this.currentPath),"UTF-8");
        }
        catch(Exception e){
            content = "";
        }
        this.textArea.setText(content);
    }
    /**
     * Fall til að vista. Skrifar í skránna sem tilviksbreytur benda á.
     */
    private void save() throws IOException{
        Files.write(currentPath, textArea.getText().getBytes("UTF-8"));
    }
    /**
     * Fall sem býr til nýja tóma skrá, sem bendir ekki neinsstaðar. Fyrst er skráin sem notandi
     * hefur verið að vinna í vistuð.
     */
    private void newFile(){
        try{
            save();
        } catch(Exception e){
            saveAs();
        }
        currentPath = Paths.get("");
        currentFile = null;
        textArea.setText("");
        frame.setTitle("New File");
    }
    /**
     * Opnar glugga til að velja nýja skrá og rita yfir hana eða skrifa inn nýtt skráarnafn og búa þá til nýja skrá.
     * Skilar villuboði ef Það text ekki að vista nýju skránna, eða ef ekki text að búa hana til.
     * Setur nýtt nafn skránnar efst á textaritilsgluggann.
     */
    private void saveAs(){
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION){
            currentFile = chooser.getSelectedFile();
            currentPath = Paths.get(currentFile.getPath());
            try{
                currentFile.createNewFile();
                save();
            }
            catch(Exception exc){
                errorMessage("Error while saving file");
            }
            frame.setTitle(currentFile.getName());
        }
    }
    /**
     * Opnar glugga til að velja skrá sem er til og opna hana. Sé notandi nú þegar í skrá með gildri slóð opnast
     * valglugginn í möppunni sem sú skrá er í. Sé notandi í skrá með ógildri slóð opnast valglugginn á
     * heimasvæði notanda.
     */
    private void open(){
        JFileChooser chooser = new JFileChooser();
        if(currentFile != null && currentFile.exists()) chooser.setCurrentDirectory(currentFile.getParentFile());
        int result = chooser.showOpenDialog(frame);
        if(result == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            currentPath = Paths.get(file.getPath());
            currentFile = file;
            frame.setTitle(currentFile.getName());
            write();
        }
    }
    /**
     * Fall sem byggir textaritilinn innan rammans og birtir. Upphafsstærð er sett sem 400x400 pixlar. Styður skrun
     * og birtir valblað efst á skjánum. Hægt er að loka editor með því að ýta á krossinn efst í rammanum.
     */
    private void makeEditor(String title){
        frame.setLayout(new MigLayout());
        frame.setTitle(title);
        JScrollPane scroller = new JScrollPane();
        scroller.setSize(new Dimension(400,400));
        scroller.setPreferredSize(new Dimension(400,400));
        scroller.setViewportView(this.textArea);
        frame.setJMenuBar(makeMenu());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
        frame.add(scroller,"push, grow");
        frame.pack();
        frame.setVisible(true);
    }
    /**
     * Framkvæmir aðgerðirnar lýst að ofan þegar ýtt er á viðeigandi hnappa með því að kalla í samnefnd föll.
     */
    public void actionPerformed(ActionEvent e){
        String actionName = ((JMenuItem)(e.getSource())).getText();
        if(actionName.equals("Save")){
            try{
                save();
            }
            catch(Exception exc){
                saveAs();
            }
        }
        else if(actionName.equals("Save As")) saveAs();
        else if(actionName.equals("Open")) open();
        else if(actionName.equals("New")) newFile();
    }
    public static void main(String[] args){
        Runnable evt = ()->{
        try{
            new Textaritill(args[0]);
        }
        catch(Exception e){
            new Textaritill();
        }
        };
        EventQueue.invokeLater(evt);
    }
}
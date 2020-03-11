import java.io.*;
import java.nio.file.*;
import javax.swing.*;
import java.awt.*;
import net.miginfocom.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
public class Textaritill implements ActionListener{
    private JFrame frame = new JFrame("Textaritill");
    private JTextArea textArea = new JTextArea();
    private Path currentPath;
    private File currentFile;
    public Textaritill(String path){
        currentPath = Paths.get(path);
        currentFile = currentPath.toFile();
        String fileName = currentPath.getFileName().toString();
        System.out.println(currentFile.exists());
        if(currentFile.exists()){
            makeEditor(fileName);
        }
        else{
            makeEditor("New File");
            errorMessage("Could not find file");
        }
        write();
    }

    public Textaritill(){
        makeEditor("New File");
        currentPath = Paths.get("");
        currentFile = null;
    }
    private void errorMessage(String s){
        JOptionPane.showMessageDialog(frame, s,
        "Error",JOptionPane.ERROR_MESSAGE);
    }
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

    private void save() throws IOException{
        Files.write(currentPath, textArea.getText().getBytes("UTF-8"));
    }

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
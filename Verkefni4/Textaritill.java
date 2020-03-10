import java.io.*;
import java.nio.file.*;
import javax.swing.*;
import java.awt.*;
import net.miginfocom.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
public class Textaritill{
    private JFrame frame = new JFrame("Textaritill");
    private JTextArea textArea = new JTextArea();
    private Path currentPath;
    private File currentFile;
    public Textaritill(String path){
        currentPath = Paths.get(path);
        currentFile = currentPath.toFile();
        String fileName = currentPath.getFileName().toString();
        makeEditor(fileName);
        write();
    }

    public Textaritill(){
        makeEditor("New File");
        currentPath = Paths.get("");
        currentFile = currentPath.toFile();
    }

    private JMenuBar makeMenu(){
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Tools");
        JMenuItem open = makeOpen();
        JMenuItem save = makeSave();
        JMenuItem saveAs = makeSaveAs();
        JMenuItem newFile = new JMenuItem("New");
        menu.add(save);
        menu.add(saveAs);
        menu.add(newFile);
        menu.add(open);
        menuBar.add(menu);
        return menuBar;
    }

    private  void write(){
        String content;
        try{
            content = new String(Files.readAllBytes(this.currentPath),"UTF-8");
        }
        catch(Exception e){
            content = "";
        }
        this.textArea.setText(content);
        frame.setTitle(currentFile.getName());
    }

    private JMenuItem makeOpen(){
        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                        JFileChooser chooser = new JFileChooser();
                        if(currentFile.exists()) chooser.setCurrentDirectory(currentFile.getParentFile());
                        int result = chooser.showOpenDialog(frame);
                        if(result == JFileChooser.APPROVE_OPTION){
                            File file = chooser.getSelectedFile();
                            currentPath = Paths.get(file.getPath());
                            currentFile = file;
                            write();
                        }
                    }
                }
            );
        return open;
    }
    private JMenuItem makeSave(){
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    try{
                    save();
                    }
                    catch(Exception exc){
                    saveAs();
                    }
                }
        });
        return save;
    }
    private void save() throws IOException{
        Files.write(currentPath, textArea.getText().getBytes("UTF-8"));
    }
    private JMenuItem makeSaveAs(){
        JMenuItem saveAs = new JMenuItem("Save as...");
        saveAs.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    saveAs();
                }
        });
        return saveAs;
    }
    private void newFile(){
        try{
            save();
        } catch(Exception e){
            saveAs();
        }
        if(currentFile.exists())
            currentPath = Paths.get(currentFile.getParentFile().getPath());
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
            catch(Exception e){
                //Do something?
            }
            frame.setTitle(currentFile.getName());
        }
    }

    public void makeEditor(String title){
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

    public static void main(String[] args){
        Runnable evt = ()->{
        try{
            Textaritill textaritill = new Textaritill(args[0]);
        }
        catch(Exception e){
            Textaritill textaritill = new Textaritill();
        }
        };
        EventQueue.invokeLater(evt);
    }
}
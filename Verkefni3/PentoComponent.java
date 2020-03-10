/**
 * Klasi sem erfir frá JComponent og er notaður til að birta Pentomino þrautir.
 * @author Jón Gunnar Hannesson
 * @author jgh12@hi.is
 * Klasann má nota með því að keyra fyrst
 * SET CLASSPATH=.;miglayout-core-5.3-20190920.220054-302.jar;miglayout-swing-5.3-20190918.220057-300.jar
 */
import java.awt.*;
import javax.swing.*;
import java.beans.PropertyChangeSupport;
import javax.swing.BorderFactory;
import java.awt.geom.Rectangle2D;
public class PentoComponent extends JComponent{
    // Fastayrðing gagna.
    // board inniheldur hvaða strengjafylki sem er en er ætlað Pentominos lýkt og í Pento.java
    // width og height eru tilviksbreytur sem halda utan um hæð borðsins, miðað við að hver stafur 
    // í board sé ferningur með hliðarlengdir 50.0. 
    private String[] board = {""};
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private int width;
    private int height;
    private Insets ins;
        /**
         * Smiður fyrir nýjan PentoComponent.
         * Notkun: PentoComponent p = new PentoComponent();
         * Eftir: p vísar á tilvik af PentoComponent, sem má nota til að birta Pentomino þrautir.
         */
    public PentoComponent(){
        setBorder(BorderFactory.createTitledBorder("Pentominoes"));
        this.ins = this.getInsets();
        this.width = 250 + this.ins.left;
        this.height = 250 + this.ins.top;
        this.setPreferredSize(new Dimension(width,height));
    }
        /**
         *Gefur hverjum Pentomino F,I,L,P,N,T,U,V,W,X,Y,Z sér lit, ' ' fær hvítan lit, 
         *stjörnur og annað fá engan lit.
         *
         * @param c Stafur sem lýsir Pentomino
         * @return Litur fyrir hvern Pentomino
         */
    private Color getCharColor(char c){
        Color col = null;
        switch(c){
            case' ':
                col = new Color(255,255,255);
                break;
            case'F':
                col= new Color(255,0,0);
                break;
            case'I':
                col= new Color(0,255,0);
                break;
            case'L':
                col= new Color(255,255,0);
                break;
            case'P':
                col= new Color(255,0,255);
                break;
            case'N':
                col= new Color(0,255,255);
                break;
            case'T':
                col= new Color(128,0,0).brighter();
                break;
            case'U':
                col= new Color(0,128,0).brighter();
                break;
            case'V':
                col= new Color(240,128,128);
                break;
            case'W':
                col= new Color(238,232,170);
                break;
            case'X':
                col= new Color(127,255,212);
                break;
            case'Y':
                col= new Color(186,85,211);
                break;
            case'Z':
                col= new Color(0,164,96);
                break;
            default:
                col = getBackground();
        }
        return col;
    }
        /**
         * Setur nýtt borð til að birta sem PentoComponent.
         * Endurbirtir borð ef því er breytt og sendir út PropertyChange.
         * @param board strengjafylki sem lýsir nýja borðinu
         */
    public void setBoard(String[] board){
        String[] old = new String[this.board.length];
        for(int i = 0; i<this.board.length; i++){
            old[i] = this.board[i];
        }
        this.board = new String[board.length];
        for(int i = 0; i<board.length; i++){
            this.board[i] = board[i];
        }
        Dimension size = computeMinimumSize();
        this.setMinimumSize(size);
        this.setPreferredSize(size);
        this.setSize(size);
        this.repaint();
        pcs.firePropertyChange("board", old, board);
    }
        /**
         * Hjálparfall við að reikna stærð borðs.
         * @return minnsta stærð sem PentoComponent getur verið.
         */
    private Dimension computeMinimumSize(){
         ins = this.getInsets();
         int insetHeight = ins.top + ins.bottom;
         int insetWidth = ins.right + ins.left;
         int max = 1;
         int local = 0;
         for(int i = 0; i<board.length; i++){
             local = board[i].length();
             if(max < local) max = local;
         }
         width = max*50 + insetWidth;
         height = board.length*50 + insetHeight;
         return new Dimension(width, height);
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
         Graphics2D g2d = (Graphics2D)g;
         for(int i = 0; i<board.length; i++){
             for(int j = 0; j<board[i].length(); j++){
                Rectangle2D rect = new Rectangle2D.Double(this.ins.left + 50.0*j,this.ins.top + 50.0*i, 50.0,50.0);
                g2d.setColor(getCharColor(board[i].charAt(j)));
                g2d.fill(rect);
             }
         }
    }
    // public static void main(String[] args){
    //     Runnable evt =
    //         () ->
    //         {
    //     String[][] board = {{"FLLLLZZY", 
    //                         "FFFWLZYY",
    //                         "VFWWZZPY",
    //                         "VWW  PPY",
    //                         "VVV  PPT",
    //                         "UUXNNTTT",
    //                         "UXXXNNNT",
    //                         "UUXIIIII",},{
    //                         "*WW***VVVY*",
    //                         "WW***UUUVYY",
    //                         "WX***UZUVYI",
    //                         "XXX***ZZZYI",
    //                         "NXFFP***ZLI",
    //                         "NFFTPP***LI",
    //                         "NNFTPP***LI",
    //                         "*NTTT***LL*",},{
    //                             ""
    //                         }};
    //             JFrame g = new JFrame();
    //             g.setLayout(new MigLayout());
    //             g.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    //             PentoComponent pen = new PentoComponent();
    //             JButton left = new JButton("Vinstri");
    //             int i =1;
    //             left.addActionListener(
    //                 new ActionListener()
    //                 {
    //                     @Override
    //                     public void actionPerformed( ActionEvent e )
    //                     {
    //                         EventQueue.invokeLater(()-> {
    //                             i--;
    //                             pen.setBoard(board[i]);
    //                         });
    //                     }
    //                 });
    //             JButton right = new JButton("Hægri");
    //             pen.setBoard(board[0]);
	// 			g.add(pen,"push,grow,wrap");
    //             g.add(left,"span, split 2, center");
    //             g.add(right,"span, split 2, center, wrap");
    //             g.pack();
	// 			g.setVisible(true);
    //         };
	// 	EventQueue.invokeLater(evt);
    // }
}

//TODO
// Setja maxdimension sem viewport
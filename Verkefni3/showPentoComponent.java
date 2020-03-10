// Einfalt viðmót til að birta PentoComponent fyrir þrjú borð.
// Má keyra með því að gera skipanir

// SET CLASSPATH=.;miglayout-core-5.3-20190920.220054-302.jar;miglayout-swing-5.3-20190918.220057-300.jar
// javac -encoding utf8 ShowPentoComponent.java
// java ShowPentoComponent

// fyrir Windows OS.
// Sé maður að nota LINUX skal keyra
// CLASSPATH=.:miglayout-core-5.3-20190920.220054-302.jar:miglayout-swing-5.3-20190918.220057-300.jar
// javac -encoding utf8 ShowPentoComponent.java
// java ShowPentoComponent
// Höfundur: Jón Gunnar Hannesson, 2020

import net.miginfocom.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
public class showPentoComponent{
    private static int i = 1;
    public static void main(String[] args){
        Runnable evt =
            () ->
            {
        String[][] board = {{"FLLLLZZY", 
                            "FFFWLZYY",
                            "VFWWZZPY",
                            "VWW  PPY",
                            "VVV  PPT",
                            "UUXNNTTT",
                            "UXXXNNNT",
                            "UUXIIIII",},{
                            "*WW***VVVY*",
                            "WW***UUUVYY",
                            "WX***UZUVYI",
                            "XXX***ZZZYI",
                            "NXFFP***ZLI",
                            "NFFTPP***LI",
                            "NNFTPP***LI",
                            "*NTTT***LL*",},{
                                ""
                            }};
                JFrame g = new JFrame();
                g.setLayout(new MigLayout());
                g.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                PentoComponent pen = new PentoComponent();
                JButton left = new JButton("Vinstri");
                JButton right = new JButton("Hægri");
                left.addActionListener(
                    new ActionListener()
                    {
                        @Override
                        public void actionPerformed( ActionEvent e )
                        {
                            EventQueue.invokeLater(()-> {
                                if(i != 0){
                                pen.setBoard(board[--i]);
                                g.pack();
                                }
                            });
                        }
                    });
                right.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed( ActionEvent e )
                    {
                        EventQueue.invokeLater(()-> {
                            if(i != board.length-1){
                            pen.setBoard(board[++i]);
                            g.pack();
                            }
                        });
                    }
                });
                pen.setBoard(board[0]);
				g.add(pen,"grow,wrap");
                g.add(left,"span, split 2, center");
                g.add(right,"span, split 2, center, wrap");
                g.pack();
				g.setVisible(true);
            };
		EventQueue.invokeLater(evt);
    }
}

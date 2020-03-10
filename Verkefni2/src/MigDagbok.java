import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import net.miginfocom.swing.*;
import java.text.*;
import com.toedter.calendar.JCalendar;
import java.beans.*;
public class MigDagbok
{
    static JCalendar theCalendar;
    static javax.swing.JTextArea theTextArea;
    static DiaryData d;
	public static void main( String args[] )
    {
        Runnable evt =
            () ->
            {
                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf2.format(new java.util.Date().getTime());
                String startTime = sdf1.format(new java.util.Date().getTime());
                d = new DiaryData("dagbokVidmot", date);

                javax.swing.JFrame g = new javax.swing.JFrame(startTime);
                    new Klukka(1000,()->{g.setTitle(date +"  "+ Klukka.timeString());});
				g.setLayout
					(   new net.miginfocom.swing.MigLayout
						(   "wrap 1"				// ein sъla
						,   "[grow 100]"			// sъlan breikkar
						,   "[grow 0][grow 100]"	// textasvжрiр stжkkar, en dagataliр ekki
						)
					);
                theCalendar = new com.toedter.calendar.JCalendar(java.util.Locale.forLanguageTag("is-IS"));
                theCalendar.addPropertyChangeListener(new PropertyChangeListener(){
                    @Override
                    public void propertyChange(PropertyChangeEvent e){
                        d.setText(theTextArea.getText());
                        d.save();
                        d.setDate(sdf2.format(theCalendar.getDate()));
                        theTextArea.setText(d.getText());
                    }
                });
                g.addWindowListener(new WindowAdapter(){
                    public void windowClosing(WindowEvent evt){
                        d.setText(theTextArea.getText());
                        d.save();
                        System.exit(0);
                    }
                });
                g.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

                theTextArea = new javax.swing.JTextArea();
                theTextArea.setText(d.getText());
				g.add(theCalendar,"center");
				javax.swing.JScrollPane sp = new javax.swing.JScrollPane();
				sp.setViewportView(theTextArea);
				g.add(sp,"grow");
                g.pack();
				g.setVisible(true);
            };
		java.awt.EventQueue.invokeLater(evt);
	}
}
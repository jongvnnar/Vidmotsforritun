import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;

// Tilvik af klasanum Klukka eru klukkur sem láta
// vita af því þegar klukkan breytist, á tilteknum
// fresti.  Einnig inniheldur klasinn fall sem skilar
// tímanum á textasniði.

// Höfundur: Snorri Agnarsson

public class Klukka
{
    private Runnable myRunnable;
    private javax.swing.Timer myTimer;
    // Fastayrðing gagna:
    //  Hlutir af þessu tagi eru óbreytanlegir eftir smíð.
    //  myRunnable innihaldur ávallt tilvísun á Runnable hlut og
    //  myTimer inniheldur tilvísun á timer hlut sem tifar með
    //  föstu millibili. Millibilið er tiltekið í smiðnum fyrir
    //  hluti af tagi Klukka.  Í hvert skipti sem myTimer tifar
    //  er myRunnable keyrður.
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

    // Notkun: String time = Klukka.timeString();
    // Fyrir:  Ekkert.
    // Eftir:  time vísar á streng á sniðinu hh:mm:ss sem er
    //         núverandi klukka miðað samkvæmt tölvunni.
    public static String timeString()
    {
        return sdf.format(new java.util.Date().getTime());
    }

    // Notkun: Klukka k = new Klukka(delay,action);
    // Fyrir:  delay er jákvæð heiltala, action vísar á
    //         hlut af tagi Runnable.
    // Eftir:  k vísar á hlut af tagi Klukka sem hefur
    //         verið ræstur og tifar með millibili sem
    //         er delay millisekúndur. Í hvert skipti
    //         sem k tifar keyrist action.run().
    public Klukka( int delay, Runnable action )
    {
        myRunnable = action;
        ActionListener a = 
            new ActionListener()
            {
                public void actionPerformed( ActionEvent evt )
                {
                    myRunnable.run();
                }
            };
        myTimer = new javax.swing.Timer(delay,a);
        myTimer.start();
    }

    // Notkun: Klukka.main(args);
    // Eftir:  Búið er að láta klukku tifa með einnar sekúndu millibili
    //         í 10 sekúndur. Hvert tif veldur útskrift á línu með
    //         tímanum.
    public static void main( String[] args ) throws InterruptedException
    {
        new Klukka(1000,()->{System.out.println(Klukka.timeString());});
        Thread.sleep(10000);
        System.exit(0);
    }
}
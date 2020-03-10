// Tilvik af klasanum DiaryData gefa aðgang að dagbókartextum
// fyrir hvaða dagsetningu sem er.  Forsendan er sú að texti
// fyrir dagsetningu yyyy-mm-dd er geymdur í skrá með nafninu
// yyyy-mm-dd (fyrir viðeigandi gildi á ári, mánuði og degi)
// í möppu innan heimamöppu notandans, sem tiltekin er þegar
// tilvikið af DiaryData er smíðað.

// Höfundur: Snorri Agnarsson

import java.nio.file.*;

public class DiaryData
{
    private String dir;
    private String date, orgtext, text;

    // Fastayrðing gagna:
    //  dir inniheldur fulla slóð á möppu sem inniheldur texta allra
    //  daga sem dagbókin inniheldur.
    //  Texti hvers dags með dagsetningu yyyy-mm-dd er í skrá með
    //  heitinu yyyy-mm-dd (með viðeigandi stafi fyrir yyyy, mm og dd).
    //  date inniheldur streng á sniðinu yyyy-mm-dd sem inniheldur
    //  dagsetningu dagsins sem verið er að vinna með (vinnudagurinn).
    //  text inniheldur textann sem fylgir vinnudeginum, með áorðnum
    //  breytingum.
    //  orgtext inniheldur texta vinnudagsins eins og mappan inniheldur
    //  í skráakerfinu.
    //  Ef mappan inniheldur ekki neinn texta fyrir vinnudaginn þá er
    //  orgtext tómi strengurinn "".
    //  Engin af skráunum í dir sem standa fyrir dagsetningu á að vera
    //  tóm. Þess í stað eyðum við þeim skrám sem ekki innihalda neinn
    //  texta.
    
    // Notkun: DiaryData d = new DiaryData(dir,date);
    // Fyrir:  dir er nafn á möppu, innan heimamöppu tölvunotandans, sem
    //         inniheldur dagbókartexta í skrám með nöfnum sem samsvara
    //         dagsetningum, þ.e. nöfnin eru á sniðinu yyyy-mm-dd.
    //         date er strengur á sniðinu yyyy-mm-dd, sem inniheldur
    //         löglega dagsetningu.
    // Eftir:  d er hlutur sem veitir aðgengi að innihaldi möppunnar.
    //         Núverandi dagur (vinnudagurinn) í d er date.
    public DiaryData( String dir, String date )
    {
        this.dir = Paths.get(System.getProperty("user.home"),dir).toString();
        this.date = date;
        this.text = FileOps.read(dir,date);
        orgtext = text;
    }
    
    // Notkun: String text = d.getText();
    // Fyrir:  Ekkert (nema að d vísar á DiaryData).
    // Eftir:  text inniheldur textann fyrir núverandi dagsetningu d.
    //         Ef núverandi dagsetningin hefur engan texta þá er text
    //         tómi strengurinn "".
    public String getText()
    {
        return text;
    }
    
    // Notkun: String date = d.getDate();
    // Fyrir:  Ekkert (nema að d vísar á DiaryData).
    // Eftir:  date inniheldur núverandi dagsetningu d.
    //         Dagsetningin er strengur á sniðinu yyyy-mm-dd.
    public String getDate()
    {
        return date;
    }

    // Notkun: d.setText(text);
    // Fyrir:  text er strengur, ekki null (og d vísar á DiaryData).
    // Eftir:  Textinn sem samsvarar núverandi núverandi dagsetningu
    //         er nú textinn í text.  Athugið að þessi texti er ekki
    //         skrifaður í skráakerfið í þessari aðgerð. Til þess
    //         þarf að nota save() aðgerðina eða breyta núverandi
    //         dagsetningu með setDate().
    public void setText( String text )
    {
        this.text = text;
    }
    
    // Notkun: boolean c = d.isChanged();
    // Fyrir:  Ekkert (nema að d vísar á DiaryData).
    // Eftir:  c er satt þá og því aðeins að texti núverandi dagsetningar
    //         sé ekki sami og upphaflegi textinn, þ.e. textinn sem geymdur
    //         er í samsvarandi skrá.
    public boolean isChanged()
    {
        return !text.equals(orgtext);
    }

    // Noktun: d.setDate(date);
    // Fyrir:  date er strengur sem inniheldur löglega dagsetningu
    //         á sniðinu yyyy-mm-dd (og d vísar á DiaryData).
    // Eftir:  Búið er að vista texta þeirrar dagsetningar sem var
    //         núverandi fyrir kallið og breyta núverandi dagsetningu
    //         í date og uppfæra núverandi texta til samræmis.
    public void setDate( String date )
    {
        save();
        orgtext = text;
        this.date = date;
        this.text = FileOps.read(dir,date);
        orgtext = text;
    }

    // Notkun: d.save();
    // Fyrir:  Ekkert (nema að d vísar á DiaryData).
    // Eftir:  Búið er að vista núverandi texta núverandi
    //         dagsetningar og innihald dagbókarskráa er því
    //         að fullu uppfært.
    public void save()
    {
        if( !isChanged() ) return;
        try
        {
            if( text.equals("") )
                FileOps.delete(dir,date);
            else
                FileOps.write(dir,date,text);
        }
        catch( Exception e )
        {
            // Þetta á ekki að gerast ef mappan er til staðar
            // og hefur réttar heimildir.
            e.printStackTrace();
        }
    }
    
    // Notkun: boolean e = d.exists(date);
    // Fyrir:  date er strengur á sniðinu yyyy-mm-dd sem
    //         stendur fyrir löglega dagsetningu (og d vísar
    //         á DiaryData).
    // Eftir:  e er satt þá og því aðeins að það sé einhver
    //         dagbókartexti sem fylgir dagsetningunni.
    public boolean exists( String date )
    {
        return FileOps.exists(dir,date);
    }
    
    // Notkun: main(args);
    // Fyrir:  Mappan diary er til í heimasvæði notandans.
    // Eftir:  Búið er að prófa þennan klasa örlítið og ef
    //         villa fannst þá koma villuboð.
    public static void main( String[] args )
    {
        DiaryData d = new DiaryData("diary","2020-01-26");
        String save = d.getText();
        d.setText("new text");
        d.setDate("2020-01-27");
        d.setDate("2020-01-26");
        if( !d.getText().equals("new text") ) throw new Error();
        d.setText(save);
        if( !d.getText().equals(save) ) throw new Error();
        d.save();
        if( !d.getText().equals(save) ) throw new Error();
    }
}
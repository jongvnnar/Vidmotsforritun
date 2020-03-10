import java.io.*;
import java.nio.file.*;

// Klasinn FileOps inniheldur aðgerðir sem gera okkur kleift að lesa
// og skrifa heilar textaskrár auk aðgerðar til að eyða skrá og aðgerðar
// til að athuga hvort skrá er til.
public class FileOps
{
    // Notkun: String text = FileOps.read(dir,fname);
    // Fyrir:  dir er slóð sem vísar á möppu.
    //         fname er nafn skrár í möppunni sem inniheldur texta
    //         í UTF-8 stafakóðun.
    // Eftir:  text er innihald skrárinnar.
    // Afbrigði: Ef skráin er ekki til þá inniheldur text tóma
    //         strenginn "".
    static public String read( String dir, String fname )
    {
        Path path = Paths.get(dir,fname);
        String result;
        try
        {
            result = new String(Files.readAllBytes(path),"UTF-8");
        }
        catch( Exception e )
        {
            result = "";
        }
        return result;
    }

    // Notkun: FileOps.delete(dir,fname);
    // Fyrir:  dir er slóð sem vísar á möppu.
    //         fname er nafn skrár í möppunni.
    // Eftir:  Búið er að eyða skránni.
    // Afbrigði: Ef skráin var ekki til þá gerist ekkert.
    static public void delete( String dir, String fname )
    {
        Path path = Paths.get(dir,fname);
        File file = path.toFile();
        file.delete();
    }

    // Notkun: boolean e = FileOps.exists(dir,fname);
    // Fyrir:  dir er slóð sem vísar á möppu.
    //         fname er löglegt nafn skrár, sem e.t.v.
    //         er í möppunni.
    // Eftir:  b er satt þá og því aðeins að skrá með þessu
    //         nafni sé til í möppunni.
    static public boolean exists( String dir, String fname )
    {
        Path path = Paths.get(dir,fname);
        File file = path.toFile();
        return file.exists();
    }

    // Notkun: FileOps.write(dir,fname,text);
    // Fyrir:  dir er slóð sem vísar á möppu.
    //         fname er löglegt nafn skrár í möppunni sem má vera til
    //         eða ekki.
    // Eftir:  Skráin er til og inniheldur text, kóðað í UTF-8.
    static public void write( String dir, String fname, String text )
        throws IOException
    {
        Path path = Paths.get(dir,fname);
        Files.write(path,text.getBytes("UTF-8"));
    }
}
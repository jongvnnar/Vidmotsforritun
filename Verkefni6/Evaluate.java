/**
 * Hjálparklasi fyrir Calculator til að meta reiknisegðir sem innihalda fasta.
 * Notast við mxparser v4.4.0 eftir Mariusz Gromada(mariuszgromada.org@gmail.com)
 * Vefsíðu mxparser má finna hér: http://mathparser.org/
 * og skjölun hér: http://mathparser.org/api/org/mariuszgromada/math/mxparser/package-summary.html
 * 
 * @author Jón Gunnar Hannesson
 * @author jgh12@hi.is
 */
import org.mariuszgromada.math.mxparser.*;
import java.util.*;
public class Evaluate{
    /**
     * Fastayrðing gagna:
     * e er hlutur af tagi Expression sem heldur utan um reiknisegðina sem á að meta.
     */
    private Expression e;

    /**
     * Smiður fyrir Evaluate klasa
     * Notkun: Evaluate e = new Evaluate(String s, LinkedHashMap<String,String> memory)
     * @param s er strengur sem lýsir gildri eða ógildri reiknisegð er inniheldur mögulega fasta
     * @param memory er LinkedHashMap fyrir strengi sem heldur utan um fasta sem keys og gildi þeirra sem value.
     * Eftir: e er grunnstillt sem Expression út frá strengnum s og föstum úr memory.
     */
    public Evaluate(String s, LinkedHashMap<String,String> memory){
        for(Map.Entry<String, String> e: memory.entrySet()){
            if(s.contains(e.getKey()));
                s = s.replace(e.getKey(), e.getValue());
        }
        s = s.replace("π", "pi");
        s = s.replace("mod","#");
        e = new Expression(s);
    }
    /**
     * Fall sem nota má til að fá svar úr reiknisegð sem skilgreind var í smið. Skilar NaN ef 
     * reiknisegð er ógild.
     * @return strengur sem lýsir svari úr reiknisegð skv. skjölun á Expression úr mxparser
     */
    public String getNumber(){
        double retValue = e.calculate();
        if(Math.abs(retValue-Math.round(retValue)) < Double.MIN_VALUE) 
            return Long.toString(Math.round(retValue));
        else return Double.toString(retValue);
    }
}

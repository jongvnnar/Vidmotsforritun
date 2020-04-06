import org.mariuszgromada.math.mxparser.*;
import java.util.*;
public class Evaluate{
    private Expression e;
    public Evaluate(String s){
        e = new Expression(s);
    }
    public Evaluate(String s, LinkedHashMap<String,String> memory){
        for(Map.Entry<String, String> e: memory.entrySet()){
            if(s.contains(e.getKey()));
                s = s.replace(e.getKey(), e.getValue());
        }
        s = s.replace("π", "pi");
        s = s.replace("mod","#");
        e = new Expression(s);
    }
    public String getNumber(){
        double retValue = e.calculate();
        if(Math.abs(retValue-Math.round(retValue)) < Double.MIN_VALUE) 
            return Long.toString(Math.round(retValue));
        else return Double.toString(retValue);
    }
}
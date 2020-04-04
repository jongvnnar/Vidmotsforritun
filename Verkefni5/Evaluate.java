/**
 * Klasi notaður til að meta gildi reiknisegða. Nota má sem hjálparklasa með því að beita
 * getNumber() aðferðinni. Klasinn getur metið einfaldar reiknisegðir án sviga með heiltölum og
 * kommutölum sem beita margföldun, deilingu, plús og mínus. Segðir eru metnar frá vinstri til
 * hægri, ekki er tekið tillit til operator precedence.
 * 
 * @author Jón Gunnar Hannesson
 * @author jgh12@hi.is
 */
public class Evaluate{
    /**
     * Fastayrðing gagna. Expression er fylki strengja, þar sem hvert stak fylkisins á að vera til skiptis
     * heiltala eða kommutala og aðgerð '+', '-', '*', '/' eða '='.
     */
    private String[] expression;
    /**
     * Býr til nýtt tilvik af Expression hlut.
     * @param expression strengur sem lýsir gildri eða ógildri reiknisegð þar sem bil er á milli
     * talna/aðgerða.
     * Eftir: tilviksbreytan expression hefur verið grunnstillt sem strengjafylki út frá expression
     *  parameter þar sem skipt er í bilum.
     */
    public Evaluate(String expression){
        this.expression = expression.split(" ");
    }

    /**
     * Hjálparfall sem metur reiknisegðina lýst í tilviksbreytu frá vinstri til hægri
     * og skilar svari sem double tölu. Kastar villu ef reiknisegðin er ekki gild, þ.e. bilsetning
     * var ekki rétt í upphafi, notast var við aðgerðir sem Evaluate klasinn skilur ekki eða að 
     * ekki er skipt á milli talna og aðgerða í hverju staki expression tilviksbreytunnar.
     * @return double tala sem skilar svari við reiknisegðinni miðað við lýsingu að ofan.
     * @throws Exception ef reiknisegð er ekki gild eins og lýst er að ofan.
     */
    private double evaluate() throws Exception{
        double retVal = Double.parseDouble(expression[0]);
        for(int i = 1; i<expression.length-1; i += 2){
            double nextVal;
            try{
                nextVal = Double.parseDouble(expression[i+1]);
                retVal = performOp(retVal, expression[i], nextVal);
            }
            catch(Exception e){
                throw new Exception("Invalid expression");
            }
        }
        return retVal; 
    }
    /**
     * Hjálparfall við að framkvæma reikniaðgerð á tvær double tölur.
     * @param firstNum fyrra númerið sem á að beita aðgerðinni á
     * @param operation strengur sem lýsir aðgerðinni sem á að beita.
     *  Þarf að vera '-','+','*' eða '/'.
     * @param secondNum seinna númerið sem á að beita aðgerðinni á.
     * @return double tala sem þar sem beitt hefur verið aðgerðinni operation á tölurnar tvær í röðinni 
     * firstNum-operation-secondNum.
     * @throws Exception ef reynt er að beita reikniaðgerðum sem fallið styður ekki
     */
    private double performOp(double firstNum, String operation, double secondNum)throws Exception{
        char op = operation.charAt(0);
        switch(op){
            case '/': return firstNum / secondNum;
            case '*': return firstNum * secondNum;
            case '+': return firstNum + secondNum;
            case '-': return firstNum - secondNum;
            default: throw new Exception("Invalid expression");
        }
    }
    /**
     * Fall sem skilar svari við reiknisegðinni sem sett var í miðað við lýsingar á Evaluate
     * klasanum að ofan.
     * @return strengur sem lýsir tölunni sem metin var sem svar, hvort sem það er heiltala eða kommutala.
     * @throws Exception ef reiknisegðin er ógild eða notast við aðgerðir sem klasinn ekki skilur.
     */
    public String getNumber() throws Exception{
        double retValue;
        try{
            retValue = evaluate();
        }
        catch(Exception e){
            throw new Exception("Invalid expression");
        }
        if(Math.abs(retValue-Math.round(retValue)) < Double.MIN_VALUE) 
            return Long.toString(Math.round(retValue));
        else return Double.toString(retValue);
    }
}
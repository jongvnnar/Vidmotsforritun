public class Evaluate{
    String[] expression;
    double retValue;
    public Evaluate(String expression) throws Exception{
        this.expression = expression.split(" ");
        try{
        retValue = evaluate();
        }
        catch(Exception e){
            throw new Exception("Invalid expression");
        }
    }
    private double evaluate() throws Exception{
        double retVal = Double.parseDouble(expression[0]);
        for(int i = 1; i<expression.length-1; i += 2){
            double nextVal;
            try{
                nextVal =Double.parseDouble(expression[i+1]);
                retVal = performOp(retVal, expression[i], nextVal);
            }
            catch(Exception e){
                throw new Exception("Invalid expression");
            }
        }
        return retVal; 
    }
    private double performOp(double firstNum, String operation, double secondNum){
        char op = operation.charAt(0);
        switch(op){
            case '/': return firstNum / secondNum;
            case '*': return firstNum * secondNum;
            case '+': return firstNum + secondNum;
            case '-': return firstNum - secondNum;
            default: return 0.0;
        }
    }
    public String getNumber(){
        if(Math.abs(retValue-Math.round(retValue)) < Double.MIN_VALUE) 
            return Long.toString(Math.round(retValue));
        else return Double.toString(retValue);
    }
}
package simpl.parser.ast;

import simpl.interpreter.IntValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;

public class Div extends ArithExpr {

    public Div(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " / " + r + ")";
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        Value value_left = l.eval(s);
        Value value_right = r.eval(s);
        if( !(value_left instanceof IntValue) ) 
            throw new RuntimeError("left hand isn't int values");
        if(  !(value_right instanceof IntValue) )
            throw new RuntimeError("right hand  isn't int values");
        if( ((IntValue)value_right).n == 0 )
            throw new RuntimeError("divisor cannot be 0!");
        
        int result = ((IntValue)value_left).n / ((IntValue)value_right).n;
        return new IntValue( result );
    }
}

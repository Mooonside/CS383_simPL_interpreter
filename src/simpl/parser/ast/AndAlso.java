package simpl.parser.ast;

import simpl.interpreter.BoolValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;

public class AndAlso extends BinaryExpr {

    public AndAlso(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " andalso " + r + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult type_result_left = l.typecheck(E);
        TypeResult type_result_right = r.typecheck(E);
        Substitution s = type_result_right.s.compose(type_result_left.s);

        Type type_left = type_result_left.t;
        Type type_right = type_result_right.t;

        type_left = s.apply(type_left);
        type_right = s.apply(type_right);

        s = type_right.unify(Type.BOOL).compose(s);
        s = type_left.unify(Type.BOOL).compose(s);
        
        return TypeResult.of(s,Type.BOOL);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        Value value_left = l.eval(s);
        // if left hand is not boolean already
        if(!(value_left instanceof BoolValue))
            throw new RuntimeError("not a bool value");
        //if  left hand is false
        if( ! ((BoolValue)value_left).b  ){
            return new BoolValue(false);
        }
        Value value_right = r.eval(s);
        //if right hand is not a boolean
        if(!(value_left instanceof BoolValue))
            throw new RuntimeError("not a bool value");
        //ow.it depends on the right hand's value
        return new BoolValue( ((BoolValue)value_right).b );
    }
}

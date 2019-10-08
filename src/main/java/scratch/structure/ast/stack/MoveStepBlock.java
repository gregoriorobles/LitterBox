package scratch.structure.ast.stack;

import scratch.structure.ast.Extendable;
import scratch.structure.ast.ScriptBodyBlock;
import scratch.structure.ast.Stackable;
import scratch.structure.ast.visitor.BlockVisitor;

public class MoveStepBlock extends SingleIntInputBlock {

    public MoveStepBlock(String opcode, Stackable next, Extendable parent, boolean shadow, boolean topLevel, String inputName, int inputValue, int inputShadow) {
        super(opcode, next, parent, shadow, topLevel, inputName, inputValue, inputShadow);
    }

    public MoveStepBlock(String opcode, Stackable next, Extendable parent, boolean shadow, boolean topLevel, int x, int y, String inputName, int inputValue, int inputShadow) {
        super(opcode, next, parent, shadow, topLevel, x, y, inputName, inputValue, inputShadow);
    }

    public MoveStepBlock(String opcode, Stackable next, Extendable parent, boolean shadow, boolean topLevel, String inputName, String inputVariableID, int inputShadow) {
        super(opcode, next, parent, shadow, topLevel, inputName, inputVariableID, inputShadow);
    }

    public MoveStepBlock(String opcode, Stackable next, Extendable parent, boolean shadow, boolean topLevel, int x, int y, String inputName, String inputVariableID, int inputShadow) {
        super(opcode, next, parent, shadow, topLevel, x, y, inputName, inputVariableID, inputShadow);
    }


    @Override
    public void accept(BlockVisitor visitor) {
        visitor.visit(this);
    }
}

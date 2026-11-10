package model;

import java.util.ArrayList;
import java.util.List;

public class Conditional extends Expression {
    public List<IfBlock> ifBlocks;

    public Conditional() {
        this.ifBlocks = new ArrayList<>();
    }

    @Override
    public void execute(Program p) {
        for (IfBlock ifblock : ifBlocks) {
            if (ifblock.guard.evaluate(p)) {
                ifblock.execute(p);
                break;
            }
        }
    }
}

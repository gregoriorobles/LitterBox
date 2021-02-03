package de.uni_passau.fim.se2.litterbox.analytics.metric;

import de.uni_passau.fim.se2.litterbox.analytics.MetricExtractor;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import de.uni_passau.fim.se2.litterbox.ast.model.Script;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.bool.*;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.num.*;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.string.Answer;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.string.AttributeOf;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.string.Username;
import de.uni_passau.fim.se2.litterbox.ast.model.procedure.ProcedureDefinition;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.actorlook.AskAndWait;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.common.ResetTimer;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.spritemotion.SetDragMode;
import de.uni_passau.fim.se2.litterbox.ast.visitor.ScratchVisitor;
import de.uni_passau.fim.se2.litterbox.utils.Preconditions;

public class TopLevelSensingStmtCount implements MetricExtractor, ScratchVisitor {
    public static final String NAME = "top_level_sensing_stmt_count";
    private int count = 0;

    @Override
    public double calculateMetric(Program program) {
        Preconditions.checkNotNull(program);
        count = 0;
        program.accept(this);
        return count;
    }

    @Override
    public void visit(AskAndWait node) {
        //The parent is a StmtList, so the parent of the StmtList has to be checked
        if (node.getParentNode().getParentNode() instanceof Script || node.getParentNode().getParentNode() instanceof ProcedureDefinition) {
            count++;
        }
    }

    @Override
    public void visit(SetDragMode node) {
        //The parent is a StmtList, so the parent of the StmtList has to be checked
        if (node.getParentNode().getParentNode() instanceof Script || node.getParentNode().getParentNode() instanceof ProcedureDefinition) {
            count++;
        }
    }

    @Override
    public void visit(ResetTimer node) {
        //The parent is a StmtList, so the parent of the StmtList has to be checked
        if (node.getParentNode().getParentNode() instanceof Script || node.getParentNode().getParentNode() instanceof ProcedureDefinition) {
            count++;
        }
    }

    @Override
    public String getName() {
        return NAME;
    }
}
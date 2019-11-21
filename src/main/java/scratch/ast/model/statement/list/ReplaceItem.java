/*
 * Copyright (C) 2019 LitterBox contributors
 *
 * This file is part of LitterBox.
 *
 * LitterBox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * LitterBox is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LitterBox. If not, see <http://www.gnu.org/licenses/>.
 */
package scratch.ast.model.statement.list;

import scratch.utils.UnmodifiableListBuilder;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.base.Preconditions;
import scratch.ast.model.ASTNode;
import scratch.ast.model.expression.num.NumExpr;
import scratch.ast.model.expression.string.StringExpr;
import scratch.ast.model.variable.Variable;
import scratch.ast.visitor.ScratchVisitor;

public class ReplaceItem implements ListStmt {

    private final NumExpr index;
    private final Variable variable;
    private final StringExpr string;
    private final ImmutableList<ASTNode> children;

    public ReplaceItem(StringExpr string, NumExpr index, Variable variable) {
        this.index = Preconditions.checkNotNull(index);
        this.variable = Preconditions.checkNotNull(variable);
        this.string = Preconditions.checkNotNull(string);
        this.children = ImmutableList.<ASTNode>builder()
            .add(index)
            .add(variable)
            .add(string).build();
    }

    public NumExpr getIndex() {
        return index;
    }

    public Variable getVariable() {
        return variable;
    }

    public StringExpr getString() {
        return string;
    }

    @Override
    public void accept(ScratchVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ImmutableList<ASTNode> getChildren() {
        return children;
    }
}
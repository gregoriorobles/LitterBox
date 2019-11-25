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
package scratch.ast.model.statement;

import scratch.ast.model.AbstractNode;
import scratch.ast.model.expression.Expression;
import scratch.ast.visitor.ScratchVisitor;
import utils.Preconditions;

public class ExpressionStmt extends AbstractNode implements Stmt {

    private final Expression expression;

    public ExpressionStmt(Expression expression) {
        super(expression);
        this.expression = Preconditions.checkNotNull(expression);
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public void accept(ScratchVisitor visitor) {
        visitor.visit(this);
    }

}
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
package de.uni_passau.fim.se2.litterbox.ast.model.statement.declaration;

import de.uni_passau.fim.se2.litterbox.ast.model.AbstractNode;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.string.StringExpr;
import de.uni_passau.fim.se2.litterbox.ast.model.type.Type;
import de.uni_passau.fim.se2.litterbox.ast.model.variable.Identifier;
import de.uni_passau.fim.se2.litterbox.ast.visitor.ScratchVisitor;
import de.uni_passau.fim.se2.litterbox.utils.Preconditions;

public class DeclarationAttributeOfIdentAsTypeStmt extends AbstractNode implements DeclarationStmt {

    private final StringExpr stringExpr;
    private final Identifier ident;
    private final Type type;

    public DeclarationAttributeOfIdentAsTypeStmt(StringExpr stringExpr, Identifier ident, Type type) {
        super(stringExpr, ident, type);
        this.stringExpr = Preconditions.checkNotNull(stringExpr);
        this.ident = Preconditions.checkNotNull(ident);
        this.type = Preconditions.checkNotNull(type);
    }

    public StringExpr getStringExpr() {
        return stringExpr;
    }

    public Identifier getIdent() {
        return ident;
    }

    public Type getType() {
        return type;
    }

    @Override
    public void accept(ScratchVisitor visitor) {
        visitor.visit(this);
    }


}
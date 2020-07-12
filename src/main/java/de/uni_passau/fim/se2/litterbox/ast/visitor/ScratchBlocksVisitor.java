/*
 * Copyright (C) 2020 LitterBox contributors
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
package de.uni_passau.fim.se2.litterbox.ast.visitor;


import de.uni_passau.fim.se2.litterbox.ast.model.Message;
import de.uni_passau.fim.se2.litterbox.ast.model.Script;
import de.uni_passau.fim.se2.litterbox.ast.model.elementchoice.Next;
import de.uni_passau.fim.se2.litterbox.ast.model.elementchoice.Prev;
import de.uni_passau.fim.se2.litterbox.ast.model.elementchoice.Random;
import de.uni_passau.fim.se2.litterbox.ast.model.elementchoice.WithExpr;
import de.uni_passau.fim.se2.litterbox.ast.model.event.*;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.string.*;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.string.attributes.FixedAttribute;
import de.uni_passau.fim.se2.litterbox.ast.model.identifier.Qualified;
import de.uni_passau.fim.se2.litterbox.ast.model.identifier.StrId;
import de.uni_passau.fim.se2.litterbox.ast.model.literals.NumberLiteral;
import de.uni_passau.fim.se2.litterbox.ast.model.literals.StringLiteral;
import de.uni_passau.fim.se2.litterbox.ast.model.position.MousePos;
import de.uni_passau.fim.se2.litterbox.ast.model.position.RandomPos;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.actorlook.*;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.actorsound.*;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.common.*;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.control.*;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.list.*;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.spritelook.*;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.spritemotion.*;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.termination.DeleteClone;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.termination.StopAll;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.termination.StopThisScript;

import java.io.PrintStream;

/*
 * Documentation of syntax:
 * https://en.scratch-wiki.info/wiki/Block_Plugin/Syntax
 *
 * Every scratch block goes on a new line.
 * Example:
 *
 * [scratchblocks]
 * when green flag clicked
 * forever
 *     turn cw (15) degrees
 *     say [Hello!] for (2) secs
 *     if <mouse down?> then
 *         change [mouse clicks v] by (1)
 *     end
 * end
 * [/scratchblocks]
 */
public class ScratchBlocksVisitor extends PrintVisitor {

    private boolean inScript = false;

    public ScratchBlocksVisitor(PrintStream stream) {
        super(stream);
    }

    @Override
    public void visit(Script script) {
        emitNoSpace("[scratchblocks]");
        inScript = true;
        newLine();
        super.visit(script);
        emitNoSpace("[/scratchblocks]");
        inScript = false;
        newLine();
    }

    //---------------------------------------------------------------
    // Event blocks

    @Override
    public void visit(GreenFlag greenFlag) {
        emitNoSpace("when green flag clicked");
        newLine();
    }


    @Override
    public void visit(Clicked clicked) {
        emitNoSpace("when this sprite clicked");
        newLine();
    }

    @Override
    public void visit(KeyPressed keyPressed) {
        emitNoSpace("when ");
        keyPressed.getKey().accept(this);
        emitNoSpace(" key pressed");
        newLine();
    }

    @Override
    public void visit(StartedAsClone startedAsClone) {
        emitToken("when I start as a clone");
        newLine();
    }

    @Override
    public void visit(ReceptionOfMessage receptionOfMessage) {
        emitNoSpace("when I receive [");
        receptionOfMessage.getMsg().accept(this);
        emitNoSpace(" v]");
        newLine();
    }

    public void visit(BackdropSwitchTo backdrop) {
        emitNoSpace("when backdrop switches to [");
        backdrop.getBackdrop().accept(this);
        emitNoSpace(" v]");
        newLine();
    }

    // TODO: When loudness


    @Override
    public void visit(Broadcast node) {
        emitNoSpace("broadcast (");
        node.getMessage().accept(this);
        emitNoSpace(" v)");
        newLine();
    }

    @Override
    public void visit(BroadcastAndWait node) {
        emitNoSpace("broadcast (");
        node.getMessage().accept(this);
        emitNoSpace(" v) and wait");
        newLine();
    }



    //---------------------------------------------------------------
    // Control blocks

    @Override
    public void visit(WaitSeconds node) {
        emitNoSpace("wait ");
        node.getSeconds().accept(this);
        emitNoSpace(" seconds");
        newLine();
    }

    @Override
    public void visit(WaitUntil node) {
        emitNoSpace("wait until <");
        node.getUntil().accept(this);
        emitNoSpace(">");
        newLine();
    }

    @Override
    public void visit(StopAll node) {
        emitNoSpace("stop [all v]");
        newLine();
    }

    @Override
    public void visit(StopOtherScriptsInSprite node) {
        emitToken("stop [other scripts in sprite v]");
        newLine();
    }

    @Override
    public void visit(StopThisScript node) {
        emitToken("stop [this script v]");
        newLine();
    }


    @Override
    public void visit(CreateCloneOf node) {
        emitNoSpace("create clone of (");

        // TODO: There must be a nicer way to do this...
        if (node.getStringExpr() instanceof AsString
                && ((AsString) node.getStringExpr()).getOperand1() instanceof StrId) {

            final String spriteName = ((StrId) ((AsString) node.getStringExpr()).getOperand1()).getName();
            if (spriteName.equals("_myself_")) {
                emitNoSpace("myself");
            } else {
                node.getStringExpr().accept(this);
            }
        } else {
            node.getStringExpr().accept(this);
        }
        emitNoSpace(" v)");
        newLine();
    }

    @Override
    public void visit(DeleteClone node) {
        emitToken("delete this clone");
        newLine();
    }


    @Override
    public void visit(RepeatForeverStmt repeatForeverStmt) {
        emitToken("forever");
        newLine();
        beginIndentation();
        repeatForeverStmt.getStmtList().accept(this);
        endIndentation();
        emitNoSpace("end");
        newLine();
    }

    @Override
    public void visit(UntilStmt untilStmt) {
        emitNoSpace("repeat until <");
        untilStmt.getBoolExpr().accept(this);
        emitNoSpace(">");
        newLine();
        beginIndentation();
        untilStmt.getStmtList().accept(this);
        endIndentation();
        emitNoSpace("end");
        newLine();
    }

    @Override
    public void visit(RepeatTimesStmt repeatTimesStmt) {
        emitNoSpace("repeat ");
        repeatTimesStmt.getTimes().accept(this);
        newLine();
        beginIndentation();
        repeatTimesStmt.getStmtList().accept(this);
        endIndentation();
        emitNoSpace("end");
        newLine();
    }

    @Override
    public void visit(IfThenStmt ifThenStmt) {
        emitNoSpace("if <");
        ifThenStmt.getBoolExpr().accept(this);
        emitNoSpace("> then");
        newLine();
        beginIndentation();
        ifThenStmt.getThenStmts().accept(this);
        endIndentation();
        emitNoSpace("end");
        newLine();
    }

    @Override
    public void visit(IfElseStmt ifElseStmt) {
        emitNoSpace("if <");
        ifElseStmt.getBoolExpr().accept(this);
        emitNoSpace("> then");
        newLine();
        beginIndentation();
        ifElseStmt.getStmtList().accept(this);
        endIndentation();
        emitNoSpace("else");
        newLine();
        beginIndentation();
        ifElseStmt.getElseStmts().accept(this);
        endIndentation();
        emitNoSpace("end");
        newLine();
    }

    //---------------------------------------------------------------
    // Motion blocks

    @Override
    public void visit(MoveSteps node) {
        emitNoSpace("move ");
        node.getSteps().accept(this);
        emitNoSpace(" steps");
        newLine();
    }

    @Override
    public void visit(TurnLeft node) {
        emitNoSpace("turn left ");
        node.getDegrees().accept(this);
        emitNoSpace(" degrees");
        newLine();
    }

    @Override
    public void visit(TurnRight node) {
        emitNoSpace("turn right ");
        node.getDegrees().accept(this);
        emitNoSpace(" degrees");
        newLine();
    }

    @Override
    public void visit(GoToPos node) {
        emitNoSpace("go to (");
        node.getPosition().accept(this);
        emitNoSpace(" v)");
        newLine();
    }

    @Override
    public void visit(GoToPosXY node) {
        emitNoSpace("go to x: ");
        node.getX().accept(this);
        emitNoSpace(" y: ");
        node.getY().accept(this);
        newLine();
    }

    @Override
    public void visit(GlideSecsTo node) {
        emitNoSpace("glide ");
        node.getSecs().accept(this);
        emitNoSpace(" secs to (");
        node.getPosition().accept(this);
        emitNoSpace(" v)");
        newLine();
    }

    @Override
    public void visit(GlideSecsToXY node) {
        emitNoSpace("glide ");
        node.getSecs().accept(this);
        emitNoSpace(" secs to x: ");
        node.getX().accept(this);
        emitNoSpace(" y: ");
        node.getY().accept(this);
        newLine();
    }

    @Override
    public void visit(PointInDirection node) {
        emitNoSpace("point in direction ");
        node.getDirection().accept(this);
        newLine();
    }

    @Override
    public void visit(PointTowards node) {
        emitNoSpace("point towards (");
        node.getPosition().accept(this);
        emitNoSpace(" v)");
        newLine();
    }

    @Override
    public void visit(ChangeXBy node) {
        emitNoSpace("change x by ");
        node.getNum().accept(this);
        newLine();
    }

    @Override
    public void visit(SetXTo node) {
        emitNoSpace("set x to ");
        node.getNum().accept(this);
        newLine();
    }

    @Override
    public void visit(ChangeYBy node) {
        emitNoSpace("change y by ");
        node.getNum().accept(this);
        newLine();
    }

    @Override
    public void visit(SetYTo node) {
        emitNoSpace("set y to ");
        node.getNum().accept(this);
        newLine();
    }

    @Override
    public void visit(IfOnEdgeBounce node) {
        emitNoSpace("if on edge, bounce");
        newLine();
    }

    @Override
    public void visit(SetRotationStyle node) {
        emitNoSpace("set rotation style [");
        node.getRotation().accept(this);
        emitNoSpace(" v]");
        newLine();
    }


    //---------------------------------------------------------------
    // Looks blocks

    @Override
    public void visit(SayForSecs node) {
        emitNoSpace("say ");
        node.getString().accept(this);
        emitNoSpace(" for ");
        node.getSecs().accept(this);
        emitNoSpace(" seconds");
        newLine();
    }

    @Override
    public void visit(Say node) {
        emitNoSpace("say ");
        node.getString().accept(this);
        newLine();
    }

    @Override
    public void visit(ThinkForSecs node) {
        emitNoSpace("think ");
        node.getThought().accept(this);
        emitNoSpace(" for ");
        node.getSecs().accept(this);
        emitNoSpace(" seconds");
        newLine();
    }

    @Override
    public void visit(Think node) {
        emitNoSpace("think ");
        node.getThought().accept(this);
        newLine();
    }

    @Override
    public void visit(SwitchCostumeTo node) {
        emitNoSpace("switch costume to (");
        node.getCostumeChoice().accept(this);
        emitNoSpace(" v)");
        newLine();
    }

    @Override
    public void visit(NextCostume node) {
        emitNoSpace("next costume");
        newLine();
    }

    @Override
    public void visit(SwitchBackdrop node) {
        emitNoSpace("switch backdrop to (");
        if(node.getElementChoice() instanceof Next) {
            emitNoSpace("next backdrop");
        } else if(node.getElementChoice() instanceof Prev) {
            emitNoSpace("previous backdrop");
        } else if(node.getElementChoice() instanceof Random) {
            emitNoSpace("random backdrop");
        } else {
            node.getElementChoice().accept(this);
        }
        emitNoSpace(" v)");
        newLine();
    }

    @Override
    public void visit(NextBackdrop node) {
        emitNoSpace("next backdrop");
        newLine();
    }

    @Override
    public void visit(ChangeSizeBy node) {
        emitNoSpace("change size by ");
        node.getNum().accept(this);
        newLine();
    }

    @Override
    public void visit(SetSizeTo node) {
        emitNoSpace("set size to ");
        node.getPercent().accept(this);
        emitNoSpace(" %");
        newLine();
    }

    @Override
    public void visit(ChangeGraphicEffectBy node) {
        emitNoSpace("change [");
        node.getEffect().accept(this);
        emitNoSpace(" v] effect by ");
        node.getValue().accept(this);
        newLine();
    }

    @Override
    public void visit(SetGraphicEffectTo node) {
        emitNoSpace("set [");
        node.getEffect().accept(this);
        emitNoSpace(" v] effect to ");
        node.getValue().accept(this);
        newLine();
    }

    @Override
    public void visit(ClearGraphicEffects node) {
        emitNoSpace("clear graphic effects");
        newLine();
    }

    @Override
    public void visit(Show node) {
        emitNoSpace("show");
        newLine();
    }

    @Override
    public void visit(Hide node) {
        emitNoSpace("hide");
        newLine();
    }

    @Override
    public void visit(GoToLayer node) {
        emitNoSpace("go to [");
        node.getLayerChoice().accept(this);
        emitNoSpace(" v] layer");
        newLine();
    }

    @Override
    public void visit(ChangeLayerBy node) {
        emitNoSpace("go [");
        node.getForwardBackwardChoice().accept(this);
        emitNoSpace(" v] ");
        node.getNum().accept(this);
        emitNoSpace(" layers");
        newLine();
    }


    //---------------------------------------------------------------
    // Sound blocks

    @Override
    public void visit(PlaySoundUntilDone node) {
        emitNoSpace("play sound (");
        node.getElementChoice().accept(this);
        emitNoSpace(" v) until done");
        newLine();
    }

    @Override
    public void visit(StartSound node) {
        emitNoSpace("start sound (");
        node.getElementChoice().accept(this);
        emitNoSpace(" v)");
        newLine();
    }

    @Override
    public void visit(StopAllSounds node) {
        emitNoSpace("stop all sounds");
        newLine();
    }

    @Override
    public void visit(ChangeSoundEffectBy node) {
        emitNoSpace("change [");
        node.getEffect().accept(this);
        emitNoSpace(" v] effect by ");
        node.getValue().accept(this);
        newLine();
    }

    @Override
    public void visit(SetSoundEffectTo node) {
        emitNoSpace("set [");
        node.getEffect().accept(this);
        emitNoSpace(" v] effect to ");
        node.getValue().accept(this);
        newLine();
    }

    @Override
    public void visit(ClearSoundEffects node) {
        emitNoSpace("clear sound effects");
        newLine();
    }

    @Override
    public void visit(ChangeVolumeBy node) {
        emitNoSpace("change volume by ");
        node.getVolumeValue().accept(this);
        newLine();
    }

    @Override
    public void visit(SetVolumeTo node) {
        emitNoSpace("set volume to ");
        node.getVolumeValue().accept(this);
        emitNoSpace(" %");
        newLine();
    }

    //---------------------------------------------------------------
    // Sensing blocks

    @Override
    public void visit(AskAndWait node) {
        emitNoSpace("ask ");
        node.getQuestion().accept(this);
        emitNoSpace(" and wait");
        newLine();
    }

    @Override
    public void visit(SetDragMode node) {
        emitNoSpace("set drag mode [");
        node.getDrag().accept(this);
        emitNoSpace(" v]");
        newLine();
    }

    @Override
    public void visit(ResetTimer node) {
        emitNoSpace("reset timer");
        newLine();
    }


    //---------------------------------------------------------------
    // Variables blocks

    @Override
    public void visit(SetVariableTo node) {
        if(!inScript) {
            return;
        }
        emitNoSpace("set [");
        node.getIdentifier().accept(this);
        emitNoSpace(" v] to ");
        node.getExpr().accept(this);
        newLine();
    }

    @Override
    public void visit(ChangeVariableBy node) {
        emitNoSpace("change [");
        node.getIdentifier().accept(this);
        emitNoSpace(" v] by ");
        node.getExpr().accept(this);
        newLine();
    }

    @Override
    public void visit(ShowVariable node) {
        emitNoSpace("show variable [");
        node.getIdentifier().accept(this);
        emitNoSpace(" v]");
        newLine();
    }

    @Override
    public void visit(HideVariable node) {
        emitNoSpace("hide variable [");
        node.getIdentifier().accept(this);
        emitNoSpace(" v]");
        newLine();
    }

    @Override
    public void visit(AddTo node) {
        emitNoSpace("add ");
        node.getString().accept(this);
        emitNoSpace(" to [");
        node.getIdentifier().accept(this);
        emitNoSpace(" v]");
        newLine();
    }

    @Override
    public void visit(DeleteOf node) {
        emitNoSpace("delete ");
        node.getNum().accept(this);
        emitNoSpace(" of [");
        node.getIdentifier().accept(this);
        emitNoSpace(" v]");
        newLine();
    }

    @Override
    public void visit(DeleteAllOf node) {
        emitNoSpace("delete all of [");
        node.getIdentifier().accept(this);
        emitNoSpace(" v]");
        newLine();
    }

    @Override
    public void visit(InsertAt node) {
        emitNoSpace("insert ");
        node.getString().accept(this);
        emitNoSpace(" at ");
        node.getIndex().accept(this);
        emitNoSpace(" of [");
        node.getIdentifier().accept(this);
        emitNoSpace(" v]");
        newLine();
    }

    @Override
    public void visit(ReplaceItem node) {
        emitNoSpace("replace item ");
        node.getIndex().accept(this);
        emitNoSpace(" of [");
        node.getIdentifier().accept(this);
        emitNoSpace(" v] with ");
        node.getString().accept(this);
        newLine();
    }

    @Override
    public void visit(ShowList node) {
        emitNoSpace("show list [");
        node.getIdentifier().accept(this);
        emitNoSpace(" v]");
        newLine();
    }

    @Override
    public void visit(HideList node) {
        emitNoSpace("hide list [");
        node.getIdentifier().accept(this);
        emitNoSpace(" v]");
        newLine();
    }



    @Override
    public void visit(NumberLiteral number) {
        if(!inScript) {
            return;
        }

        emitNoSpace("(");
        double num = number.getValue();
        if(num % 1 == 0) {
            emitNoSpace(Integer.toString((int)num));
        } else {
            emitNoSpace(String.valueOf(num));
        }
        emitNoSpace(")");
    }


    @Override
    public void visit(AttributeOf node) {
        emitNoSpace("([");
        node.getAttribute().accept(this);
        emitNoSpace(" v] of (");

        // TODO: How to do this nicer?
        if(node.getElementChoice() instanceof WithExpr && ((WithExpr)node.getElementChoice()).getExpression() instanceof StrId) {
            StrId literal = (StrId) ((WithExpr)node.getElementChoice()).getExpression();
            if(literal.getName().equals("_stage_")) {
                emitNoSpace("Stage");
            } else {
                node.getElementChoice().accept(this);
            }
        } else {
            node.getElementChoice().accept(this);
        }
        emitNoSpace(" v)?)");
    }


    @Override
    public void visit(FixedAttribute node) {
        emitNoSpace(node.getType());
    }

    @Override
    public void visit(WithExpr node) {
        node.getExpression().accept(this);
    }


    @Override
    public void visit(MousePos node) {
        emitNoSpace("mouse-pointer");
    }

    @Override
    public void visit(RandomPos node) {
        emitNoSpace("random position");
    }

    @Override
    public void visit(RotationStyle node) {
        emitNoSpace(node.getToken());
    }

    @Override
    public void visit(Backdrop node) {
        node.getType().accept(this);
    }

    @Override
    public void visit(NameNum node) {
        emitNoSpace(node.getType());
    }

    @Override
    public void visit(StringLiteral stringLiteral) {
        if(inScript) {
            emitNoSpace("[");
            emitNoSpace(stringLiteral.getText());
            emitNoSpace("]");
        }
    }

    @Override
    public void visit(GraphicEffect node) {
        emitNoSpace(node.getToken());
    }

    @Override
    public void visit(SoundEffect node) {
        emitNoSpace(node.getToken());
    }

    @Override
    public void visit(LayerChoice node) {
        emitNoSpace(node.getType());
    }

    @Override
    public void visit(DragMode node) {
        emitNoSpace(node.getToken());
    }

    @Override
    public void visit(ForwardBackwardChoice node) {
        emitNoSpace(node.getType());
    }

    @Override
    public void visit(Message node) {
        StringExpr message = node.getMessage();
        if (message instanceof StringLiteral) {
            StringLiteral literal = (StringLiteral)message;
            emitNoSpace(literal.getText());
        } else {
            node.getMessage().accept(this);
        }
    }

    @Override
    public void visit(Qualified node) {
        node.getSecond().accept(this);
    }

    @Override
    public void visit(StrId strId) {
        if(!inScript) {
            return;
        }
        emitNoSpace(strId.getName());
    }
//
//    @Override
//    public void visit(BoolLiteral boolLiteral) {
//        emitToken(String.valueOf(boolLiteral.getValue()));
//    }



    // TODO: This is a dummy for now
    public String getScratchBlocks() {
        return "[scratchblocks]\n" +
                "when green flag clicked\n" +
                "todo\n" +
                "[/scratchblocks]\n";
    }
}

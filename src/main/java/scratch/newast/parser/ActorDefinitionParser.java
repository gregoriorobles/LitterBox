package scratch.newast.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import scratch.newast.ParsingException;
import scratch.newast.model.ActorDefinition;
import scratch.newast.model.ActorType;
import scratch.newast.model.DeclarationStmt;
import scratch.newast.model.DeclarationStmtList;
import scratch.newast.model.Script;
import scratch.newast.model.ScriptList;
import scratch.newast.model.SetStmtList;
import scratch.newast.model.expression.bool.Bool;
import scratch.newast.model.expression.bool.BoolExpr;
import scratch.newast.model.expression.num.NumExpr;
import scratch.newast.model.expression.num.Number;
import scratch.newast.model.expression.string.Str;
import scratch.newast.model.expression.string.StringExpr;
import scratch.newast.model.procedure.ProcedureDefinitionList;
import scratch.newast.model.resource.Resource;
import scratch.newast.model.resource.ResourceList;
import scratch.newast.model.statement.common.SetAttributeTo;
import scratch.newast.model.statement.common.SetStmt;
import scratch.newast.model.variable.Identifier;

import static scratch.newast.Constants.*;

public class ActorDefinitionParser {

    public static ActorDefinition parse(JsonNode actorDefinitionNode) throws ParsingException {
        Preconditions.checkNotNull(actorDefinitionNode);
        Preconditions.checkArgument(actorDefinitionNode.has(IS_STAGE_KEY), "Missing field isStage in ScriptGroup");
        Preconditions.checkArgument(actorDefinitionNode.has(NAME_KEY), "Missing field name in ScriptGroup");

        ActorType actorType;
        if (actorDefinitionNode.get(IS_STAGE_KEY).asBoolean()) {
            actorType = ActorType.stage;
        } else {
            actorType = ActorType.sprite;
        }

        Identifier identifier = new Identifier(actorDefinitionNode.get(NAME_KEY).asText());

        List<Resource> res = ResourceParser.parseSound(actorDefinitionNode.get("sounds"));
        res.addAll(ResourceParser.parseCostume(actorDefinitionNode.get("costumes")));
        ResourceList resources = new ResourceList(res);

        List<DeclarationStmt> decls = DeclarationStmtParser
                .parseLists(actorDefinitionNode.get("lists"), identifier.getValue(),
                        actorDefinitionNode.get(IS_STAGE_KEY).asBoolean());
        decls.addAll(DeclarationStmtParser.parseBroadcasts(actorDefinitionNode.get("broadcasts"), identifier.getValue(),
                actorDefinitionNode.get(IS_STAGE_KEY).asBoolean()));
        decls.addAll(DeclarationStmtParser.parseVariables(actorDefinitionNode.get("variables"), identifier.getValue(),
                actorDefinitionNode.get(IS_STAGE_KEY).asBoolean()));
        DeclarationStmtList declarations = new DeclarationStmtList(decls);

        JsonNode allBlocks = actorDefinitionNode.get("blocks");
        Iterator<String> fieldIterator = allBlocks.fieldNames();
        Iterable<String> iterable = () -> fieldIterator;
        Stream<String> stream = StreamSupport.stream(iterable.spliterator(), false);
        List<String> topLevelNodes = stream.filter(fieldName -> allBlocks.get(fieldName).get("topLevel").asBoolean())
                .collect(Collectors.toList());
        List<Script> scripts = new LinkedList<>();
        for (String topLevelID : topLevelNodes) {
            Script script = ScriptParser.parse(topLevelID, allBlocks);
            scripts.add(script);
        }
        ScriptList scriptList = new ScriptList(scripts);

        ProcedureDefinitionList procDeclList = ProcDefinitionParser.parse(allBlocks);

        SetStmtList setStmtList = parseSetStmts(actorDefinitionNode, identifier.getValue());

        return new ActorDefinition(actorType, identifier, resources, declarations, setStmtList, procDeclList,
                scriptList);
    }

    private static SetStmtList parseSetStmts(JsonNode actorDefinitionNode, String actorName) {
        //Todo refactor to avoid code duplication

        //String ttSLang = "textToSpeechLanguage"; // Ignored as this is an extension

        StringExpr keyExpr;
        double jsonDouble;
        String jsonString;
        boolean jsonBool;
        NumExpr numExpr;
        StringExpr stringExpr;
        BoolExpr boolExpr;
        SetStmt setStmt;

        List<SetStmt> list = new LinkedList<>();

        keyExpr = new Str(VOLUME_KEY);
        jsonDouble = actorDefinitionNode.get(VOLUME_KEY).asDouble();
        numExpr = new Number((float) jsonDouble);
        setStmt = new SetAttributeTo(keyExpr, numExpr);
        list.add(setStmt);

        keyExpr = new Str(LAYERORDER_KEY);
        jsonDouble = actorDefinitionNode.get(LAYERORDER_KEY).asDouble();
        numExpr = new Number((float) jsonDouble);
        setStmt = new SetAttributeTo(keyExpr, numExpr);
        list.add(setStmt);

        if (actorDefinitionNode.get("isStage").asBoolean()) {
            //Stage

            keyExpr = new Str(TEMPO_KEY);
            jsonDouble = actorDefinitionNode.get(TEMPO_KEY).asDouble();
            numExpr = new Number((float) jsonDouble);
            setStmt = new SetAttributeTo(keyExpr, numExpr);
            list.add(setStmt);

            keyExpr = new Str(VIDTRANSPARENCY_KEY);
            jsonDouble = actorDefinitionNode.get(VIDTRANSPARENCY_KEY).asDouble();
            numExpr = new Number((float) jsonDouble);
            setStmt = new SetAttributeTo(keyExpr, numExpr);
            list.add(setStmt);

            keyExpr = new Str(VIDSTATE_KEY);
            jsonString = actorDefinitionNode.get(VIDSTATE_KEY).asText();
            stringExpr = new Str(jsonString);
            setStmt = new SetAttributeTo(keyExpr, stringExpr);
            list.add(setStmt);

        } else {

            keyExpr = new Str(VISIBLE_KEY);
            jsonBool = actorDefinitionNode.get(VISIBLE_KEY).asBoolean();
            boolExpr = new Bool(jsonBool);
            setStmt = new SetAttributeTo(keyExpr, boolExpr);
            list.add(setStmt);

            keyExpr = new Str(X_KEY);
            jsonDouble = actorDefinitionNode.get(X_KEY).asDouble();
            numExpr = new Number((float) jsonDouble);
            setStmt = new SetAttributeTo(keyExpr, numExpr);
            list.add(setStmt);

            keyExpr = new Str(Y_KEY);
            jsonDouble = actorDefinitionNode.get(Y_KEY).asDouble();
            numExpr = new Number((float) jsonDouble);
            setStmt = new SetAttributeTo(keyExpr, numExpr);
            list.add(setStmt);

            keyExpr = new Str(SIZE_KEY);
            jsonDouble = actorDefinitionNode.get(SIZE_KEY).asDouble();
            numExpr = new Number((float) jsonDouble);
            setStmt = new SetAttributeTo(keyExpr, numExpr);
            list.add(setStmt);

            keyExpr = new Str(DIRECTION_KEY);
            jsonDouble = actorDefinitionNode.get(DIRECTION_KEY).asDouble();
            numExpr = new Number((float) jsonDouble);
            setStmt = new SetAttributeTo(keyExpr, numExpr);
            list.add(setStmt);

            keyExpr = new Str(DRAG_KEY);
            jsonBool = actorDefinitionNode.get(DRAG_KEY).asBoolean();
            boolExpr = new Bool(jsonBool);
            setStmt = new SetAttributeTo(keyExpr, boolExpr);
            list.add(setStmt);

            Logger.getGlobal().warning("rotationStyle not implemented");
        }



        list.addAll(DeclarationStmtParser.parseListSetStmts(actorDefinitionNode.get("lists"), actorName));
        list.addAll(DeclarationStmtParser.parseVariableSetStmts(actorDefinitionNode.get("variables"), actorName));
        return new SetStmtList(list);
    }


}

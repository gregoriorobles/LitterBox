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
import static org.junit.Assert.assertEquals;

import analytics.IssueReport;
import analytics.finder.BroadcastSync;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import scratch.data.ScBlock;
import scratch.data.Script;
import scratch.structure.Project;
import scratch.structure.Stage;
import utils.Identifier;
import utils.Version;

public class BroadcastSyncTest {

    @Test
    public void validateCheck() {
        Project project = new Project();
        project.setVersion(Version.SCRATCH3);
        List<Script> scripts = new ArrayList<>();
        List<ScBlock> blocks = new ArrayList<>();
        Script script = new Script();
        List<ScBlock> blocks2 = new ArrayList<>();
        Script script2 = new Script();
        ScBlock block1 = new ScBlock();
        ScBlock block2 = new ScBlock();
        block1.setContent(Identifier.RECEIVE.getValue());
        block2.setContent(Identifier.BROADCAST.getValue());
        Map<String, List<String>> fields = new HashMap<>();
        fields.put(Identifier.FIELD_RECEIVE.getValue(), Collections.singletonList("variable1"));
        block1.setFields(fields);
        Map<String, List<String>> inputs = new HashMap<>();
        List<String> in = new ArrayList<>();
        in.add("blank");
        in.add("blank");
        in.add("variable2");
        inputs.put(Identifier.FIELD_BROADCAST.getValue(), in);
        block2.setInputs(inputs);
        blocks.add(block1);
        blocks2.add(block2);
        script.setBlocks(blocks);
        script2.setBlocks(blocks2);
        double[] pos = {1.0, 1.0};
        script.setPosition(pos);
        script2.setPosition(pos);
        scripts.add(script);
        scripts.add(script2);
        Stage stage = new Stage("Stage", scripts, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0, null);
        project.setStage(stage);
        project.setSprites(new ArrayList<>());
        project.setPath("Test");
        BroadcastSync detector = new BroadcastSync();
        IssueReport iR = detector.check(project);

        assertEquals(2, iR.getCount());
        assertEquals("Broadcast with no Receive: variable2", iR.getPosition().get(0));
        assertEquals("Receive with no Broadcast: variable1", iR.getPosition().get(1));
    }
}
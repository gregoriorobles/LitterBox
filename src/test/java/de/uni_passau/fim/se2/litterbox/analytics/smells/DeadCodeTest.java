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
package de.uni_passau.fim.se2.litterbox.analytics.smells;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uni_passau.fim.se2.litterbox.analytics.Issue;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import de.uni_passau.fim.se2.litterbox.ast.parser.ProgramParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class DeadCodeTest {
    private static Program empty;
    private static Program deadCode;
    private static Program deadVariable;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static Program deadParam;
    private static Program allDead;

    @BeforeAll
    public static void setUp() throws IOException, ParsingException {

        File f = new File("./src/test/fixtures/emptyProject.json");
        empty = ProgramParser.parseProgram(f.getName(), mapper.readTree(f));
        f = new File("./src/test/fixtures/smells/deadCode.json");
        deadCode = ProgramParser.parseProgram(f.getName(), mapper.readTree(f));
        f = new File("./src/test/fixtures/smells/deadVariable.json");
        deadVariable = ProgramParser.parseProgram(f.getName(), mapper.readTree(f));
        f = new File("./src/test/fixtures/smells/allBlocksDead.json");
        allDead = ProgramParser.parseProgram(f.getName(), mapper.readTree(f));
        f = new File("./src/test/fixtures/smells/deadParam.json");
        deadParam = ProgramParser.parseProgram(f.getName(), mapper.readTree(f));
    }

    @Test
    public void testEmptyProgram() {
        DeadCode parameterName = new DeadCode();
        Set<Issue> reports = parameterName.check(empty);
        Assertions.assertEquals(0, reports.size());
    }

    @Test
    public void testDeadCode() {
        DeadCode parameterName = new DeadCode();
        Set<Issue> reports = parameterName.check(deadCode);
        Assertions.assertEquals(3, reports.size());
    }

    @Test
    public void testDeadVariable() {
        DeadCode parameterName = new DeadCode();
        Set<Issue> reports = parameterName.check(deadVariable);
        Assertions.assertEquals(2, reports.size());
    }

    @Test
    public void testDeadParam() {
        DeadCode parameterName = new DeadCode();
        Set<Issue> reports = parameterName.check(deadParam);
        Assertions.assertEquals(2, reports.size());
    }

    @Test
    public void testAllDead() {
        DeadCode parameterName = new DeadCode();
        Set<Issue> reports = parameterName.check(allDead);
        Assertions.assertEquals(126, reports.size());
    }
}

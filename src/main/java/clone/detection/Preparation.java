package clone.detection;

import java.util.ArrayList;
import java.util.List;

import scratch2.data.ScBlock;
import scratch2.data.Script;
import scratch2.structure.Project;
import scratch2.structure.Sprite;

/**
 * This class prepares the code for the normalization and reads the scripts.
 */
public class Preparation {
	
	/**
	 * Joins the scripts and make a string list.
	 * @param project The project who saves the script.
	 * @return The script as a string list.
	 */
	public List<String> codePreparation(Project project){
		List<Script> script = joinScriptLists(project);
		List<String> blockScript = getContentFromScript(script);
		return blockScript;
	}
	
	// Joins the script of all sprites and stage.
	private List<Script> joinScriptLists(Project project) {
		List<Script> script = project.getStage().getScripts();
		
		// Added null to see the end of the sprite.
		script.add(null);
		List<Sprite> sprites = project.getSprites();
		for(Sprite sp : sprites) {
        	List<Script> spriteScript = sp.getScripts();
            script.addAll(spriteScript);
            script.add(null);
        }
		return script;
	}
	
	// Reads and writes the code from the scripts.
	private List<String> getContentFromScript(List<Script> script) {
		List<String> scripts = new ArrayList<String>();
		int scriptNr = 0;
		int spriteNr = 0;
		for(Script sc : script) {
			if(sc != null) {
			    scriptNr++;
			    for(ScBlock bl : sc.getBlocks()) {
				    getContentFromBlock(bl, scripts);
			    }
			    scripts.add("~endBlock~" + scriptNr);
			    scripts.add("~endBlock~" + scriptNr + "'");
			} else {
				spriteNr++;
				scripts.add("~endSprite~" + spriteNr);
				scripts.add("~endSprite~" + spriteNr + "'");
			}
		}
		return scripts;
	}
	
	// Reads and writes the code from a block.
	private List<String> getContentFromBlock(ScBlock block, List<String> scripts){
	    scripts.add(block.getContent());
		if (block.getNestedBlocks() != null) {
            for (ScBlock nested : block.getNestedBlocks()) {
            	getContentFromBlock(nested, scripts);
            }
        }
		if (block.getElseBlocks() != null) {
            for (ScBlock elseBl : block.getElseBlocks()) {
            	getContentFromBlock(elseBl, scripts);
            }
        }
		return scripts;
	}
}

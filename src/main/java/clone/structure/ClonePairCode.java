package clone.structure;

import java.util.List;

/**
 * This class saves the code of two cloned blocks.
 */
public class ClonePairCode {

	private List<String> blockOne;
	private List<String> blockTwo;
	
	/**
	 * Creates a new ClonePairCode with the given blocks.
	 * @param blockOne The first code block.
	 * @param blockTwo The clone of the code block.
	 */
	public ClonePairCode(List<String> blockOne, List<String> blockTwo) {
		this.blockOne = blockOne;
		this.blockTwo = blockTwo;
	}
	
	/**
	 * The getter for the first block.
	 * @return blockOne
	 */
	public List<String> getBlockOne() {
		return blockOne;
	}
	
	/**
	 * The getter for the second block.
	 * @return blockTwo
	 */
	public List<String> getBlockTwo() {
		return blockTwo;
	}
}
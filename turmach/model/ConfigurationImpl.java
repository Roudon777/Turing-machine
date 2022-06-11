package turmach.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import turmach.model.TuringMachine.Direction;

public class ConfigurationImpl implements Configuration {

	private TuringMachine tm;
	
	//private Printer printer = new Printer("init", 0);
	//Use two arraylists to represent the tape from negative infinity to positive infinity
	private Map<Integer, Character> tape = new HashMap<Integer, Character>('.') ;    //symbol of each cell
	//Maintain the left and right ends of two variables, update them for each operation
	private int left, right;
	//build a ArrayList to store ConfigurationObserver
	List<ConfigurationObserver> cfgOb = new ArrayList<ConfigurationObserver>();
	private String state = "init";
	private int curPosition;
	public ConfigurationImpl( TuringMachine tm ){
		this.tm = tm;
		//this.printer = new Printer(getInitialState(), 0);
		setTapeCell(0, '.');
		//this.left = printer.curPosition();
		//this.right = printer.curPosition();
		this.curPosition = 0;
		this.left = curPosition;
		this.right = curPosition;
	}
	
	@Override
	public List<Character> getSymbolList() {
		return tm.getSymbolList();
	}
	
	@Override
	public char getBlankSymbol() {
		return tm.getBlankSymbol();
	}

	@Override
	public String getInitialState() {
		return tm.getInitialState();
	}

	@Override
	public int getRuleCount() {
		return tm.getRuleCount();
	}

	@Override
	public String getRuleState(int ruleNumber) {
		return tm.getRuleState(ruleNumber);
	}

	@Override
	public String getRuleNewState(int ruleNumber) {
		return tm.getRuleNewState(ruleNumber);
	}

	@Override
	public char getRuleSymbol(int ruleNumber) {
		return tm.getRuleSymbol(ruleNumber);
	}

	@Override
	public char getRuleNewSymbol(int ruleNumber) {
		return tm.getRuleNewSymbol(ruleNumber);
	}

	@Override
	public Direction getRuleDirection(int ruleNumber) {
		return tm.getRuleDirection(ruleNumber);
	}

	@Override
	public void changeRule(int ruleNumber, String state, char symbol, char newSymbol, Direction direction,
			String newState) {
		tm.changeRule(ruleNumber, state, symbol, newSymbol, direction, newState);
		notifyChange();
	}
	
	@Override
	public void addRule(int ruleNumber, String state, char symbol, char newSymbol, Direction direction,
			String newState) {
		tm.addRule(ruleNumber, state, symbol, newSymbol, direction, newState);
		notifyChange();
	}

	@Override
	public void removeRule(int ruleNumber) {
		tm.removeRule(ruleNumber);
		notifyChange();
	}

	@Override
	public void addSymbol(char symbol) {
		tm.addSymbol(symbol);
		notifyChange();
	}

	@Override
	public void removeSymbol(char symbol) {
		tm.removeSymbol(symbol);
		notifyChange();
	}
	/** Get the left end of the interesting part of the tape.
	 * @ensures The result should be such that
	 *      getTapeCellSymbol(i) = getBlankSymbol(),
	 *      for all i less than result
	 *  and that getCurrentPosition() is greater or equal to the result. */
	@Override
	public int getLeftEnd() {
		return this.left;
	}
	/** Get the right end of the interesting part of the tape.
	 * @ensures The result should be such that
	 *     getTapeCellSymbol(i) = getDefaultSymbol(),
	 *     for all i greater than result
	 *  and at getCurrentPosition() is less or equal to the result. */
	@Override
	public int getRightEnd() {
		return this.right;
	}
	/** Get the current position of the read/write head in this configuration.
	 */
	@Override
	public int getCurrentPosition() {
		//return this.printer.curPosition();
		return this.curPosition;
	}
	/** Return the current state of the configuration. */
	@Override
	public String getCurrentState() {
		return this.state;
	}
	/** Get value of the tape at the given position. 
	 */
	//Judge whether it is a positive number, if it is, look for it from the positive number tape, otherwise look for it from the negative number tape
	@Override
	public char getTapeCellSymbol(int position) {
		if(position >= left && position <= right )
			return tape.get( position );
		else
			return '.';
	}
	/** Change to any next configuration
	 * Requires canAdvance() ;
	 * Ensures: the final state of this object is a next configuration 
	 *         of its initial state.
	 */
	//traverse Ruleset,if there is a rule conform to current state and current position,it can advance
	@Override
	public boolean canAdvance() {
		
		for( int index = 0; index < this.getRuleCount(); index++ ) {
			if( this.tm.getRuleState(index).equals( this.state ) &&
					getTapeCellSymbol(curPosition) == this.tm.getRuleSymbol(index) ) {
				return true;
				
			}
		}
		return false ;
	}
	//find which number of rule made it canadvance,and update current state and symbol to correspond new state and symbol  
	@Override
	public void advance() {
		int index = 0;
		System.out.println("hhhhh: ");
			for( int i = 0; i < this.getRuleCount(); i++ ) {
				if( this.tm.getRuleState(i).equals( this.state ) &&
						getTapeCellSymbol(curPosition) == this.tm.getRuleSymbol(i) ) {
					index = i ;
				break;
				}
			}
		
		System.out.println("index: " + index);
		System.out.println("newSymbol: " + this.tm.getRuleNewSymbol(index));
		
		setTapeCell( curPosition, this.tm.getRuleNewSymbol(index) );
		
		if( tm.getRuleDirection(index).equals( Direction.RIGHT ) ) 
			setPosition( curPosition+1 );
		else
			setPosition( curPosition-1 );
		
		if( curPosition <= left)
			left = curPosition;
		if( curPosition >= right )
			right = curPosition;
		
		setCurrentState( this.getRuleNewState( index ) );
		
		notifyChange();
	}
	//update leftend and rightend,set new value to  the  tape
	/** Change one cell of the tape
	 * 
	 * @param position
	 * @param newValue
	 * 
	 * @ensures that the value of the tape at `position` is `newValue`
	 * and that for all `i`, 
	 *       if `i != position`
	 *       then the value of the tape at position `i` is the same
	 *       as the value of the old tape at position `i`
	 */
	@Override
	public void setTapeCell(int position, char newValue) {
//		if(  tape.containsKey( position ) ) {
//		if( newValue==tm.getBlankSymbol()) {
//				tape.remove(position);
//		} else {
//				tape.remove(position);
//				tape.put(position, newValue);		
//		}
//		}
//	 else {
//		if( newValue==tm.getBlankSymbol() ) {
//			// New and old values are the same 
//		} else {
//				tape.put(position, newValue);						
//		}
//	}
		this.tape.put(position, newValue);
		if( position <= left)
			left = curPosition;
		else if( position >= right )
			right = curPosition;
		notifyChange();
		
		
	}
	/* Change the position of the configuration.
	 * 
	 */
	@Override
	public void setPosition(int position) {
		if(tape.containsKey(position)) {
			this.curPosition = position;
		}
		else {
			this.curPosition = position;
			tape.put(position, tm.getBlankSymbol());
		}
		if(position >= this.right)
			this.right = position;
		else if(position <= this.left)
			this.left = position;
		notifyChange();
	}
	/** Change the state of the configuration.
	 * 
	 * @requires state != null
	 * @param state
	 */
	@Override
	public void setCurrentState(String state) {
		this.state = state;
		notifyChange();
	}
	/** Add an observer object to the list of observers.
	 * Note that observers will be notified after any change
	 * to this object.
	 * @param symbol
	 * @requires o != null
	 * @ensures that the list of observes will be the same as before except
	 * that it will contain `o`.
	 */
	@Override
	public void addObserver(TMObserver o) {
		tm.addObserver(o);
	}
	/** Remove an observer
	 * @param symbol
	 * @ensures that the list of observers will be the same as before except
	 * that it will not contain `o`.
	 */
	@Override
	public void deleteObserver(TMObserver o) {
		tm.deleteObserver(o);
	}
	//every time call,add an observer to the list
	@Override
	public void addConfigurationObserver(ConfigurationObserver o) {
		cfgOb.add(o);
	}
	
	@Override
	public void deleteConfigurationObserver(ConfigurationObserver o) {
		cfgOb.remove(o);
		
	}
	//told Gui there are something changed on Configuration
	private void notifyChange() {
		for( ConfigurationObserver c : cfgOb ) {
			c.notifyChangeToConfiguration( this );
		}
	}
	
	

}

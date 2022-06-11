package turmach.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class TuringMachineImpl implements TuringMachine,TuringMachine.TMObserver{

	private List<Character> symbels = new ArrayList<Character>();
	//build Turrules class
	private static record rule(
			String initState,
			char initSymbol,
			char repSymbol,
			Direction direction,
			String newState) { };
	
	private Map<Integer, rule> rules = new HashMap<Integer, rule>() ;
	//build a ArrayList to store TuringMachine Observer
	List<TMObserver> OBC = new ArrayList<TMObserver>();
	//Construct the initial symbol
	public TuringMachineImpl(){
		addSymbol('0');
		addSymbol('1');
		addSymbol('.');
		addSymbol('(');
		addSymbol(')');
		this.OBC = new Vector<TMObserver>();
	}

	/** Return a list of all symbols (characters) that this machine uses */
	@Override
	public List<Character> getSymbolList() {
		return this.symbels;
	}
	/** Get the blank symbol. New tapes will contain only this symbol */
	@Override
	public char getBlankSymbol() {
		return '.';
	}
	/** Return the number of rules.
	 * 
	 * @return
	 */
	@Override
	public String getInitialState() {
		return "Init";
	}
	/** Return the number of rules.
	 * 
	 * @return
	 */
	@Override
	public int getRuleCount() {
		return rules.size();
	}
	/** Return the source state of a rule.
	 * 
	 * @param ruleNumber
	 * 
	 * @require 0 <= ruleNumber && rulNumber < getRuleCount()
	 * @return the rule's source state.
	 */
	// Rule represent the ruleNumber's rule,find the corresponding item of rule 
	@Override
	public String getRuleState(int ruleNumber) {
		if( 0 <= ruleNumber && ruleNumber < getRuleCount() ) {}
		
		rule Rule = this.rules.get(ruleNumber);
		return Rule.initState();
	}
	/** Return the target state of a rule.
	 * 
	 * @param ruleNumber
	 * 
	 * @require 0 <= ruleNumber && rulNumber < getRuleCount()
	 * @return the rule's target state.
	 */
	@Override
	public String getRuleNewState(int ruleNumber) {
		if( 0 <= ruleNumber && ruleNumber < getRuleCount() ) {}
		rule Rule = this.rules.get(ruleNumber);
		return Rule.newState();
	}
	/** Return the trigger symbol of a rule.
	 * 
	 * @param ruleNumber
	 * 
	 * @require 0 <= ruleNumber && rulNumber < getRuleCount()
	 * @return the rule's trigger symbol.
	 */
	@Override
	public char getRuleSymbol(int ruleNumber) {
		if( 0 <= ruleNumber && ruleNumber < getRuleCount() ) {}
		rule Rule = this.rules.get(ruleNumber);
		return Rule.initSymbol();
	}
	/** Return the replacement symbol of a rule.
	 * 
	 * @param ruleNumber
	 * 
	 * @require 0 <= ruleNumber && rulNumber < getRuleCount()
	 * @return the rule's replacement symbol.
	 */
	@Override
	public char getRuleNewSymbol(int ruleNumber) {
		if( 0 <= ruleNumber && ruleNumber < getRuleCount() ) {}
		rule Rule = this.rules.get(ruleNumber);
		return Rule.repSymbol();
	}

	@Override
	public Direction getRuleDirection(int ruleNumber) {
		if( 0 <= ruleNumber && ruleNumber < getRuleCount() ) {}
		rule Rule = this.rules.get(ruleNumber);
		return Rule.direction();
	}
	/** 
	 * @param ruleNumber
	 * @requires 0 <= ruleNumber && rulNumber < getRuleCount()
	 *   and sourceState != null and direction != null and newState!=null
	 * @ensures The rule with this number will be as specified in
	 * the parameters. All other rules are unchanged and the number
	 * of rules is unchanged.
	 */
	// replace the old rule by new rule
	@Override
	public void changeRule(int ruleNumber, String state, char symbol, char newSymbol, Direction direction,
			String newState) {
		if( 0 <= ruleNumber && ruleNumber < getRuleCount() ) {}
		rule newRule = new rule(state, symbol, newSymbol, direction, newState);
		this.rules.replace(ruleNumber, newRule);
		informObserver();
	}
	/** 
	 * @param ruleNumber
	 * @requires 0 <= ruleNumber && rulNumber <= getRuleCount()
	 *   and sourceState != null and direction != null and newState!=null
	 * @ensures The rule with this number will be as specified in
	 * the parameters. All rule before it are unchanged.
	 * All rules originally at or after ruleNumber are unchanged but their
	 * rule number is increased by one. The number of rules will increase
	 * by one.
	 */
	@Override
	public void addRule(int ruleNumber, String state, char symbol, char newSymbol, Direction direction,
			String newState) {
		if( 0 <= ruleNumber && ruleNumber < getRuleCount() ) {}
		rule newRule = new rule(state, symbol, newSymbol, direction, newState);
		this.rules.put(ruleNumber, newRule);
		informObserver();
	}
	/** 
	 * @param ruleNumber
	 * @requires 0 <= ruleNumber && rulNumber < getRuleCount()
	 * @ensures The rule with this number will be deleted.
	 * All rules before it are unchanged.
	 * All rules after this one are unchanged, but they will
	 * have their rule number decreased by one.
	 * The number of rules will decrease by one.
	 */
	@Override
	public void removeRule(int ruleNumber) {
		if( 0 <= ruleNumber && ruleNumber < getRuleCount() ) {}
		this.rules.remove(ruleNumber);
		informObserver();
	}

	/** Add one symbol to the list of symbols.
	 * @ensures that the list of symbols includes `symbol` and
	 * also all symbols that were on the list before.  If
	 * `symbol` was already on the list, there should be no change.
	 * @param symbol
	 */
	@Override
	public void addSymbol(char symbol) {
		symbels.add( symbol );
		informObserver();
	}
	/** Remove one symbol from the list of symbols.
	 * 
	 * @param symbol
	 * @ensures that `symbol` is not on the list of symbols but all other
	 * symbols that were on the list are still on the list. If `symbol` was
	 * not originally on the list, then it is removed from the list.
	 */
	@Override
	public void removeSymbol(char symbol) {
		symbels.remove( symbol );
		informObserver();
	}

	/** Add an observer object to the list of observers.
	 * Note that observers will be notified after any change
	 * to this object
	 * @param symbol
	 * @requires o != null
	 * @ensures that the list of observes will be the same as before except
	 * that it will contain `o`.
	 */
	@Override
	public void addObserver(TMObserver o) {
		OBC.add(o);
		
	}
	/** Delete an observer object to the list of observers.
	 * Note that observers will be notified after any change
	 * to this object
	 * @param symbol
	 * @ensures that the list of observers will be the same as before except
	 * that it will not contain `o`.
	 */
	@Override
	public void deleteObserver(TMObserver o) {
		// TODO Auto-generated method stub
		OBC.remove(o);
	}
	//told Configuration there are something changed on TruringMachine
	private void informObserver() {
		for(TMObserver t : OBC) {
			t.notifyChangeToTM(this);
		}
	}

	@Override
	public void notifyChangeToTM(TuringMachine tm) {
		// TODO Auto-generated method stub
		
	}

}

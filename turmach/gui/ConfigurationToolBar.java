package turmach.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import turmach.model.Configuration;

public class ConfigurationToolBar extends JToolBar {

	private static final long serialVersionUID = -143838180898711557L;
	private Configuration model;
	private List<Character> cachedSymbolList = null ;
	private JButton advanceButton;
	private JTextField currentStateField;

	ConfigurationToolBar( Configuration configuration ) {
		this.model = configuration ;
		refreshButtons( this ) ;
	}
	
    void refresh() {
    	List<Character> symbolList = model.getSymbolList() ;
    	if( !symbolList.equals( cachedSymbolList) ) {
    		cachedSymbolList = symbolList ;
    		refreshButtons( this ) ;
    	}
    	advanceButton.setEnabled(model.canAdvance());
    	this.currentStateField.setText( model.getCurrentState() ) ;
    	repaint() ;
    }
	

	
	private void refreshButtons( JToolBar toolBar ) {
		// Remove all buttons 
		while( toolBar.getComponentCount() > 0 )
			toolBar.remove( toolBar.getComponent(0) );
		
		JButton button ;
		ActionListener listener ;

		
		// Go to the initial state
		button = new JButton( "Reset" ) ;
		listener = new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				System.out.println( "Reset " ) ;
				System.out.println("getCurrentPosition" +model.getCurrentPosition() ) ;
				System.out.println("getCurrentState" + model.getCurrentState() ) ;
				System.out.println( "getLeftEnd" +model.getLeftEnd() ) ;
				System.out.println("getRightEnd" + model.getRightEnd() ) ;
				for(int i=model.getCurrentPosition()-2;i<model.getCurrentPosition()+3;i++) {
				System.out.println( model.getTapeCellSymbol(i) ) ;
				}
				String inital = model.getInitialState();
				model.setCurrentState(inital);
				model.setPosition(0);
			}
		};
		button.addActionListener(listener);
		toolBar.add( button ) ;
		
		// Move left to next blank
		//
		button = new JButton( "<<" ) ;
		listener = new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				System.out.println( "<< " ) ;
				System.out.println("getCurrentPosition" +model.getCurrentPosition() ) ;
				System.out.println("getCurrentState" + model.getCurrentState() ) ;
				System.out.println( "getLeftEnd" +model.getLeftEnd() ) ;
				System.out.println("getRightEnd" + model.getRightEnd() ) ;
				for(int i=model.getCurrentPosition()-2;i<model.getCurrentPosition()+3;i++) {
				System.out.println( model.getTapeCellSymbol(i) ) ;
				}
				//move the position one cell to the left and then keep moving it left until a blank cell is at the position.
				while(true) 
				{
					moveLeft();
					if(model.getTapeCellSymbol(model.getCurrentPosition())=='.')break;
					}
			}
		};
		button.addActionListener(listener);
		toolBar.add( button ) ;
		
		// Move left
		button = new JButton( "<" ) ;
		listener = new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				moveLeft();
			}
		};
		button.addActionListener(listener);
		toolBar.add( button ) ;

		
		// For each symbol in the machine. Make one button
		java.util.List<Character> symbols = model.getSymbolList() ;
		
		for( Character symbol : symbols ) {
			button = new JButton( Character.toString( symbol ) )  ;
			listener = new ActionListener() {
				@Override
				public void actionPerformed( ActionEvent e ) {
					model.setTapeCell( model.getCurrentPosition(), symbol );
					moveRight();
				}
			};
			button.addActionListener(listener);
			toolBar.add( button ) ;
		}
		System.out.println(symbols.size());

		// Move Right
		button = new JButton( ">" ) ;
		listener = new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				moveRight();
//				System.out.println("curPostion: " + model.getCurrentPosition());
//				System.out.println("Left: " + model.getLeftEnd());
//				System.out.println("Right: " + model.getRightEnd());
//				System.out.println("RuleCount: " + model.getRuleCount());
				
				
			}
		};
		button.addActionListener(listener);
		toolBar.add( button ) ;

		
		// Move right to next blank
		button = new JButton( ">>" ) ;
		listener = new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
//				model.setPosition(model.getRightEnd());
				//move the position one cell to the right and then keep moving it left until a blank cell is at the position.
				while(true) 
				{
					moveRight();
					if(model.getTapeCellSymbol(model.getCurrentPosition())=='.')break;
					}
			}
		};
		button.addActionListener(listener);
		toolBar.add( button ) ;
		
		JLabel label = new JLabel( "CurrentState: " ) ;
		toolBar.add( label ) ;
		
		this.currentStateField = new JTextField(10) ;
		toolBar.add( this.currentStateField ) ;
		this.currentStateField.setText( model.getCurrentState() );
		
		this.advanceButton = new JButton( "Advance" ) ;
		listener = new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				model.advance();
				//refresh();
			}
		};
		advanceButton.addActionListener(listener);
		toolBar.add( this.advanceButton ) ;
		
		
		
		
		
	}
	
	private void moveLeft() {
		int p = model.getCurrentPosition() ;
		model.setPosition(p-1) ;
	}
	
	private void moveRight() {
		int p = model.getCurrentPosition() ;
		model.setPosition(p+1) ;
	}
	
	private void changeTapeTo( char symbol ) {
		int p = model.getCurrentPosition() ;
		model.setTapeCell(p, symbol);
	}
}

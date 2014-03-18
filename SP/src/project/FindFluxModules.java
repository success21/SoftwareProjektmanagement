package project;

import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.xml.stream.XMLStreamException;


import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.test.gui.*;


public class FindFluxModules{
	
	

	public static void main(String args[]){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("1");
		}
			
		try {
			new JSBMLvisualizer(SBMLReader.read(new File(args[0])));
		} catch (XMLStreamException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("2");
		}
		
		
	}
	

	
	
}
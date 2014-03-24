package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;

public class SBMLLoad {
	
	//getter...
	
	
	//these variables get set by loadSBML
		//number of reactions
		private static int numR;
		public static int getNumR(){
			return numR;
		}
		//number of metabolites (species)
		private static int numS;
		public static int getNumS(){
			return numS;
		}
		//model
		private static Model m;
		public static Model getModel(){
			return m;
		}
		//id's of all reactions
		private static String[] rct;
		public static String[] getReactions(){
			return rct;
		}
		//id's of all metabolites
		private static String[] met;
		public static String[] getMetabolites(){
			return met;
		}
	//these variables get set by optimumReaction
		//position of optimum 
		private static int optPosInMet = 0;
		public static int getOptimal(){
			return optPosInMet;
		}
		//id of optimum
		private static String id = null;
		public static String getOptimalId(){
			return id;
		}
		
		
		
	//setter
	
	//load model and set numR/numS and set 
	public static void loadSBML(String path) throws XMLStreamException, IOException{
		File file = new File("/home/guru/Downloads/S_aureus_iSB619.xml");
		SBMLDocument d = SBMLReader.read(file);
		Model m = d.getModel();
		
		numR = m.getNumReactions();
		numS = m.getNumSpecies();
		
		rct = new String[m.getNumReactions()];
		met = new String[m.getNumSpecies()];
		
		for(int i=0;i<m.getNumReactions();i++){
			Reaction r = m.getReaction(i);
			String id = r.getId();
			rct[i] = id;
		}
		
		for(int i=0;i<m.getNumSpecies();i++){
			Species s = m.getSpecies(i);
			String id = s.getId();
			met[i] = id;
		}
	}
	
	
	
	//find position of metatolite in met[] for objective function
	public static void optimumReaction(File file) throws FileNotFoundException{
		
		Scanner scanner = new Scanner(file);
		try {
			while (scanner.hasNextLine()) {
		    	String line = scanner.nextLine();
		     
		        if(line.contains("<reaction id=")){
		        	Scanner scan = new Scanner(line);
		        	scan.useDelimiter("\"");
		        	scan.next();
		        	id = scan.next();
		        	scan.close();
		        }
		        if(line.contains("OBJECTIVE_COEFFICIENT") && line.contains("value=\"1\"")){
		        	
		        	System.out.println(id);
		        	scanner.close();
		        	
		        	break;
		        }
		    }
		} catch(NoSuchElementException e) { 
		    System.out.println("Error searching for optimum reaction.");
		}
		
		scanner.close();

		for(int i=0;i<numS;i++){
			if(id==met[i]){
				optPosInMet = i;
				break;
			}
		}
		
	}


	
}

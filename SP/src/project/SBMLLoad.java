package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.LocalParameter;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;



public class SBMLLoad {



	
		//these variables get set by loadSBML
			//number of reactions
			private int numR;
			//number of metabolites (species)
			private int numS;
			//model
			private Model model;
			//id's of all reactions
			private  String[] rct;
			//id's of all metabolites
			private  String[] met;
			//sbml file
			private  File file;
			//objectiveFunction
			private double[] objectiveFunction;
			
	
	//getter

			public int getNumR() {
						return numR;
					}
			public int getNumS() {
				return numS;
			}
			public Model getModel() {
				return model;
			}
			public String[] getRct(){ 
				return rct;
			}
			public String[] getMet() {
				return met;
			}
			public File getFile() {
				return file;
			}
			public double[] getObjectiveFunction(){
				return objectiveFunction;
			}
			
			
			
			
	//methods set the values
			
		
	//load model and set numR/numS and set 
	public void loadSBML(String path) throws XMLStreamException, IOException{
		file = new File(path);
		System.out.println("loading SBML file");
		SBMLDocument d = SBMLReader.read(file);
		model = d.getModel();
		
		numR = model.getNumReactions();
		numS = model.getNumSpecies();
		
		rct = new String[model.getNumReactions()];
		met = new String[model.getNumSpecies()];
		
		for(int i=0;i<model.getNumReactions();i++){
			Reaction r = model.getReaction(i);
			String id = r.getId();
			rct[i] = id;
		}
		
		for(int i=0;i<model.getNumSpecies();i++){
			Species s = model.getSpecies(i);
			String id = s.getId();
			met[i] = id;
		}
		
		
		//set objective function
		objectiveFunction = new double[numR];
		
		for(int i=0;i<numR;i++){
			Reaction r = model.getReaction(i);
			KineticLaw k = r.getKineticLaw();
			LocalParameter p = k.getLocalParameter("OBJECTIVE_COEFFICIENT");
			double v = p.getValue();
			
			if(v==0.0){
				objectiveFunction[i] = 0;
			}
			else{
				objectiveFunction[i] = 1;
				System.out.println("para: "+v);
				System.out.println("rct: "+r.getId());
			}
			
		}
		
		 
		 
		//test
		if(model != null && numR !=0 && numS !=0 && (rct.length>0) && (met.length>0)){	}
		else{System.out.println("Error load data in loadSBML."); System.exit(1);}
			
	}	
	







	
	

			
	
}

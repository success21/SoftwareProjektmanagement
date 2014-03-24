package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.xml.stream.XMLStreamException;
import org.sbml.jsbml.*;



public class FindFluxModules{
	
	

	public static void main(String args[]) throws XMLStreamException, IOException{
		
		File file = new File("/home/guru/Downloads/PLUSPARAMETERM_barkeri_iAF692.xml");
		
		//load the sbml document
		System.out.println("Loading SBML Document...");
		SBMLDocument d = SBMLReader.read(file);
		Model m = d.getModel();
		
		
		//create reference arrays for reactions and metabolites
		int numR = m.getNumReactions();
		int numS = m.getNumSpecies();
		String[] rct = new String[m.getNumReactions()];
		String[] met = new String[m.getNumSpecies()];
		
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
		
		//array with reactions and metabolites 
		int[][] rctMetArr = new int[m.getNumReactions()][m.getNumSpecies()];
		int count =0;
		int correct = 0;
		
		System.out.println("Creating Reaction/Metabolites Matrix...");
		//calculates the values of rctMetArr
		for(int i=0;i<numR;i++){					//all reactions
			for(int k=0;k<numS;k++){				//all metabolites
			//compares reactants and products with metabolites (species) and set the stochiometric value in the matrix
				Reaction r = m.getReaction(i);
				int w = r.getNumReactants();		
					for(int t=0;t<w;t++){			//all reactants
						SpeciesReference s = r.getReactant(t);
						Species sp = s.getSpeciesInstance();
						String id = sp.getId();
						if(id == met[k]){
							rctMetArr[i][k] = (int) -(r.getReactant(t).getStoichiometry()); correct++;
						}
					}
				int z = r.getNumProducts();
					for(int t=0;t<z;t++){			//all products
						SpeciesReference s = r.getProduct(t);
						Species sp = s.getSpeciesInstance();
						String id = sp.getId();
						if(id == met[k]){
							rctMetArr[i][k] = (int) r.getProduct(t).getStoichiometry(); correct++;
						}
					}
			}
		}
		
		
		
		/*
		//prints matrix
		for(int i=0;i<numR;i++){
			for(int k=0;k<numS;k++){
					System.out.print(rctMetArr[i][k]);
			}
			System.out.println();
		}
		*/
		
		//tests
		
		
		for(int i=0;i<numR;i++){					//all reactions
			for(int k=0;k<numS;k++){
				if(rctMetArr[i][k]!=0){
					count++;
				}
				
			}
		}
		double wus=0;
		int error=0;
		
		for(int i=0;i<numR;i++){
			Reaction r = m.getReaction(i);
			try{
				wus += r.getNumReactants();
			}
			catch(NullPointerException tt){
				error++;
			}
			
			try{
				wus += r.getNumProducts();
			}
			catch(NullPointerException tt){
				error++;
			}
		}
		
		System.out.println(count+":"+correct+":"+wus+":"+error);
		
		
		//go on with finding optimum
		String optimum = optimumReaction(file);
		int optPosInMet = 0;
		for(int i=0;i<numS;i++){
			if(optimum==met[i]){
				optPosInMet = i;
				break;
			}
		}
		
		gauss(rctMetArr, optPosInMet);
		
		
	System.out.println("finished");	
	}	

	//calculate optimum S*v=0. find all vectors which solve the equation and maximize x.
	public static void gauss(int[][] arr, int x) {
		
			
		}


	//find optimum "objective function". returns id of reaction.
	public static String optimumReaction(File file) throws FileNotFoundException{
	
		Scanner scanner = new Scanner(file);
		try {
			String id = null;
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
		        	return id;
		        }
		    }
		} catch(NoSuchElementException e) { 
		    System.out.println("Error searching for optimum reaction.");
		}
		
		scanner.close();
		return null;
		
	}


}



package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.xml.stream.XMLStreamException;
import lpsolve.LpSolveException;

import org.sbml.jsbml.*;

import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LinearProgram;


// boolean = s.getBoundaryCondition();

public class FindFluxModules{
	
	
	public static void findFlux(){
		
		//get values from SBMLLoad
		int numR = SBMLLoad.getNumR();
		int numS = SBMLLoad.getNumS();
		Model m = SBMLLoad.getModel();
		
		//build matrix
		double[][] rctMetArr = matrixBuild(numR,numS,met,m);
		
		
		//find value for objective function
		SBMLLoad.optimumReaction();
		
		//find optimum using LP
		double[] k = gauss(rctMetArr,numR,numS,m,optPosInMet);
				
	}
	public static void main(String args[]) throws XMLStreamException, IOException, LpSolveException{
		
		
		
		
		
		
		
		
		for(double v: k){
			System.out.println(v);
		}
		
	System.out.println("finished");	
	}	

	//calculate optimum S*v=0. find all vectors which solve the equation and maximize x.
	public static double[] gauss(double[][] matrix, int numR , int numS, Model m, int optPosInMet) throws LpSolveException {
		double[] obj = new double[numR];
		for(int i=0;i<numR;i++){
			obj[i] = matrix[i][optPosInMet];
		}
		LinearProgram lp = new LinearProgram(obj); 
	      
		for(int i=0;i<numS;i++){		
			double[] constraint = new double[numR];
			for(int k=0;k<numR;k++){	
				constraint[k] = matrix[k][i] ;				
			}
			if(i==optPosInMet){
				continue;
			}
			else{
				lp.addConstraint(new LinearBiggerThanEqualsConstraint(constraint, 0,null)); 
				lp.addConstraint(new LinearSmallerThanEqualsConstraint(constraint, 0,null));
			}
		}
		
		for(int k=0;k<numR;k++){	
			double[] constraint = new double[numS];
			for(int i=0;i<numS;i++){	
				constraint[i] = matrix[k][i] ;				
			}
			Reaction r = m.getReaction(k);
			boolean reversible = r.getReversible();
			if(reversible){
				lp.addConstraint(new LinearBiggerThanEqualsConstraint(constraint, -1000,null)); 
				lp.addConstraint(new LinearSmallerThanEqualsConstraint(constraint, 1000,null));
			}
			else{
				lp.addConstraint(new LinearBiggerThanEqualsConstraint(constraint, 0,null)); 
				lp.addConstraint(new LinearSmallerThanEqualsConstraint(constraint, 1000,null));
			}	
		}
	
		lp.setMinProblem(true); 
		LinearProgramSolver solver  = SolverFactory.newDefault(); 
		double[] sol = solver.solve(lp);
		
		int c=0;
		for(int i=0;i<sol.length;i++){
			if(sol[i]!=0.0){
				c++;
			}
		}
		System.out.println(c+":"+numR+":"+sol.length);
		return null;
	}

	//build matrix with reaction and metabolites	
	public static double[][] matrixBuild(int numR,int numS, String[] met, Model m){
		
		//array with reactions and metabolites 
		double[][] rctMetArr = new double[m.getNumReactions()][m.getNumSpecies()];
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
							rctMetArr[i][k] = -(r.getReactant(t).getStoichiometry()); correct++;
						}
					}
				int z = r.getNumProducts();
					for(int t=0;t<z;t++){			//all products
						SpeciesReference s = r.getProduct(t);
						Species sp = s.getSpeciesInstance();
						String id = sp.getId();
						if(id == met[k]){
							rctMetArr[i][k] = r.getProduct(t).getStoichiometry(); correct++;
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

		return rctMetArr;
	}

}



package project;

import java.io.File;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Model;

public class MainClass {
	
	public static void main(String args[]){
		
		//load SBML etc.
			
			if(args.length>0){		
				try {
					SBMLLoad.loadSBML(args[0]);
				} catch (XMLStreamException | IOException e) {
					e.printStackTrace();
				}
			}
			else{
				try {
					SBMLLoad.loadSBML("/home/guru/Downloads/S_aureus_iSB619.xml");
				} catch (XMLStreamException | IOException e) {
					e.printStackTrace();
				}
			}
	
			int t = SBMLLoad.getNumR();
		//call FindFluxModules
			
			
			
			
			
			
			
		//more methods: e.g. pdf,graphviz,...
	
		
		
		System.out.println("finish");
	
	}
}






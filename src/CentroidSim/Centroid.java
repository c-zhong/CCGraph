package CentroidSim;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;

import CentroidSim.Subgraph.SubgraphType;
import Filtering.*;

public class Centroid {

	
	public static MDVector getIsSubCentroid(Subgraph issub_g, double threshold){
		MDVector centroid = new MDVector();
		//TODO: get the centroid of issub_g
		LinkedList<Con_Filtering> qualified_nodes = issub_g.filterByDepth(threshold);
		LinkedList<Con_Filtering> genHyponodes = issub_g.base.getGenHypoNodes(); 
		
		
		for(String key : centroid.mdvector.keySet()){
			Con_Intervals mergedIntervals = new Con_Intervals();
			for(Con_Filtering node : qualified_nodes){
				Con_Intervals ivs = node.condition.conditions.get(key);
				if(ivs != null){
					mergedIntervals.mergeIntervals(ivs.intervals, "OR");
				}
			}
			
			
			//add bounded important isolated nodes
			for(Con_Filtering node : genHyponodes){
				Con_Intervals ivs = node.condition.conditions.get(key);
				if(ivs != null){
					for (Con_Interval ivv : ivs.intervals){ 
						if(ivv.isBounded()){
							mergedIntervals.mergeIntervals(ivv, "OR");
						}
					}
				}
			}
			
			
			centroid.mdvector.put(key, mergedIntervals);
		}
		return centroid;
	}
	
	
	public static MDVector getSubCentroid(Subgraph issub_g, double threshold){
		MDVector centroid = new MDVector();
		//TODO: get the centroid of issub_g
		
		LinkedList<Con_Filtering> qualified_nodes = issub_g.filterByDepth_Inverse(threshold);
		
		
		for(String key : centroid.mdvector.keySet()){
			Con_Intervals mergedIntervals = new Con_Intervals();
			for(Con_Filtering node : qualified_nodes){
				Con_Intervals ivs = node.condition.conditions.get(key);
				if(ivs != null){
					mergedIntervals.mergeIntervals(ivs.intervals, "OR");
				}
			}
			
		
			
			centroid.mdvector.put(key, mergedIntervals);
		}
		return centroid;
	}
	
	public static MDVector getIsComCentroid(Subgraph iscom_g, double threshold){
		MDVector centroid = new MDVector();
		//TODO: get the centroid of iscom_g
		for(String key : centroid.mdvector.keySet()){
			Con_Intervals mergedIntervals = new Con_Intervals();
			for(Con_Filtering node : iscom_g.subgraph.vertexSet()){
				Con_Intervals ivs = node.condition.conditions.get(key);
				if(ivs != null && ivs.isBounded(threshold)){
					mergedIntervals.mergeIntervals(ivs.intervals, "OR");
				}
			}
			centroid.mdvector.put(key, mergedIntervals);
		}
		return centroid;
	}
	
	public static MDVector getIsEqlCentroid(Subgraph iseql_g){
		MDVector centroid = new MDVector();
		//TODO: get the centroid of iseql_g
		for(Con_Filtering node : iseql_g.subgraph.vertexSet()){
			if(node != null){
				centroid = new MDVector(node.condition);
				return centroid;
			}
		}

		return centroid;
	}
	
	
	public static ArrayList<Double> centroidSim_vector(
			ArrayList<MDVector> mdlist1,
			ArrayList<MDVector> mdlist2, 
			double overlap_value
			//0: w_isSub, 
			//1: w_isCom, 
			//2: w_isEql, 
			//3: w_Sum
			){
		ArrayList<Double> result = new ArrayList<Double>();
		for (int i = 0; i < mdlist1.size() && i < mdlist2.size(); i++){
			double itemSim = 0;
//			System.out.println("list index: "+i);
			MDVector v1 = mdlist1.get(i);
			MDVector v2 = mdlist2.get(i);
			if(v1 != null && v2 != null){
				itemSim += MDVector.similarity(v1, v2, overlap_value);
			}
			System.out.println("["+i+"]: "+ itemSim);
			result.add(itemSim);
		}
		return result;	
	}
	
	
	public static double centroidSim_weighted(
			ArrayList<MDVector> mdlist1,
			ArrayList<MDVector> mdlist2,
			double overlap_value,
			double[] weights 
			//0: w_isSub, 
			//1: w_isCom, 
			//2: w_isEql, 
			//3: w_Sum
			){
		double result = 0;
		double counter = 0;
		for (int i = 0; i < mdlist1.size() && i < mdlist2.size(); i++){
			double itemSim = 0;
			System.out.println("list index: "+i);
			MDVector v1 = mdlist1.get(i);
			MDVector v2 = mdlist2.get(i);
			if(v1 != null && v2 != null){
				itemSim += MDVector.similarity(v1, v2, overlap_value);
			}
//			System.out.println("list ["+i+"]: "+ itemSim);
			result += weights[i] * itemSim;
			counter += weights[i];
		}
		return result/counter;	
	}
	
	
	public static double centroidSim_weighted(
			ArrayList<Double> sim,
			double[] weights 
			//0: w_isSub, 
			//1: w_isCom, 
			//2: w_isEql, 
			//3: w_Sum
			){
		double result = 0;
		double count = 0;
		for (int i = 0; i < sim.size(); i++){
			double itemSim = sim.get(i);
			
			if(itemSim > 0){
				
				result += weights[i]*itemSim;
				count += weights[i];
				
			}
			System.out.println("list ["+i+"]: "+ itemSim);
		}
		if(count > 0){
			return result/count;
		}else {
			return 0;
		}
	}
	

	
		
		

}

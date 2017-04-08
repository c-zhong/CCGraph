package CentroidSim;


import java.util.LinkedList;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DirectedWeightedSubgraph;

import Filtering.Con_Filtering;
import Filtering.Con_Graph;
import Filtering.Con_Graph.MyEdge;

public class Subgraph {
	String original_graph_id = "";
	DirectedWeightedSubgraph<Con_Filtering, MyEdge> subgraph = null;
	Con_Graph base =  null;

	public enum SubgraphType{
		ISSUM, 
		ISCOM,
		ISEQL,
		SUM,
		UNKNOWN
	}
	
	
	SubgraphType graph_type = SubgraphType.UNKNOWN; //1: issub 2:iscom, 3:
	
	public Subgraph(Con_Graph base, SubgraphType type){
		if(type == SubgraphType.ISSUM){
			setisSubGraph(base);
		}else if(type == SubgraphType.ISCOM){
			setisComGraph(base);
		}else if(type == SubgraphType.ISEQL){
			setisEqlGraph(base);
		}else if(type ==SubgraphType.SUM){
			setSumGraph(base);
		}
	}
	
    //return the isSub subgraph of g
	public void setisSubGraph (Con_Graph graph){
		original_graph_id = graph.getGraphID();
		base = graph;
		graph_type = SubgraphType.ISSUM;
		//TODO get isSub subgraph of g 
		subgraph = graph.getSubgraph(2);	
		//subgraph = graph.getisSubSubgraph();
		
	}
	
	//return the isCom subgraph of g
	public void setisComGraph (Con_Graph graph){
		base = graph;
		original_graph_id = graph.getGraphID();
		graph_type = SubgraphType.ISCOM;
		Con_Graph isCom = null;
		//TODO get isCom subgraph of g
		subgraph = graph.getSubgraph(3);
		
			
	}
	
	//return the isEql subgraph of g
	public void setisEqlGraph (Con_Graph graph){
		base = graph;
		original_graph_id = graph.getGraphID();
		Con_Graph isEql = null;
		graph_type = SubgraphType.ISEQL;

		//TODO get isEql subgraph of g
		subgraph = graph.getSubgraph(1);
		
		
	}
	
	

		
		
	//return the sum subgraph of g
	public void setSumGraph (Con_Graph graph){
		base = graph;
		original_graph_id = graph.getGraphID();
		Con_Graph Sum = null;
		graph_type = SubgraphType.SUM;

		//TODO get Sum subgraph of g
		subgraph = graph.getSubgraph(4);
		
	}
	
		
	public LinkedList<Con_Filtering> filterByDepth(double threshold){
		//contain a set of vertices
		LinkedList<Con_Filtering> result = null;
		
		if (subgraph == null){
			return result;
		}
		

		if(graph_type == SubgraphType.ISSUM){
			result = new LinkedList<Con_Filtering>();
			LinkedList<Con_Filtering> roots = new LinkedList<Con_Filtering>();
			LinkedList<Con_Filtering> leaves = new LinkedList<Con_Filtering>();
			//find all the root
			for(Con_Filtering node : subgraph.vertexSet()){
				if(subgraph.outDegreeOf(node) == 0){
					roots.add(node);
				}
				if(node.generate_hypo){
					result.add(node);
				}
			}
			//find all the leaves
			for(Con_Filtering node : subgraph.vertexSet()){				
				if(subgraph.inDegreeOf(node) == 0){
					leaves.add(node);
				}
			}
			
			
			for(Con_Filtering root : roots){
				for(Con_Filtering leaf : leaves){
					DijkstraShortestPath<Con_Filtering, MyEdge> path = 
	        				new DijkstraShortestPath<Con_Filtering, MyEdge>
	        					(subgraph, leaf, root);	
					double length = path.getPathLength();
					List<MyEdge> edge_list = path.getPathEdgeList();
					if (edge_list != null){
						double minlen = (length) * threshold;
			        		for(MyEdge pathedge : edge_list){
			        			Con_Filtering source = subgraph.getEdgeSource(pathedge);
			        			if(!result.contains(source)){
			        				DijkstraShortestPath<Con_Filtering, MyEdge> path_source = 
					        				new DijkstraShortestPath<Con_Filtering, MyEdge>
					        					(subgraph, source, root);
			        				if(path_source.getPathLength() >= minlen){
				        				result.add(source);
				        			}
			        			}
			        			
			        			Con_Filtering target = subgraph.getEdgeTarget(pathedge);
			        			if(!result.contains(target)){
			        				DijkstraShortestPath<Con_Filtering, MyEdge> path_target = 
					        				new DijkstraShortestPath<Con_Filtering, MyEdge>
					        					(subgraph, target, root);
			        				if(path_target.getPathLength() >= minlen){
				        				result.add(target);
				        			}
			        			}	
			        		}
					}
				}	
			}//root, leaf		 
		}//if graph = sum
		return result;	
	}
	
	
	
	public LinkedList<Con_Filtering> filterByDepth_Inverse(double threshold){
		//contain a set of vertices
		LinkedList<Con_Filtering> result = null;
		
		if (subgraph == null){
			return result;
		}
		

		if(graph_type == SubgraphType.SUM){
			result = new LinkedList<Con_Filtering>();
			LinkedList<Con_Filtering> roots = new LinkedList<Con_Filtering>();
			LinkedList<Con_Filtering> leaves = new LinkedList<Con_Filtering>();
			//find all the root
			for(Con_Filtering node : subgraph.vertexSet()){
				if(subgraph.inDegreeOf(node) == 0){
					roots.add(node);
				}
			}
			//find all the leaves
			for(Con_Filtering node : subgraph.vertexSet()){				
				if(subgraph.outDegreeOf(node) == 0){
					leaves.add(node);
				}
			}
			
			
			for(Con_Filtering root : roots){
				for(Con_Filtering leaf : leaves){
					DijkstraShortestPath<Con_Filtering, MyEdge> path = 
	        				new DijkstraShortestPath<Con_Filtering, MyEdge>
	        					(subgraph, root, leaf);	
					double length = path.getPathLength();
					if (path != null && length > 0){
						double minlen = length * threshold;
						if(path.getPathEdgeList() == null){
							continue;
						}
			        		for(MyEdge pathedge : path.getPathEdgeList()){
			        			Con_Filtering source = subgraph.getEdgeSource(pathedge);
			        			
			        			if(!result.contains(source)){
			        				DijkstraShortestPath<Con_Filtering, MyEdge> path_source = 
					        				new DijkstraShortestPath<Con_Filtering, MyEdge>
					        					(subgraph, root, source);
			        				if(path_source.getPathLength() <= minlen){
				        				result.add(source);
				        			}
			        			}
			        			
			        			Con_Filtering target = subgraph.getEdgeTarget(pathedge);
			        			if(!result.contains(target)){
			        				DijkstraShortestPath<Con_Filtering, MyEdge> path_target = 
					        				new DijkstraShortestPath<Con_Filtering, MyEdge>
					        					(subgraph, root, target);
			        				if(path_target.getPathLength() <= minlen){
				        				result.add(target);
				        			}
			        			}	
			        		}
					}
				}	
			}//root, leaf		 
		}//if graph = sum
		return result;	
	}
	
	
	

}

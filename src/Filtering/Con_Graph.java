package Filtering;
/*
 * JGraphT
 * 
 * Visualize a graph: 
 * http://stackoverflow.com/questions/24517434/jgraph-drawing-a-simpleweightedgraph-on-a-jpanel/24519791#24519791
 * 
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.jgrapht.graph.*;



public class Con_Graph {

	
	/*  1: equal to  
	 * 	2: is subsumed by 
	 *  3: is complementary to 
	 *  4: subsumes
	 *  7: is exact complementary
	 *  -1: other
	 */
	
	public static class MyEdge extends DefaultWeightedEdge {
		@Override
		public String toString() {
			//return String.valueOf(getWeight());
			return "";
		}

		public double weight() {
			return getWeight();
		}

	}

	public ListenableDirectedWeightedGraph<Con_Filtering, MyEdge> graph = null;

	public String graphID = "";
	

	
	public Con_Graph(String id, LinkedList<Con_Filtering> filters){
		graphID = id;
		graph = new  ListenableDirectedWeightedGraph<Con_Filtering, MyEdge>(MyEdge.class);

		int node_num =0; 

		//construct graph (vertices and edges)
		for(int i=0; i<filters.size(); i++){
			Con_Filtering filter1 = filters.get(i);
			graph.addVertex(filter1);
			node_num ++;
			for(int j=node_num-1-1; j>=0; j--){
				Con_Filtering filter2 = filters.get(j);
				if(!graph.vertexSet().contains(filter2)){
					graph.addVertex(filter2);
				}
				int edge_type = Con_Filtering.relationship(filter1, filter2);
				//add edge
				if(edge_type != -1){
					MyEdge e = graph.addEdge(filter1, filter2);
					graph.setEdgeWeight(e, edge_type);
				}
			}
		}

	}


	public Con_Graph(String id, LinkedList<Con_Filtering> filters, int definition){
		graphID = id;

		if(definition == -1){ //is undirected
			// is equal to (B <--> A)
			// is subsumed by (B --> A)
			// is complementary with (B <--> A)
			// corresponds to (B <--> A)
			// add edges (A --> B)

			graph = new  ListenableDirectedWeightedGraph<Con_Filtering, MyEdge>(MyEdge.class);

			int node_num =0; 

			//construct graph (vertices and edges)
			for(int i=0; i<filters.size(); i++){
				Con_Filtering filter1 = filters.get(i);
				graph.addVertex(filter1);
				node_num ++;
				for(int j=0; j<filters.size(); j++){
					if(j == i)
						continue;

					Con_Filtering filter2 = filters.get(j);

					if(!graph.vertexSet().contains(filter2)){
						graph.addVertex(filter2);
					}

					int edge_type = Con_Filtering.relationship(filter1, filter2);

					//add edge
					if(edge_type != -1){
						if(graph.containsEdge(filter2, filter1)){
							if(edge_type == 2){
								MyEdge e = graph.addEdge(filter1, filter2);
								graph.setEdgeWeight(e, edge_type);
							}
						}else{
							MyEdge e = graph.addEdge(filter1, filter2);
							graph.setEdgeWeight(e, edge_type);
						}

					}
				}
			}


		}


		if(definition == 0){ 
			// is complementary (B <--> A)
			// is equal (B <--> A)
			// corresponds (B <--> A)
			// is subsumed by (B --> A)


			graph = new  ListenableDirectedWeightedGraph<Con_Filtering, MyEdge>(MyEdge.class);


			//construct graph (vertices and edges)
			for(int i=0; i<filters.size(); i++){
				Con_Filtering filter1 = filters.get(i);
				graph.addVertex(filter1);

				for(int j=0; j<filters.size(); j++){
					if(j == i)
						continue;

					Con_Filtering filter2 = filters.get(j);

					if(!graph.vertexSet().contains(filter2)){
						graph.addVertex(filter2);
					}


					/*  1: equal to  
					 * 	2: is subsumed by 
					 *  3: is complementary to 
					 *  4: is linked to*/

					int edge_type = Con_Filtering.relationship(filter1, filter2);

					//add edge
					if(edge_type != -1){
						MyEdge e = graph.addEdge(filter1, filter2);
						graph.setEdgeWeight(e, edge_type);
					}


				}
			}


		}//if(definition == 0)
		else  if(definition == 2){ 
			// is complementary, happen-after (B --> A)
			// is equal, happen-after(B --> A)
			//is equal,  corresponds (B --> A)
			// is subsumed by (B --> A)


			graph = new  ListenableDirectedWeightedGraph<Con_Filtering, MyEdge>(MyEdge.class);


			//construct graph (vertices and edges)
			for(int i=0; i<filters.size(); i++){
				Con_Filtering filter1 = filters.get(i);
				graph.addVertex(filter1);

				for(int j=0; j<filters.size(); j++){
					if(j == i)
						continue;


					Con_Filtering filter2 = filters.get(j);

					if(!graph.vertexSet().contains(filter2)){
						graph.addVertex(filter2);
					}


					/*  1: equal to  
					 * 	2: is subsumed by 
					 *  3: is complementary to 
					 *  4: is linked to*/

					int edge_type = Con_Filtering.relationship(filter1, filter2);

					//add edge
					if(edge_type != -1  && filter1.operation_index > filter2.operation_index){
						MyEdge e = graph.addEdge(filter1, filter2);
						graph.setEdgeWeight(e, edge_type);
					}

				}
			}


		}//if(defintion == 2)


		else  if(definition == 3){ 
			//ignore datasource
			// is complementary, happen-after (B --> A)
			// is equal, happen-after(B --> A)
			//is equal,  corresponds (B --> A)
			// is subsumed by (B --> A)


			graph = new  ListenableDirectedWeightedGraph<Con_Filtering, MyEdge>(MyEdge.class);

			//construct graph (vertices and edges)
			for(int i=0; i<filters.size(); i++){
				Con_Filtering filter1 = filters.get(i);
				graph.addVertex(filter1);

				for(int j=0; j<filters.size(); j++){
					if(j == i)
						continue;

					Con_Filtering filter2 = filters.get(j);

					if(!graph.vertexSet().contains(filter2)){
						graph.addVertex(filter2);
					}

					if(filter1.operation_index > filter2.operation_index){
						/*  1: equal to  
						 * 	2: is subsumed by 
						 *  3: is complementary to 
						 *  4: subsumes
						 *  7: is exact complementary
						 *  -1: other
						 */
						int edge_type = Con_Filtering.relationship_ignore_datasource(filter1, filter2);
						//add edge
						if(edge_type != -1){
							MyEdge e = graph.addEdge(filter1, filter2);
							graph.setEdgeWeight(e, edge_type);
						}
					}

				}
			}


		}//if(defintion == 3)
	}


	public String getGraphID (){
		return graphID;
	}

	/*  1: equal to  
	 * 	2: is subsumed by 
	 *  3: is complementary to 
	 *  4: is linked to*/

	public int countEdges(int weight){
		int count =0;

		for (MyEdge edge : graph.edgeSet()) {
			if(edge.weight() == weight)
				count ++;
		}

		return count;

	}
	
	
	
	
	
	public DirectedWeightedSubgraph<Con_Filtering, MyEdge> getSubgraph(int edgetype){
		/*  1: equal to  
		 * 	2: is subsumed by 
		 *  3: is complementary to 
		 *  4: subsumes
		 *  7: is exact complementary
		 *  -1: other
		 */

		Set<MyEdge> edgeSet = new HashSet<MyEdge>();
		Set<Con_Filtering> vSet = new HashSet<Con_Filtering>();
		
		for(MyEdge edge : graph.edgeSet()){
			if(edge.weight() ==  edgetype){
				vSet.add(graph.getEdgeSource(edge));
				vSet.add(graph.getEdgeTarget(edge));
				edgeSet.add(edge);
			}
		}
		
		DirectedWeightedSubgraph<Con_Filtering, MyEdge> subgraph = new DirectedWeightedSubgraph<Con_Filtering, MyEdge>(graph,
                vSet, edgeSet);
		return subgraph;			

	}
	
	
	public LinkedList<Con_Filtering> getGenHypoNodes(){
	

		LinkedList<Con_Filtering> vSet = new LinkedList<Con_Filtering>();
		

	
		for(Con_Filtering node: graph.vertexSet()){
			if(node.generate_hypo && !vSet.contains(node)){
				vSet.add(node);
			}
		}
	

		return vSet;			

	}
	




}






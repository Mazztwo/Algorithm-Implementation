import java.util.*;
import java.io.*;

// Assignment 4 for Algorithms

public class Airline
{
	public static void main(String args[])throws IOException
	{	
		Scanner keyboard = new Scanner(System.in); 			// Initialize scanner object to read user keyboard input
		String fileName;									// Name of file with flight data as a string 
		Scanner input;										// Will be used to read from the user file
		String cities[];									// Will hold the names of the cities read from the file
		List[] adjList;										// Adjacency list representation of our graph. An array of lists
		String line[];										// Holds the line in the file tokenized at whitespace
		int userMenuChoice = 0;								// Holds the user's menu choice
		EdgeWeightedGraph G;								// Holds edge weighted graph for MST generation
		Edge graphEdge;										// Holds edge object to be initialized when user file is read
		LazyPrimMST mst;									// Holds the minimum spanning tree
		String sourceCity;									// Holds user's starting city for shortest path algorithms
		String destinationCity;								// Holds user's destination city for shortest path algorithms					
		int indexOfSource;									// Holds the number representation of the source city
		int indexOfDestination;								// Holds the number representation of the destination city
		EdgeWeightedDigraph N;								// Holds edge weighted digraph for shortest path based on total miles
		DirectedEdge digraphEdge;							// Holds edge for digraph edge
		DijkstraSP MSPshortestPathDistance;					// Holds MSP for the shortest path based distance
		DijkstraSP MSPshortestPathCost;						// Holds MSP for the shortest path based on price
		EdgeWeightedDigraph I;								// Holds edge weighted digraph for shortest path based on cost
		Graph P;											// Holds graph for BFS algorithm for shortest hops
		BreadthFirstPaths BFSshortestHops;					// Holds structure with shortest hops paths 
		double userAmount;									// Holds the amount the user wants paths to be less than or equal to
		int userDistance;									// Holds user distance between cities they want to add
		double userCost;										// Holds user cost between cities they want to add	
		
		
		
		
		// Get file name from user
		System.out.println("Welcome to the Airline program. Please enter a filename:   ");		
		fileName = keyboard.nextLine();
		File file = new File(fileName);
		
		while(!file.exists())
		{
			System.out.println("INPUT ERROR: File name entered does not exist. Please enter file that exists:   ");
			fileName = keyboard.nextLine();
			file = new File(fileName);
		}
		
		// Open the user specified file to read from it
		input = new Scanner(file);
		
		// The first line of the file will be the size of our adjacency list and list of cities
		int size = Integer.parseInt(input.nextLine());
		cities = new String[size];
		adjList = new List[size];
		G = new EdgeWeightedGraph(size);
		N = new EdgeWeightedDigraph(size);
		I = new EdgeWeightedDigraph(size);
		P = new Graph(size);
		
		// We must fill our adjacency list with empty lists in order to be able to add
		// new edges to the lists when we read them in from the user file.
		// Also, we now fill our cities array with the names of the cities from the file
		for(int i = 0; i < size; i = i + 1)
		{
			adjList[i] = new List();
			cities[i] = input.nextLine();
		}

		//Read the file and make the appropriate adjacency list representation of the graph
		while(input.hasNext())
		{
			line= (input.nextLine()).split(" ");	// Split each line into parts
			
			// Each edge in our graph will be represented twice, once for each vertex in that edge.
			adjList[Integer.parseInt(line[0]) - 1].add(Integer.parseInt(line[1]) - 1, 		// Add city's number-1 to the list
													   Integer.parseInt(line[2]), 			// Add distance to that city to list
													   Double.parseDouble(line[3]) );		// Add cost to go to that city to list
			
			adjList[Integer.parseInt(line[1]) - 1].add(Integer.parseInt(line[0]) - 1, 		// Add city's number-1 to the list
					   								   Integer.parseInt(line[2]), 			// Add distance to that city to list
					   								   Double.parseDouble(line[3]) );		// Add cost to go to that city to list	
			
			// Create new edge and add it to the EdgeWeightedGraph
			graphEdge = new Edge(Integer.parseInt(line[0]) - 1,Integer.parseInt(line[1]) - 1, (double) Integer.parseInt(line[2]));
			G.addEdge(graphEdge);
				
			// Create new edge for each side of path and add them to EdgeWeightedDigraph for the MSP based on price
			digraphEdge = new DirectedEdge(Integer.parseInt(line[0]) - 1,Integer.parseInt(line[1]) - 1, (double) Integer.parseInt(line[2]));
			N.addEdge(digraphEdge);
			digraphEdge = new DirectedEdge(Integer.parseInt(line[1]) - 1,Integer.parseInt(line[0]) - 1, (double) Integer.parseInt(line[2]));
			N.addEdge(digraphEdge);
			
			// Create new edge for each side of path and add them to EdgeWeightedDigraph for the MSP based on price
			digraphEdge = new DirectedEdge(Integer.parseInt(line[0]) - 1,Integer.parseInt(line[1]) - 1, Double.parseDouble(line[3]));
			I.addEdge(digraphEdge);
			digraphEdge = new DirectedEdge(Integer.parseInt(line[1]) - 1,Integer.parseInt(line[0]) - 1, Double.parseDouble(line[3]));
			I.addEdge(digraphEdge);
			
			// Add each edge to graph for possible BFS traversal for shortest path based on hops
			P.addEdge(Integer.parseInt(line[0]) - 1,Integer.parseInt(line[1]) - 1);
			P.addEdge(Integer.parseInt(line[1]) - 1,Integer.parseInt(line[0]) - 1);
			
		}
		
		while(userMenuChoice != 9)
		{
			displayMenu();
			userMenuChoice = keyboard.nextInt();
			
			switch( userMenuChoice )
			{
				case 1: 	// Display list of direct routes
				{
					displayRoutes(adjList,cities);
					break;
				}
				case 2: 	// Display minimum spanning tree of service routes in terms of distances
				{
					System.out.println("\nMinimum Spanning Tree:\n*********************");
					
					mst = new LazyPrimMST(G);
				
					for(Edge e :mst.edges())
					{	
						System.out.println(cities[e.either()] + " to " + cities[e.other(e.either())] + " : Distance = " + e.weight() );
					}
				
					break;
				}
				case 3: 	// Display shortest path based on total miles
				{
					keyboard.nextLine(); 		// Consume new line character
					
					// First we grab the source and destination cities
					System.out.println("Please enter a source city:  ");
					sourceCity = keyboard.nextLine();
					indexOfSource = Arrays.asList(cities).indexOf(sourceCity);
					if(indexOfSource < 0)
					{
						System.out.println("The city you entered does not exist in the data. Try again.");
						break;
					}
					
					System.out.println("Please enter a destination city:  ");
					destinationCity = keyboard.nextLine();
					indexOfDestination = Arrays.asList(cities).indexOf(destinationCity);
					if(indexOfDestination < 0)
					{
						System.out.println("The city you entered does not exist in the data. Try again.");
						break;
					}
					
					// We generate the MSP tree based on the user given starting city
					MSPshortestPathDistance = new DijkstraSP(N,indexOfSource);
					
					// If a shortest path exists, display it to the user, else indicate that there is no path.
					if (MSPshortestPathDistance.hasPathTo(indexOfDestination)) 
					{
						System.out.println("\nSHORTEST DISTANCE PATH from " + sourceCity + " to " + destinationCity);
						System.out.println("*********************************************************************");
						System.out.println("Shortest distance from " + sourceCity+ " to " + destinationCity + " is " + MSPshortestPathDistance.distTo(indexOfDestination));
						System.out.println("Path with edges (in reverse order):");
						
						for (DirectedEdge e : MSPshortestPathDistance.pathTo(indexOfDestination)) 
		                {
							System.out.print(cities[e.to()] + "<-" + cities[e.from()] + " " + e.weight + "   ");
		                }
		                
						System.out.println();
		            }
		            else 
		            {
		            	System.out.println("There is no path from " + sourceCity + " to " + destinationCity);
		            }
					
					break;
				}
				case 4: 	// Display shortest path based on price			
				{
					keyboard.nextLine(); 		// Consume new line character
					
					// First we grab the source and destination cities
					System.out.println("Please enter a source city:  ");
					sourceCity = keyboard.nextLine();
					indexOfSource = Arrays.asList(cities).indexOf(sourceCity);
					if(indexOfSource < 0)
					{
						System.out.println("The city you entered does not exist in the data. Try again.");
						break;
					}
					
					System.out.println("Please enter a destination city:  ");
					destinationCity = keyboard.nextLine();
					indexOfDestination = Arrays.asList(cities).indexOf(destinationCity);
					if(indexOfDestination < 0)
					{
						System.out.println("The city you entered does not exist in the data. Try again.");
						break;
					}
					
					// We generate the MSP tree based on the user given starting city
					MSPshortestPathCost = new DijkstraSP(I,indexOfSource);
					
					// If a shortest path exists, display it to the user, else indicate that there is no path.
					if (MSPshortestPathCost.hasPathTo(indexOfDestination)) 
					{
						System.out.println("\nLOWEST COST PATH from " + sourceCity + " to " + destinationCity);
						System.out.println("*********************************************************************");
						System.out.println("Lowest cost path from " + sourceCity+ " to " + destinationCity + " is " + MSPshortestPathCost.distTo(indexOfDestination));
						System.out.println("Path with edges (in reverse order):");
						
						for (DirectedEdge e : MSPshortestPathCost.pathTo(indexOfDestination)) 
		                {
							System.out.print(cities[e.to()] + "<-" + cities[e.from()] + " " + e.weight + "   ");
		                }
		                
						System.out.println();
		            }
		            else 
		            {
		                System.out.println("There is no path from " + sourceCity + " to " + destinationCity);
		            }
					
					
					break;
				}
				case 5: 	// Display shortest path based on number of hops
				{
					keyboard.nextLine(); 		// Consume new line character
					
					// First we grab the source and destination cities
					System.out.println("Please enter a source city:  ");
					sourceCity = keyboard.nextLine();
					indexOfSource = Arrays.asList(cities).indexOf(sourceCity);
					if(indexOfSource < 0)
					{
						System.out.println("The city you entered does not exist in the data. Try again.");
						break;
					}
					
					System.out.println("Please enter a destination city:  ");
					destinationCity = keyboard.nextLine();
					indexOfDestination = Arrays.asList(cities).indexOf(destinationCity);
					if(indexOfDestination < 0)
					{
						System.out.println("The city you entered does not exist in the data. Try again.");
						break;
					}
					
					BFSshortestHops = new BreadthFirstPaths(P,indexOfSource);
					
					if(BFSshortestHops.hasPathTo(indexOfDestination))
					{
						
						System.out.println("\nFEWEST HOPS from " + sourceCity + " to " + destinationCity);
						System.out.println("*********************************************************************");
						System.out.println("Fewest hops from " + sourceCity+ " to " + destinationCity + " is " + BFSshortestHops.distTo(indexOfDestination));
						System.out.println("Path (in reverse order):");
						
						for (int e : BFSshortestHops.pathTo(indexOfDestination)) 
		                {
							System.out.print(cities[e] + " ");
		                }
		                
						System.out.println();
						
					}
					else
					{
						 System.out.println("There is no path from " + sourceCity + " to " + destinationCity);
					}
			
					break;
				}
				case 6: 	// Display paths that cost less than or equal to an amount
				{
					
					keyboard.nextLine(); 		// Consume new line character
					
					// First we grab the amount the user specifies all paths are less than or equal to
					System.out.println("Please enter a cost to see all paths less than or equal to this cost:  ");
					userAmount= keyboard.nextDouble();
					
					System.out.println("\nALL PATHS OF COST " + userAmount + " OR LESS");
					System.out.println("Note: Paths are duplicated, once from each end city's point of view.");
					System.out.println("*********************************************************************");
					System.out.println("List of paths of at most " + userAmount + " in cost:");
					
					// This structure below will iterate through every city, using a Dijkstra Shortest Path tree.
					// The first two for loops represent the changing source and destination cities. If they would be the same
					// we simply increment our destination city by one and keep going. If a path exists from source to destination
					// and this path costs less than or equal to the user specified amount, then we print it out. This will repeat
					// for each source/destination combination possible on the graph.
					for(indexOfSource = 0; indexOfSource < size; indexOfSource++)
					{
						// We generate the MSP tree
						MSPshortestPathCost = new DijkstraSP(I,indexOfSource);
						
						for(indexOfDestination = 0; indexOfDestination < size; indexOfDestination++)
						{
							if(indexOfSource == indexOfDestination) indexOfDestination++;
							if(indexOfDestination == size) break;			// If we've reached our size, then we are done
							
							// If a shortest path exists, display it to the user if it costs less than or equal to their amount
							if (MSPshortestPathCost.hasPathTo(indexOfDestination)) 
							{
								if(MSPshortestPathCost.distTo(indexOfDestination) <= userAmount)
								{
									System.out.println("Cost: " + MSPshortestPathCost.distTo(indexOfDestination) + " Path (reversed): ");
									
									for (DirectedEdge e : MSPshortestPathCost.pathTo(indexOfDestination)) 
					                {
										System.out.print(cities[e.to()] + "<-" + cities[e.from()] + " " + e.weight + "   ");
					                }
					                
									System.out.println("\n");
								}
				            }
						}
					}
					
					break;
				}
				case 7:  	// Add a new route to the schedule
				{
					keyboard.nextLine(); 		// Consume new line character
					
					// First we grab the source and destination cities for te new path
					System.out.println("Please enter a source city of the new route you want to add:  ");
					sourceCity = keyboard.nextLine();
					indexOfSource = Arrays.asList(cities).indexOf(sourceCity);
					if(indexOfSource < 0)
					{
						System.out.println("The city you entered does not exist in the data. Try again.");
						break;
					}
					
					System.out.println("Please enter a destination city of the new route you want to add:  ");
					destinationCity = keyboard.nextLine();
					indexOfDestination = Arrays.asList(cities).indexOf(destinationCity);
					if(indexOfDestination < 0)
					{
						System.out.println("The city you entered does not exist in the data. Try again.");
						break;
					}
					
					// Then we grab the distance between the new cities and the price for the route
					System.out.println("Please enter the distance between the two cities you just entered:   ");
					userDistance = keyboard.nextInt();
					
					System.out.println("Please enter the cost for the route you want to add:   ");
					userCost = keyboard.nextDouble();
					
					// Now we open the file as a print writer object and append the new path to it
					FileWriter fwriter = new FileWriter(fileName,true);
					PrintWriter outputFile = new PrintWriter(fwriter);
					outputFile.println((indexOfSource+1) + " " + (indexOfDestination+1) + " " + userDistance + " " + userCost);
					outputFile.close();
					
					System.out.println("The new route has been added to the file.");
					
					// We now close and reopen the scanner object containing the file and re-make our graphs
					input.close();
					
					// Open the user specified file to read from it
					input = new Scanner(file);
					
					// The first line of the file will be the size of our adjacency list and list of cities
				    size = Integer.parseInt(input.nextLine());
					cities = new String[size];
					adjList = new List[size];
					G = new EdgeWeightedGraph(size);
					N = new EdgeWeightedDigraph(size);
					I = new EdgeWeightedDigraph(size);
					P = new Graph(size);
					
					// We must fill our adjacency list with empty lists in order to be able to add
					// new edges to the lists when we read them in from the user file.
					// Also, we now fill our cities array with the names of the cities from the file
					for(int i = 0; i < size; i = i + 1)
					{
						adjList[i] = new List();
						cities[i] = input.nextLine();
					}

					//Read the file and make the appropriate adjacency list representation of the graph
					while(input.hasNext())
					{
						line= (input.nextLine()).split(" ");	// Split each line into parts
						
						// Each edge in our graph will be represented twice, once for each vertex in that edge.
						adjList[Integer.parseInt(line[0]) - 1].add(Integer.parseInt(line[1]) - 1, 		// Add city's number-1 to the list
																   Integer.parseInt(line[2]), 			// Add distance to that city to list
																   Double.parseDouble(line[3]) );		// Add cost to go to that city to list
						
						adjList[Integer.parseInt(line[1]) - 1].add(Integer.parseInt(line[0]) - 1, 		// Add city's number-1 to the list
								   								   Integer.parseInt(line[2]), 			// Add distance to that city to list
								   								   Double.parseDouble(line[3]) );		// Add cost to go to that city to list	
						
						// Create new edge and add it to the EdgeWeightedGraph
						graphEdge = new Edge(Integer.parseInt(line[0]) - 1,Integer.parseInt(line[1]) - 1, (double) Integer.parseInt(line[2]));
						G.addEdge(graphEdge);
							
						// Create new edge for each side of path and add them to EdgeWeightedDigraph for the MSP based on price
						digraphEdge = new DirectedEdge(Integer.parseInt(line[0]) - 1,Integer.parseInt(line[1]) - 1, (double) Integer.parseInt(line[2]));
						N.addEdge(digraphEdge);
						digraphEdge = new DirectedEdge(Integer.parseInt(line[1]) - 1,Integer.parseInt(line[0]) - 1, (double) Integer.parseInt(line[2]));
						N.addEdge(digraphEdge);
						
						// Create new edge for each side of path and add them to EdgeWeightedDigraph for the MSP based on price
						digraphEdge = new DirectedEdge(Integer.parseInt(line[0]) - 1,Integer.parseInt(line[1]) - 1, Double.parseDouble(line[3]));
						I.addEdge(digraphEdge);
						digraphEdge = new DirectedEdge(Integer.parseInt(line[1]) - 1,Integer.parseInt(line[0]) - 1, Double.parseDouble(line[3]));
						I.addEdge(digraphEdge);
						
						// Add each edge to graph for possible BFS traversal for shortest path based on hops
						P.addEdge(Integer.parseInt(line[0]) - 1,Integer.parseInt(line[1]) - 1);
						P.addEdge(Integer.parseInt(line[1]) - 1,Integer.parseInt(line[0]) - 1);
						
					}
					
					
					
					break;
				}
				case 8: 	// Remove a route from the schedule
				{
					keyboard.nextLine(); 		// Consume new line character
					
					// First we grab the source and destination cities for te new path
					System.out.println("Please enter a source city of the route you want to remove:  ");
					sourceCity = keyboard.nextLine();
					indexOfSource = Arrays.asList(cities).indexOf(sourceCity);
					if(indexOfSource < 0)
					{
						System.out.println("The city you entered does not exist in the data. Try again.");
						break;
					}
					
					System.out.println("Please enter a destination city of the route you want to remove:  ");
					destinationCity = keyboard.nextLine();
					indexOfDestination = Arrays.asList(cities).indexOf(destinationCity);
					if(indexOfDestination < 0)
					{
						System.out.println("The city you entered does not exist in the data. Try again.");
						break;
					}



					// I could not figure out how to change a specific line in the file without having to make a new file ... 



					
					break;
				}
				default: 	// Save schedule and quit program
				{
					// Do nothing, this just exits the program.
					// If the user added/removed a path, that change has already been saved.
					break;
				}
			}
		}
		
		// Close scanner objects
		keyboard.close();
		input.close();
	}
	
	// A function that displays the main menu
	public static void displayMenu()
	{
		// Next we must design a menu that allows for the different choices that are specified by the assignment guidelines
		System.out.println("\nPlease select an option:\n************************"
												   + "\n1)Display list of direct routes"
											       + "\n2)Display minimum spanning tree of service routes"
												   + "\n3)Display shortest path based on total miles"
											       + "\n4)Display shortest path based on price"
										     	   + "\n5)Display shortest path based on number of hops"
												   + "\n6)Display paths that cost less than or equal to an amount"
												   + "\n7)Add a new route to the schedule"
												   + "\n8)Remove a route from the schedule"
												   + "\n9)Save schedule and quit program"
												   + "\n************************");
	}
	
	// Function to display the entire list of direct routes
	public static void displayRoutes(List[] adjList,String[] cities)
	{		
		System.out.println("\nEntire list of direct routes:\n****************************");
		
		for(int i = 0; i < adjList.length; i = i + 1)
		{
			Node temp = adjList[i].getFirst();
			String city = cities[i];
			
			while(temp != null)
			{
				System.out.println(city + " to " 
									    + cities[temp.getCity()] 
										+ ": Distance = " + temp.getDistance() 
										+ " Cost = " + temp.getPrice());
				
				temp = temp.getNext();
			}
			
		}
	}
	
	// A List class in order to better organize the adjacency list 
	public static class List
	{
		Node first;  		// A pointer to the first Node in the list
		
		public List()
		{
			first = null;
		}
		
		public void add(int city, int distance, double price)
		{
			Node temp = new Node();
			temp.setCity(city);
			temp.setDistance(distance);
			temp.setPrice(price);
			
			temp.next = first;
			first = temp;
			
		}	
		
		public Node getFirst()
		{
			return first;
		}
		
	}
	
	// Inner class to represent nodes
	public static class Node
	{
		private Node next;			// Points to next node in list
		private int city;		    // Holds an integer representation of the name of the city
		private int distance;		// Holds the distance to get to this city
		private double price;		// Holds the price to fly to this city 
				
		// Constructor
		public Node()				
		{
			city = 0;
			distance = 0;
			price = 0;
		}
			
		// Returns the next node
		public Node getNext()
		{
			return next;
		}
		// Sets the city field
		public void setCity(int cityNumber)
		{
			city = cityNumber;
		}
				
		// Returns the city field 
		public int getCity()
		{
			return city;
		}
	
		// Sets the distance field
		public void setDistance(int distance)
		{
			this.distance = distance;
		}
						
		// Returns the distance field 
		public int getDistance()
		{
			return distance;
		}
				
		// Sets the city field
		public void setPrice(double price)
		{
			this.price = price;
		}
								
		// Returns the city field 
		public double getPrice()
		{
			return price;
		}
	}
	
	
	// Authors Robert Sedgewick and author Kevin Wayne
	// Code can be obtained at http://algs4.cs.princeton.edu/home/
	//
	// Code for Edge from textbook  
	public static class Edge implements Comparable<Edge> { 

	    private final int v;
	    private final int w;
	    private final double weight;

	    /**
	     * Initializes an edge between vertices <tt>v/tt> and <tt>w</tt> of
	     * the given <tt>weight</tt>.
	     * param v one vertex
	     * param w the other vertex
	     * param weight the weight of the edge
	     * @throws IndexOutOfBoundsException if either <tt>v</tt> or <tt>w</tt> 
	     *    is a negative integer
	     * @throws IllegalArgumentException if <tt>weight</tt> is <tt>NaN</tt>
	     */
	    public Edge(int v, int w, double weight) {
	        if (v < 0) throw new IndexOutOfBoundsException("Vertex name must be a nonnegative integer");
	        if (w < 0) throw new IndexOutOfBoundsException("Vertex name must be a nonnegative integer");
	        if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
	        this.v = v;
	        this.w = w;
	        this.weight = weight;
	    }

	    /**
	     * Returns the weight of the edge.
	     * @return the weight of the edge
	     */
	    public double weight() {
	        return weight;
	    }

	    /**
	     * Returns either endpoint of the edge.
	     * @return either endpoint of the edge
	     */
	    public int either() {
	        return v;
	    }

	    /**
	     * Returns the endpoint of the edge that is different from the given vertex
	     * (unless the edge represents a self-loop in which case it returns the same vertex).
	     * @param vertex one endpoint of the edge
	     * @return the endpoint of the edge that is different from the given vertex
	     *   (unless the edge represents a self-loop in which case it returns the same vertex)
	     * @throws java.lang.IllegalArgumentException if the vertex is not one of the endpoints
	     *   of the edge
	     */
	    public int other(int vertex) {
	        if      (vertex == v) return w;
	        else if (vertex == w) return v;
	        else throw new IllegalArgumentException("Illegal endpoint");
	    }

	    /**
	     * Compares two edges by weight.
	     * @param that the other edge
	     * @return a negative integer, zero, or positive integer depending on whether
	     *    this edge is less than, equal to, or greater than that edge
	     */
	    public int compareTo(Edge that) {
	        if      (this.weight() < that.weight()) return -1;
	        else if (this.weight() > that.weight()) return +1;
	        else                                    return  0;
	    }

	    /**
	     * Returns a string representation of the edge.
	     * @return a string representation of the edge
	     */
	    public String toString() {
	        return String.format("%d-%d %.5f", v, w, weight);
	    }

	    /**
	     * Unit tests the <tt>Edge</tt> data type.
	     */
	    public static void main(String[] args) {
	        Edge e = new Edge(12, 23, 3.14);
	       System.out.println(e);
	    }
	}
	
	// Code for Edge weighted graph from textbook
	public static class EdgeWeightedGraph {
	    private final int V;
	    private int E;
	    private Bag<Edge>[] adj;
	    
	    /**
	     * Initializes an empty edge-weighted graph with <tt>V</tt> vertices and 0 edges.
	     * param V the number of vertices
	     * @throws java.lang.IllegalArgumentException if <tt>V</tt> < 0
	     */
	    public EdgeWeightedGraph(int V) {
	        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
	        this.V = V;
	        this.E = 0;
	        adj = (Bag<Edge>[]) new Bag[V];
	        for (int v = 0; v < V; v++) {
	            adj[v] = new Bag<Edge>();
	        }
	    }

	    /**
	     * Initializes a random edge-weighted graph with <tt>V</tt> vertices and <em>E</em> edges.
	     * param V the number of vertices
	     * param E the number of edges
	     * @throws java.lang.IllegalArgumentException if <tt>V</tt> < 0
	     * @throws java.lang.IllegalArgumentException if <tt>E</tt> < 0
	     */
	    public EdgeWeightedGraph(int V, int E) {
	        this(V);
	        if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
	        for (int i = 0; i < E; i++) {
	            int v = (int) (Math.random() * V);
	            int w = (int) (Math.random() * V);
	            double weight = Math.round(100 * Math.random()) / 100.0;
	            Edge e = new Edge(v, w, weight);
	            addEdge(e);
	        }
	    }


	    /**
	     * Initializes a new edge-weighted graph that is a deep copy of <tt>G</tt>.
	     * @param G the edge-weighted graph to copy
	     */
	    public EdgeWeightedGraph(EdgeWeightedGraph G) {
	        this(G.V());
	        this.E = G.E();
	        for (int v = 0; v < G.V(); v++) {
	            // reverse so that adjacency list is in same order as original
	            Stack<Edge> reverse = new Stack<Edge>();
	            for (Edge e : G.adj[v]) {
	                reverse.push(e);
	            }
	            for (Edge e : reverse) {
	                adj[v].add(e);
	            }
	        }
	    }


	    /**
	     * Returns the number of vertices in the edge-weighted graph.
	     * @return the number of vertices in the edge-weighted graph
	     */
	    public int V() {
	        return V;
	    }

	    /**
	     * Returns the number of edges in the edge-weighted graph.
	     * @return the number of edges in the edge-weighted graph
	     */
	    public int E() {
	        return E;
	    }

	    // throw an IndexOutOfBoundsException unless 0 <= v < V
	    private void validateVertex(int v) {
	        if (v < 0 || v >= V)
	            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
	    }

	    /**
	     * Adds the undirected edge <tt>e</tt> to the edge-weighted graph.
	     * @param e the edge
	     * @throws java.lang.IndexOutOfBoundsException unless both endpoints are between 0 and V-1
	     */
	    public void addEdge(Edge e) {
	        int v = e.either();
	        int w = e.other(v);
	        validateVertex(v);
	        validateVertex(w);
	        adj[v].add(e);
	        adj[w].add(e);
	        E++;
	    }

	    /**
	     * Returns the edges incident on vertex <tt>v</tt>.
	     * @return the edges incident on vertex <tt>v</tt> as an Iterable
	     * @param v the vertex
	     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
	     */
	    public Iterable<Edge> adj(int v) {
	        validateVertex(v);
	        return adj[v];
	    }

	    /**
	     * Returns the degree of vertex <tt>v</tt>.
	     * @return the degree of vertex <tt>v</tt>               
	     * @param v the vertex
	     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
	     */
	    public int degree(int v) {
	        validateVertex(v);
	        return adj[v].size();
	    }

	    /**
	     * Returns all edges in the edge-weighted graph.
	     * To iterate over the edges in the edge-weighted graph, use foreach notation:
	     * <tt>for (Edge e : G.edges())</tt>.
	     * @return all edges in the edge-weighted graph as an Iterable.
	     */
	    public Iterable<Edge> edges() {
	        Bag<Edge> list = new Bag<Edge>();
	        for (int v = 0; v < V; v++) {
	            int selfLoops = 0;
	            for (Edge e : adj(v)) {
	                if (e.other(v) > v) {
	                    list.add(e);
	                }
	                // only add one copy of each self loop (self loops will be consecutive)
	                else if (e.other(v) == v) {
	                    if (selfLoops % 2 == 0) list.add(e);
	                    selfLoops++;
	                }
	            }
	        }
	        return list;
	    }

	    /**
	     * Returns a string representation of the edge-weighted graph.
	     * This method takes time proportional to <em>E</em> + <em>V</em>.
	     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
	     *   followed by the <em>V</em> adjacency lists of edges
	     */
	    public String toString() {
	        String NEWLINE = System.getProperty("line.separator");
	        StringBuilder s = new StringBuilder();
	        s.append(V + " " + E + NEWLINE);
	        for (int v = 0; v < V; v++) {
	            s.append(v + ": ");
	            for (Edge e : adj[v]) {
	                s.append(e + "  ");
	            }
	            s.append(NEWLINE);
	        }
	        return s.toString();
	    }


	}

	// Code for linked bag from textbook
	public static class Bag<Item> implements Iterable<Item> {
	    private int N;               // number of elements in bag
	    private Node<Item> first;    // beginning of bag

	    // helper linked list class
	    private class Node<Item> {
	        private Item item;
	        private Node<Item> next;
	    }

	    /**
	     * Initializes an empty bag.
	     */
	    public Bag() {
	        first = null;
	        N = 0;
	    }

	    /**
	     * Is this bag empty?
	     * @return true if this bag is empty; false otherwise
	     */
	    public boolean isEmpty() {
	        return first == null;
	    }

	    /**
	     * Returns the number of items in this bag.
	     * @return the number of items in this bag
	     */
	    public int size() {
	        return N;
	    }

	    /**
	     * Adds the item to this bag.
	     * @param item the item to add to this bag
	     */
	    public void add(Item item) {
	        Node<Item> oldfirst = first;
	        first = new Node<Item>();
	        first.item = item;
	        first.next = oldfirst;
	        N++;
	    }


	    /**
	     * Returns an iterator that iterates over the items in the bag in arbitrary order.
	     * @return an iterator that iterates over the items in the bag in arbitrary order
	     */
	    public Iterator<Item> iterator()  {
	        return new ListIterator<Item>(first);  
	    }

	    // an iterator, doesn't implement remove() since it's optional
	    private class ListIterator<Item> implements Iterator<Item> {
	        private Node<Item> current;

	        public ListIterator(Node<Item> first) {
	            current = first;
	        }

	        public boolean hasNext()  { return current != null;                     }
	        public void remove()      { throw new UnsupportedOperationException();  }

	        public Item next() {
	            if (!hasNext()) throw new NoSuchElementException();
	            Item item = current.item;
	            current = current.next; 
	            return item;
	        }
	    }

	    
	    
	    
	    
	    
	    
	    
	    


	}
	
	// Code for Lazy Prim's from textbook
	public static class LazyPrimMST {
	    private double weight;       // total weight of MST
	    private Queue<Edge> mst;     // edges in the MST
	    private boolean[] marked;    // marked[v] = true if v on tree
	    private MinPQ<Edge> pq;      // edges with one endpoint in tree

	    /**
	     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
	     * @param G the edge-weighted graph
	     */
	    public LazyPrimMST(EdgeWeightedGraph G) {
	        mst = new Queue<Edge>();
	        pq = new MinPQ<Edge>();
	        marked = new boolean[G.V()];
	        for (int v = 0; v < G.V(); v++)     // run Prim from all vertices to
	            if (!marked[v]) prim(G, v);     // get a minimum spanning forest

	        // check optimality conditions
	        assert check(G);
	    }

	    // run Prim's algorithm
	    private void prim(EdgeWeightedGraph G, int s) {
	        scan(G, s);
	        while (!pq.isEmpty()) {                        // better to stop when mst has V-1 edges
	            Edge e = pq.delMin();                      // smallest edge on pq
	            int v = e.either(), w = e.other(v);        // two endpoints
	            assert marked[v] || marked[w];
	            if (marked[v] && marked[w]) continue;      // lazy, both v and w already scanned
	            mst.enqueue(e);                            // add e to MST
	            weight += e.weight();
	            if (!marked[v]) scan(G, v);               // v becomes part of tree
	            if (!marked[w]) scan(G, w);               // w becomes part of tree
	        }
	    }

	    // add all edges e incident to v onto pq if the other endpoint has not yet been scanned
	    private void scan(EdgeWeightedGraph G, int v) {
	        assert !marked[v];
	        marked[v] = true;
	        for (Edge e : G.adj(v))
	            if (!marked[e.other(v)]) pq.insert(e);
	    }
	        
	    /**
	     * Returns the edges in a minimum spanning tree (or forest).
	     * @return the edges in a minimum spanning tree (or forest) as
	     *    an iterable of edges
	     */
	    public Iterable<Edge> edges() {
	        return mst;
	    }

	    /**
	     * Returns the sum of the edge weights in a minimum spanning tree (or forest).
	     * @return the sum of the edge weights in a minimum spanning tree (or forest)
	     */
	    public double weight() {
	        return weight;
	    }

	    // check optimality conditions (takes time proportional to E V lg* V)
	    private boolean check(EdgeWeightedGraph G) {

	        // check weight
	        double totalWeight = 0.0;
	        for (Edge e : edges()) {
	            totalWeight += e.weight();
	        }
	        double EPSILON = 1E-12;
	        if (Math.abs(totalWeight - weight()) > EPSILON) {
	            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, weight());
	            return false;
	        }

	        // check that it is acyclic
	        UF uf = new UF(G.V());
	        for (Edge e : edges()) {
	            int v = e.either(), w = e.other(v);
	            if (uf.connected(v, w)) {
	                System.err.println("Not a forest");
	                return false;
	            }
	            uf.union(v, w);
	        }

	        // check that it is a spanning forest
	        for (Edge e : G.edges()) {
	            int v = e.either(), w = e.other(v);
	            if (!uf.connected(v, w)) {
	                System.err.println("Not a spanning forest");
	                return false;
	            }
	        }

	        // check that it is a minimal spanning forest (cut optimality conditions)
	        for (Edge e : edges()) {

	            // all edges in MST except e
	            uf = new UF(G.V());
	            for (Edge f : mst) {
	                int x = f.either(), y = f.other(x);
	                if (f != e) uf.union(x, y);
	            }

	            // check that e is min weight edge in crossing cut
	            for (Edge f : G.edges()) {
	                int x = f.either(), y = f.other(x);
	                if (!uf.connected(x, y)) {
	                    if (f.weight() < e.weight()) {
	                        System.err.println("Edge " + f + " violates cut optimality conditions");
	                        return false;
	                    }
	                }
	            }

	        }

	        return true;
	    }

	}
	
	// Code for Queue from textbook
	public static class Queue<Item> implements Iterable<Item> {
	    private int N;               // number of elements on queue
	    private Node<Item> first;    // beginning of queue
	    private Node<Item> last;     // end of queue

	    // helper linked list class
	    private static class Node<Item> {
	        private Item item;
	        private Node<Item> next;
	    }

	    /**
	     * Initializes an empty queue.
	     */
	    public Queue() {
	        first = null;
	        last  = null;
	        N = 0;
	    }

	    /**
	     * Is this queue empty?
	     * @return true if this queue is empty; false otherwise
	     */
	    public boolean isEmpty() {
	        return first == null;
	    }

	    /**
	     * Returns the number of items in this queue.
	     * @return the number of items in this queue
	     */
	    public int size() {
	        return N;     
	    }

	    /**
	     * Returns the item least recently added to this queue.
	     * @return the item least recently added to this queue
	     * @throws java.util.NoSuchElementException if this queue is empty
	     */
	    public Item peek() {
	        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
	        return first.item;
	    }

	    /**
	     * Adds the item to this queue.
	     * @param item the item to add
	     */
	    public void enqueue(Item item) {
	        Node<Item> oldlast = last;
	        last = new Node<Item>();
	        last.item = item;
	        last.next = null;
	        if (isEmpty()) first = last;
	        else           oldlast.next = last;
	        N++;
	    }

	    /**
	     * Removes and returns the item on this queue that was least recently added.
	     * @return the item on this queue that was least recently added
	     * @throws java.util.NoSuchElementException if this queue is empty
	     */
	    public Item dequeue() {
	        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
	        Item item = first.item;
	        first = first.next;
	        N--;
	        if (isEmpty()) last = null;   // to avoid loitering
	        return item;
	    }

	    /**
	     * Returns a string representation of this queue.
	     * @return the sequence of items in FIFO order, separated by spaces
	     */
	    public String toString() {
	        StringBuilder s = new StringBuilder();
	        for (Item item : this)
	            s.append(item + " ");
	        return s.toString();
	    } 

	    /**
	     * Returns an iterator that iterates over the items in this queue in FIFO order.
	     * @return an iterator that iterates over the items in this queue in FIFO order
	     */
	    public Iterator<Item> iterator()  {
	        return new ListIterator<Item>(first);  
	    }

	    // an iterator, doesn't implement remove() since it's optional
	    private class ListIterator<Item> implements Iterator<Item> {
	        private Node<Item> current;

	        public ListIterator(Node<Item> first) {
	            current = first;
	        }

	        public boolean hasNext()  { return current != null;                     }
	        public void remove()      { throw new UnsupportedOperationException();  }

	        public Item next() {
	            if (!hasNext()) throw new NoSuchElementException();
	            Item item = current.item;
	            current = current.next; 
	            return item;
	        }
	    }
	}
	
	// Code for minPQ from textbook
	public static class MinPQ<Key> implements Iterable<Key> {
	    private Key[] pq;                    // store items at indices 1 to N
	    private int N;                       // number of items on priority queue
	    private Comparator<Key> comparator;  // optional comparator

	    /**
	     * Initializes an empty priority queue with the given initial capacity.
	     * @param initCapacity the initial capacity of the priority queue
	     */
	    public MinPQ(int initCapacity) {
	        pq = (Key[]) new Object[initCapacity + 1];
	        N = 0;
	    }

	    /**
	     * Initializes an empty priority queue.
	     */
	    public MinPQ() {
	        this(1);
	    }

	    /**
	     * Initializes an empty priority queue with the given initial capacity,
	     * using the given comparator.
	     * @param initCapacity the initial capacity of the priority queue
	     * @param comparator the order to use when comparing keys
	     */
	    public MinPQ(int initCapacity, Comparator<Key> comparator) {
	        this.comparator = comparator;
	        pq = (Key[]) new Object[initCapacity + 1];
	        N = 0;
	    }

	    /**
	     * Initializes an empty priority queue using the given comparator.
	     * @param comparator the order to use when comparing keys
	     */
	    public MinPQ(Comparator<Key> comparator) { this(1, comparator); }

	    /**
	     * Initializes a priority queue from the array of keys.
	     * Takes time proportional to the number of keys, using sink-based heap construction.
	     * @param keys the array of keys
	     */
	    public MinPQ(Key[] keys) {
	        N = keys.length;
	        pq = (Key[]) new Object[keys.length + 1];
	        for (int i = 0; i < N; i++)
	            pq[i+1] = keys[i];
	        for (int k = N/2; k >= 1; k--)
	            sink(k);
	        assert isMinHeap();
	    }

	    /**
	     * Is the priority queue empty?
	     * @return true if the priority queue is empty; false otherwise
	     */
	    public boolean isEmpty() {
	        return N == 0;
	    }

	    /**
	     * Returns the number of keys on the priority queue.
	     * @return the number of keys on the priority queue
	     */
	    public int size() {
	        return N;
	    }

	    /**
	     * Returns a smallest key on the priority queue.
	     * @return a smallest key on the priority queue
	     * @throws java.util.NoSuchElementException if priority queue is empty
	     */
	    public Key min() {
	        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
	        return pq[1];
	    }

	    // helper function to double the size of the heap array
	    private void resize(int capacity) {
	        assert capacity > N;
	        Key[] temp = (Key[]) new Object[capacity];
	        for (int i = 1; i <= N; i++) temp[i] = pq[i];
	        pq = temp;
	    }

	    /**
	     * Adds a new key to the priority queue.
	     * @param x the key to add to the priority queue
	     */
	    public void insert(Key x) {
	        // double size of array if necessary
	        if (N == pq.length - 1) resize(2 * pq.length);

	        // add x, and percolate it up to maintain heap invariant
	        pq[++N] = x;
	        swim(N);
	        assert isMinHeap();
	    }

	    /**
	     * Removes and returns a smallest key on the priority queue.
	     * @return a smallest key on the priority queue
	     * @throws java.util.NoSuchElementException if the priority queue is empty
	     */
	    public Key delMin() {
	        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
	        exch(1, N);
	        Key min = pq[N--];
	        sink(1);
	        pq[N+1] = null;         // avoid loitering and help with garbage collection
	        if ((N > 0) && (N == (pq.length - 1) / 4)) resize(pq.length  / 2);
	        assert isMinHeap();
	        return min;
	    }


	   /***********************************************************************
	    * Helper functions to restore the heap invariant.
	    **********************************************************************/

	    private void swim(int k) {
	        while (k > 1 && greater(k/2, k)) {
	            exch(k, k/2);
	            k = k/2;
	        }
	    }

	    private void sink(int k) {
	        while (2*k <= N) {
	            int j = 2*k;
	            if (j < N && greater(j, j+1)) j++;
	            if (!greater(k, j)) break;
	            exch(k, j);
	            k = j;
	        }
	    }

	   /***********************************************************************
	    * Helper functions for compares and swaps.
	    **********************************************************************/
	    private boolean greater(int i, int j) {
	        if (comparator == null) {
	            return ((Comparable<Key>) pq[i]).compareTo(pq[j]) > 0;
	        }
	        else {
	            return comparator.compare(pq[i], pq[j]) > 0;
	        }
	    }

	    private void exch(int i, int j) {
	        Key swap = pq[i];
	        pq[i] = pq[j];
	        pq[j] = swap;
	    }

	    // is pq[1..N] a min heap?
	    private boolean isMinHeap() {
	        return isMinHeap(1);
	    }

	    // is subtree of pq[1..N] rooted at k a min heap?
	    private boolean isMinHeap(int k) {
	        if (k > N) return true;
	        int left = 2*k, right = 2*k + 1;
	        if (left  <= N && greater(k, left))  return false;
	        if (right <= N && greater(k, right)) return false;
	        return isMinHeap(left) && isMinHeap(right);
	    }


	   /***********************************************************************
	    * Iterators
	    **********************************************************************/

	    /**
	     * Returns an iterator that iterates over the keys on the priority queue
	     * in ascending order.
	     * The iterator doesn't implement <tt>remove()</tt> since it's optional.
	     * @return an iterator that iterates over the keys in ascending order
	     */
	    public Iterator<Key> iterator() { return new HeapIterator(); }

	    private class HeapIterator implements Iterator<Key> {
	        // create a new pq
	        private MinPQ<Key> copy;

	        // add all items to copy of heap
	        // takes linear time since already in heap order so no keys move
	        public HeapIterator() {
	            if (comparator == null) copy = new MinPQ<Key>(size());
	            else                    copy = new MinPQ<Key>(size(), comparator);
	            for (int i = 1; i <= N; i++)
	                copy.insert(pq[i]);
	        }

	        public boolean hasNext()  { return !copy.isEmpty();                     }
	        public void remove()      { throw new UnsupportedOperationException();  }

	        public Key next() {
	            if (!hasNext()) throw new NoSuchElementException();
	            return copy.delMin();
	        }
	    }

	}
	
	// Code for UnionFind from textbook
	public static class UF {
	    private int[] id;     // id[i] = parent of i
	    private byte[] rank;  // rank[i] = rank of subtree rooted at i (cannot be more than 31)
	    private int count;    // number of components

	    /**
	     * Initializes an empty union-find data structure with <tt>N</tt>
	     * isolated components <tt>0</tt> through <tt>N-1</tt>
	     * @throws java.lang.IllegalArgumentException if <tt>N &lt; 0</tt>
	     * @param N the number of sites
	     */
	    public UF(int N) {
	        if (N < 0) throw new IllegalArgumentException();
	        count = N;
	        id = new int[N];
	        rank = new byte[N];
	        for (int i = 0; i < N; i++) {
	            id[i] = i;
	            rank[i] = 0;
	        }
	    }

	    /**
	     * Returns the component identifier for the component containing site <tt>p</tt>.
	     * @param p the integer representing one object
	     * @return the component identifier for the component containing site <tt>p</tt>
	     * @throws java.lang.IndexOutOfBoundsException unless <tt>0 &le; p &lt; N</tt>
	     */
	    public int find(int p) {
	        if (p < 0 || p >= id.length) throw new IndexOutOfBoundsException();
	        while (p != id[p]) {
	            id[p] = id[id[p]];    // path compression by halving
	            p = id[p];
	        }
	        return p;
	    }

	    /**
	     * Returns the number of components.
	     * @return the number of components (between <tt>1</tt> and <tt>N</tt>)
	     */
	    public int count() {
	        return count;
	    }
	  
	    /**
	     * Are the two sites <tt>p</tt> and <tt>q</tt> in the same component?
	     * @param p the integer representing one site
	     * @param q the integer representing the other site
	     * @return true if the two sites <tt>p</tt> and <tt>q</tt> are in the same component; false otherwise
	     * @throws java.lang.IndexOutOfBoundsException unless
	     *      both <tt>0 &le; p &lt; N</tt> and <tt>0 &le; q &lt; N</tt>
	     */
	    public boolean connected(int p, int q) {
	        return find(p) == find(q);
	    }

	  
	    /**
	     * Merges the component containing site <tt>p</tt> with the 
	     * the component containing site <tt>q</tt>.
	     * @param p the integer representing one site
	     * @param q the integer representing the other site
	     * @throws java.lang.IndexOutOfBoundsException unless
	     *      both <tt>0 &le; p &lt; N</tt> and <tt>0 &le; q &lt; N</tt>
	     */
	    public void union(int p, int q) {
	        int i = find(p);
	        int j = find(q);
	        if (i == j) return;

	        // make root of smaller rank point to root of larger rank
	        if      (rank[i] < rank[j]) id[i] = j;
	        else if (rank[i] > rank[j]) id[j] = i;
	        else {
	            id[j] = i;
	            rank[i]++;
	        }
	        count--;
	    }   
	}
	
	// Code for dijkstra's from textbook
	public static class DijkstraSP {
	    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
	    private DirectedEdge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
	    private IndexMinPQ<Double> pq;    // priority queue of vertices

	    /**
	     * Computes a shortest paths tree from <tt>s</tt> to every other vertex in
	     * the edge-weighted digraph <tt>G</tt>.
	     * @param G the edge-weighted digraph
	     * @param s the source vertex
	     * @throws IllegalArgumentException if an edge weight is negative
	     * @throws IllegalArgumentException unless 0 &le; <tt>s</tt> &le; <tt>V</tt> - 1
	     */
	    public DijkstraSP(EdgeWeightedDigraph G, int s) {
	        for (DirectedEdge e : G.edges()) {
	            if (e.weight() < 0)
	                throw new IllegalArgumentException("edge " + e + " has negative weight");
	        }

	        distTo = new double[G.V()];
	        edgeTo = new DirectedEdge[G.V()];
	        for (int v = 0; v < G.V(); v++)
	            distTo[v] = Double.POSITIVE_INFINITY;
	        distTo[s] = 0.0;

	        // relax vertices in order of distance from s
	        pq = new IndexMinPQ<Double>(G.V());
	        pq.insert(s, distTo[s]);
	        while (!pq.isEmpty()) {
	            int v = pq.delMin();
	            for (DirectedEdge e : G.adj(v))
	                relax(e);
	        }

	        // check optimality conditions
	        assert check(G, s);
	    }

	    // relax edge e and update pq if changed
	    private void relax(DirectedEdge e) {
	        int v = e.from(), w = e.to();
	        if (distTo[w] > distTo[v] + e.weight()) {
	            distTo[w] = distTo[v] + e.weight();
	            edgeTo[w] = e;
	            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
	            else                pq.insert(w, distTo[w]);
	        }
	    }

	    /**
	     * Returns the length of a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>.
	     * @param v the destination vertex
	     * @return the length of a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>;
	     *    <tt>Double.POSITIVE_INFINITY</tt> if no such path
	     */
	    public double distTo(int v) {
	        return distTo[v];
	    }

	    /**
	     * Is there a path from the source vertex <tt>s</tt> to vertex <tt>v</tt>?
	     * @param v the destination vertex
	     * @return <tt>true</tt> if there is a path from the source vertex
	     *    <tt>s</tt> to vertex <tt>v</tt>, and <tt>false</tt> otherwise
	     */
	    public boolean hasPathTo(int v) {
	        return distTo[v] < Double.POSITIVE_INFINITY;
	    }

	    /**
	     * Returns a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>.
	     * @param v the destination vertex
	     * @return a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>
	     *    as an iterable of edges, and <tt>null</tt> if no such path
	     */
	    public Iterable<DirectedEdge> pathTo(int v) {
	        if (!hasPathTo(v)) return null;
	        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
	        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
	            path.push(e);
	        }
	        return path;
	    }


	    // check optimality conditions:
	    // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
	    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
	    private boolean check(EdgeWeightedDigraph G, int s) {

	        // check that edge weights are nonnegative
	        for (DirectedEdge e : G.edges()) {
	            if (e.weight() < 0) {
	                System.err.println("negative edge weight detected");
	                return false;
	            }
	        }

	        // check that distTo[v] and edgeTo[v] are consistent
	        if (distTo[s] != 0.0 || edgeTo[s] != null) {
	            System.err.println("distTo[s] and edgeTo[s] inconsistent");
	            return false;
	        }
	        for (int v = 0; v < G.V(); v++) {
	            if (v == s) continue;
	            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
	                System.err.println("distTo[] and edgeTo[] inconsistent");
	                return false;
	            }
	        }

	        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
	        for (int v = 0; v < G.V(); v++) {
	            for (DirectedEdge e : G.adj(v)) {
	                int w = e.to();
	                if (distTo[v] + e.weight() < distTo[w]) {
	                    System.err.println("edge " + e + " not relaxed");
	                    return false;
	                }
	            }
	        }

	        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
	        for (int w = 0; w < G.V(); w++) {
	            if (edgeTo[w] == null) continue;
	            DirectedEdge e = edgeTo[w];
	            int v = e.from();
	            if (w != e.to()) return false;
	            if (distTo[v] + e.weight() != distTo[w]) {
	                System.err.println("edge " + e + " on shortest path not tight");
	                return false;
	            }
	        }
	        return true;
	    }


	    /*
	    public static void main(String[] args) {
	        In in = new In(args[0]);
	        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
	        int s = Integer.parseInt(args[1]);

	        // compute shortest paths
	        DijkstraSP sp = new DijkstraSP(G, s);


	        // print shortest path
	        for (int t = 0; t < G.V(); t++) 
	        {
	            if (sp.hasPathTo(t)) 
	            {
	                StdOut.printf("%d to %d (%.2f)  ", s, t, sp.distTo(t));
	                if (sp.hasPathTo(t)) 
	                {
	                    for (DirectedEdge e : sp.pathTo(t)) 
	                    {
	                        StdOut.print(e + "   ");
	                    }
	                }
	                StdOut.println();
	            }
	            else 
	            {
	                StdOut.printf("%d to %d         no path\n", s, t);
	            }
	        }
	    }*/

	}

	// Code for directed edge from textbook
	public static class DirectedEdge { 
	    private final int v;
	    private final int w;
	    private final double weight;
	    
	    /**
	     * Initializes a directed edge from vertex <tt>v</tt> to vertex <tt>w</tt> with
	     * the given <tt>weight</tt>.
	     * @param v the tail vertex
	     * @param w the head vertex
	     * @param weight the weight of the directed edge
	     * @throws java.lang.IndexOutOfBoundsException if either <tt>v</tt> or <tt>w</tt>
	     *    is a negative integer
	     * @throws IllegalArgumentException if <tt>weight</tt> is <tt>NaN</tt>
	     */
	    public DirectedEdge(int v, int w, double weight) {
	        if (v < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
	        if (w < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
	        if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
	        this.v = v;
	        this.w = w;
	        this.weight = weight;
	    }

	    /**
	     * Returns the tail vertex of the directed edge.
	     * @return the tail vertex of the directed edge
	     */
	    public int from() {
	        return v;
	    }

	    /**
	     * Returns the head vertex of the directed edge.
	     * @return the head vertex of the directed edge
	     */
	    public int to() {
	        return w;
	    }

	    /**
	     * Returns the weight of the directed edge.
	     * @return the weight of the directed edge
	     */
	    public double weight() {
	        return weight;
	    }

	    /**
	     * Returns a string representation of the directed edge.
	     * @return a string representation of the directed edge
	     */
	    public String toString() {
	        return v + "->" + w + " " + String.format("%5.2f", weight);
	    }

	    /*
	    public static void main(String[] args) {
	        DirectedEdge e = new DirectedEdge(12, 23, 3.14);
	        StdOut.println(e);
	    }*/
	}
	
	// Code for Indexed MinPQ for redirection for dijkstra's
	public static class IndexMinPQ<Key extends Comparable<Key>> implements Iterable<Integer> {
	    private int NMAX;        // maximum number of elements on PQ
	    private int N;           // number of elements on PQ
	    private int[] pq;        // binary heap using 1-based indexing
	    private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
	    private Key[] keys;      // keys[i] = priority of i

	    /**
	     * Initializes an empty indexed priority queue with indices between 0 and NMAX-1.
	     * @param NMAX the keys on the priority queue are index from 0 to NMAX-1
	     * @throws java.lang.IllegalArgumentException if NMAX < 0
	     */
	    public IndexMinPQ(int NMAX) {
	        if (NMAX < 0) throw new IllegalArgumentException();
	        this.NMAX = NMAX;
	        keys = (Key[]) new Comparable[NMAX + 1];    // make this of length NMAX??
	        pq   = new int[NMAX + 1];
	        qp   = new int[NMAX + 1];                   // make this of length NMAX??
	        for (int i = 0; i <= NMAX; i++) qp[i] = -1;
	    }

	    /**
	     * Is the priority queue empty?
	     * @return true if the priority queue is empty; false otherwise
	     */
	    public boolean isEmpty() {
	        return N == 0;
	    }

	    /**
	     * Is i an index on the priority queue?
	     * @param i an index
	     * @throws java.lang.IndexOutOfBoundsException unless (0 &le; i < NMAX)
	     */
	    public boolean contains(int i) {
	        if (i < 0 || i >= NMAX) throw new IndexOutOfBoundsException();
	        return qp[i] != -1;
	    }

	    /**
	     * Returns the number of keys on the priority queue.
	     * @return the number of keys on the priority queue
	     */
	    public int size() {
	        return N;
	    }

	    /**
	     * Associates key with index i.
	     * @param i an index
	     * @param key the key to associate with index i
	     * @throws java.lang.IndexOutOfBoundsException unless 0 &le; i < NMAX
	     * @throws java.util.IllegalArgumentException if there already is an item associated with index i
	     */
	    public void insert(int i, Key key) {
	        if (i < 0 || i >= NMAX) throw new IndexOutOfBoundsException();
	        if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
	        N++;
	        qp[i] = N;
	        pq[N] = i;
	        keys[i] = key;
	        swim(N);
	    }

	    /**
	     * Returns an index associated with a minimum key.
	     * @return an index associated with a minimum key
	     * @throws java.util.NoSuchElementException if priority queue is empty
	     */
	    public int minIndex() { 
	        if (N == 0) throw new NoSuchElementException("Priority queue underflow");
	        return pq[1];        
	    }

	    /**
	     * Returns a minimum key.
	     * @return a minimum key
	     * @throws java.util.NoSuchElementException if priority queue is empty
	     */
	    public Key minKey() { 
	        if (N == 0) throw new NoSuchElementException("Priority queue underflow");
	        return keys[pq[1]];        
	    }

	    /**
	     * Removes a minimum key and returns its associated index.
	     * @return an index associated with a minimum key
	     * @throws java.util.NoSuchElementException if priority queue is empty
	     */
	    public int delMin() { 
	        if (N == 0) throw new NoSuchElementException("Priority queue underflow");
	        int min = pq[1];        
	        exch(1, N--); 
	        sink(1);
	        qp[min] = -1;            // delete
	        keys[pq[N+1]] = null;    // to help with garbage collection
	        pq[N+1] = -1;            // not needed
	        return min; 
	    }

	    /**
	     * Returns the key associated with index i.
	     * @param i the index of the key to return
	     * @return the key associated with index i
	     * @throws java.lang.IndexOutOfBoundsException unless 0 &le; i < NMAX
	     * @throws java.util.NoSuchElementException no key is associated with index i
	     */
	    public Key keyOf(int i) {
	        if (i < 0 || i >= NMAX) throw new IndexOutOfBoundsException();
	        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
	        else return keys[i];
	    }

	    /**
	     * Change the key associated with index i to the specified value.
	     * @param i the index of the key to change
	     * @param key change the key assocated with index i to this key
	     * @throws java.lang.IndexOutOfBoundsException unless 0 &le; i < NMAX
	     * @deprecated Replaced by changeKey()
	     */
	    @Deprecated public void change(int i, Key key) {
	        changeKey(i, key);
	    }

	    /**
	     * Change the key associated with index i to the specified value.
	     * @param i the index of the key to change
	     * @param key change the key assocated with index i to this key
	     * @throws java.lang.IndexOutOfBoundsException unless 0 &le; i < NMAX
	     * @throws java.util.NoSuchElementException no key is associated with index i
	     */
	    public void changeKey(int i, Key key) {
	        if (i < 0 || i >= NMAX) throw new IndexOutOfBoundsException();
	        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
	        keys[i] = key;
	        swim(qp[i]);
	        sink(qp[i]);
	    }

	    /**
	     * Decrease the key associated with index i to the specified value.
	     * @param i the index of the key to decrease
	     * @param key decrease the key assocated with index i to this key
	     * @throws java.lang.IndexOutOfBoundsException unless 0 &le; i < NMAX
	     * @throws java.lang.IllegalArgumentException if key &ge; key associated with index i
	     * @throws java.util.NoSuchElementException no key is associated with index i
	     */
	    public void decreaseKey(int i, Key key) {
	        if (i < 0 || i >= NMAX) throw new IndexOutOfBoundsException();
	        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
	        if (keys[i].compareTo(key) <= 0) throw new IllegalArgumentException("Calling decreaseKey() with given argument would not strictly decrease the key");
	        keys[i] = key;
	        swim(qp[i]);
	    }

	    /**
	     * Increase the key associated with index i to the specified value.
	     * @param i the index of the key to increase
	     * @param key increase the key assocated with index i to this key
	     * @throws java.lang.IndexOutOfBoundsException unless 0 &le; i < NMAX
	     * @throws java.lang.IllegalArgumentException if key &le; key associated with index i
	     * @throws java.util.NoSuchElementException no key is associated with index i
	     */
	    public void increaseKey(int i, Key key) {
	        if (i < 0 || i >= NMAX) throw new IndexOutOfBoundsException();
	        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
	        if (keys[i].compareTo(key) >= 0) throw new IllegalArgumentException("Calling increaseKey() with given argument would not strictly increase the key");
	        keys[i] = key;
	        sink(qp[i]);
	    }

	    /**
	     * Remove the key associated with index i.
	     * @param i the index of the key to remove
	     * @throws java.lang.IndexOutOfBoundsException unless 0 &le; i < NMAX
	     * @throws java.util.NoSuchElementException no key is associated with index i
	     */
	    public void delete(int i) {
	        if (i < 0 || i >= NMAX) throw new IndexOutOfBoundsException();
	        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
	        int index = qp[i];
	        exch(index, N--);
	        swim(index);
	        sink(index);
	        keys[i] = null;
	        qp[i] = -1;
	    }


	   /**************************************************************
	    * General helper functions
	    **************************************************************/
	    private boolean greater(int i, int j) {
	        return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
	    }

	    private void exch(int i, int j) {
	        int swap = pq[i]; pq[i] = pq[j]; pq[j] = swap;
	        qp[pq[i]] = i; qp[pq[j]] = j;
	    }


	   /**************************************************************
	    * Heap helper functions
	    **************************************************************/
	    private void swim(int k)  {
	        while (k > 1 && greater(k/2, k)) {
	            exch(k, k/2);
	            k = k/2;
	        }
	    }

	    private void sink(int k) {
	        while (2*k <= N) {
	            int j = 2*k;
	            if (j < N && greater(j, j+1)) j++;
	            if (!greater(k, j)) break;
	            exch(k, j);
	            k = j;
	        }
	    }


	   /***********************************************************************
	    * Iterators
	    **********************************************************************/

	    /**
	     * Returns an iterator that iterates over the keys on the
	     * priority queue in ascending order.
	     * The iterator doesn't implement <tt>remove()</tt> since it's optional.
	     * @return an iterator that iterates over the keys in ascending order
	     */
	    public Iterator<Integer> iterator() { return new HeapIterator(); }

	    private class HeapIterator implements Iterator<Integer> {
	        // create a new pq
	        private IndexMinPQ<Key> copy;

	        // add all elements to copy of heap
	        // takes linear time since already in heap order so no keys move
	        public HeapIterator() {
	            copy = new IndexMinPQ<Key>(pq.length - 1);
	            for (int i = 1; i <= N; i++)
	                copy.insert(pq[i], keys[pq[i]]);
	        }

	        public boolean hasNext()  { return !copy.isEmpty();                     }
	        public void remove()      { throw new UnsupportedOperationException();  }

	        public Integer next() {
	            if (!hasNext()) throw new NoSuchElementException();
	            return copy.delMin();
	        }
	    }


	    /*
	    public static void main(String[] args) {
	        // insert a bunch of strings
	        String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };

	        IndexMinPQ<String> pq = new IndexMinPQ<String>(strings.length);
	        for (int i = 0; i < strings.length; i++) {
	            pq.insert(i, strings[i]);
	        }

	        // delete and print each key
	        while (!pq.isEmpty()) {
	            int i = pq.delMin();
	            StdOut.println(i + " " + strings[i]);
	        }
	        StdOut.println();

	        // reinsert the same strings
	        for (int i = 0; i < strings.length; i++) {
	            pq.insert(i, strings[i]);
	        }

	        // print each key using the iterator
	        for (int i : pq) {
	            StdOut.println(i + " " + strings[i]);
	        }
	        while (!pq.isEmpty()) {
	            pq.delMin();
	        }

	    }*/
	}
	
	// Code for edge weighted digraph from book
	public static class EdgeWeightedDigraph {
	    private final int V;
	    private int E;
	    private Bag<DirectedEdge>[] adj;
	    
	    /**
	     * Initializes an empty edge-weighted digraph with <tt>V</tt> vertices and 0 edges.
	     * param V the number of vertices
	     * @throws java.lang.IllegalArgumentException if <tt>V</tt> < 0
	     */
	    public EdgeWeightedDigraph(int V) {
	        if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
	        this.V = V;
	        this.E = 0;
	        adj = (Bag<DirectedEdge>[]) new Bag[V];
	        for (int v = 0; v < V; v++)
	            adj[v] = new Bag<DirectedEdge>();
	    }

	    /**
	     * Initializes a random edge-weighted digraph with <tt>V</tt> vertices and <em>E</em> edges.
	     * param V the number of vertices
	     * param E the number of edges
	     * @throws java.lang.IllegalArgumentException if <tt>V</tt> < 0
	     * @throws java.lang.IllegalArgumentException if <tt>E</tt> < 0
	     */
	    public EdgeWeightedDigraph(int V, int E) {
	        this(V);
	        if (E < 0) throw new IllegalArgumentException("Number of edges in a Digraph must be nonnegative");
	        for (int i = 0; i < E; i++) {
	            int v = (int) (Math.random() * V);
	            int w = (int) (Math.random() * V);
	            double weight = Math.round(100 * Math.random()) / 100.0;
	            DirectedEdge e = new DirectedEdge(v, w, weight);
	            addEdge(e);
	        }
	    }

	    /**
	     * Initializes a new edge-weighted digraph that is a deep copy of <tt>G</tt>.
	     * @param G the edge-weighted graph to copy
	     */
	    public EdgeWeightedDigraph(EdgeWeightedDigraph G) {
	        this(G.V());
	        this.E = G.E();
	        for (int v = 0; v < G.V(); v++) {
	            // reverse so that adjacency list is in same order as original
	            Stack<DirectedEdge> reverse = new Stack<DirectedEdge>();
	            for (DirectedEdge e : G.adj[v]) {
	                reverse.push(e);
	            }
	            for (DirectedEdge e : reverse) {
	                adj[v].add(e);
	            }
	        }
	    }

	    /**
	     * Returns the number of vertices in the edge-weighted digraph.
	     * @return the number of vertices in the edge-weighted digraph
	     */
	    public int V() {
	        return V;
	    }

	    /**
	     * Returns the number of edges in the edge-weighted digraph.
	     * @return the number of edges in the edge-weighted digraph
	     */
	    public int E() {
	        return E;
	    }

	    // throw an IndexOutOfBoundsException unless 0 <= v < V
	    private void validateVertex(int v) {
	        if (v < 0 || v >= V)
	            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
	    }

	    /**
	     * Adds the directed edge <tt>e</tt> to the edge-weighted digraph.
	     * @param e the edge
	     * @throws java.lang.IndexOutOfBoundsException unless endpoints of edge are between 0 and V-1
	     */
	    public void addEdge(DirectedEdge e) {
	        int v = e.from();
	        int w = e.to();
	        validateVertex(v);
	        validateVertex(w);
	        adj[v].add(e);
	        E++;
	    }


	    /**
	     * Returns the directed edges incident from vertex <tt>v</tt>.
	     * @return the directed edges incident from vertex <tt>v</tt> as an Iterable
	     * @param v the vertex
	     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
	     */
	    public Iterable<DirectedEdge> adj(int v) {
	        validateVertex(v);
	        return adj[v];
	    }

	    /**
	     * Returns the number of directed edges incident from vertex <tt>v</tt>.
	     * This is known as the <em>outdegree</em> of vertex <tt>v</tt>.
	     * @return the outdegree of vertex <tt>v</tt>
	     * @param v the vertex
	     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
	     */
	    public int outdegree(int v) {
	        validateVertex(v);
	        return adj[v].size();
	    }

	    /**
	     * Returns all directed edges in the edge-weighted digraph.
	     * To iterate over the edges in the edge-weighted graph, use foreach notation:
	     * <tt>for (DirectedEdge e : G.edges())</tt>.
	     * @return all edges in the edge-weighted graph as an Iterable.
	     */
	    public Iterable<DirectedEdge> edges() {
	        Bag<DirectedEdge> list = new Bag<DirectedEdge>();
	        for (int v = 0; v < V; v++) {
	            for (DirectedEdge e : adj(v)) {
	                list.add(e);
	            }
	        }
	        return list;
	    } 

	    /**
	     * Returns a string representation of the edge-weighted digraph.
	     * This method takes time proportional to <em>E</em> + <em>V</em>.
	     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
	     *   followed by the <em>V</em> adjacency lists of edges
	     */
	    public String toString() {
	        String NEWLINE = System.getProperty("line.separator");
	        StringBuilder s = new StringBuilder();
	        s.append(V + " " + E + NEWLINE);
	        for (int v = 0; v < V; v++) {
	            s.append(v + ": ");
	            for (DirectedEdge e : adj[v]) {
	                s.append(e + "  ");
	            }
	            s.append(NEWLINE);
	        }
	        return s.toString();
	    }

	    /*
	    public static void main(String[] args) {
	        In in = new In(args[0]);
	        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
	        StdOut.println(G);
	    }*/

	}

	// BFS Path algorithm from textbook
	public static class BreadthFirstPaths {
	    private static final int INFINITY = Integer.MAX_VALUE;
	    private boolean[] marked;  // marked[v] = is there an s-v path
	    private int[] edgeTo;      // edgeTo[v] = previous edge on shortest s-v path
	    private int[] distTo;      // distTo[v] = number of edges shortest s-v path

	    /**
	     * Computes the shortest path between the source vertex <tt>s</tt>
	     * and every other vertex in the graph <tt>G</tt>.
	     * @param G the graph
	     * @param s the source vertex
	     */
	    public BreadthFirstPaths(Graph G, int s) {
	        marked = new boolean[G.V()];
	        distTo = new int[G.V()];
	        edgeTo = new int[G.V()];
	        bfs(G, s);

	        assert check(G, s);
	    }

	    /**
	     * Computes the shortest path between any one of the source vertices in <tt>sources</tt>
	     * and every other vertex in graph <tt>G</tt>.
	     * @param G the graph
	     * @param sources the source vertices
	     */
	    public BreadthFirstPaths(Graph G, Iterable<Integer> sources) {
	        marked = new boolean[G.V()];
	        distTo = new int[G.V()];
	        edgeTo = new int[G.V()];
	        for (int v = 0; v < G.V(); v++) distTo[v] = INFINITY;
	        bfs(G, sources);
	    }


	    // breadth-first search from a single source
	    private void bfs(Graph G, int s) {
	        Queue<Integer> q = new Queue<Integer>();
	        for (int v = 0; v < G.V(); v++) distTo[v] = INFINITY;
	        distTo[s] = 0;
	        marked[s] = true;
	        q.enqueue(s);

	        while (!q.isEmpty()) {
	            int v = q.dequeue();
	            for (int w : G.adj(v)) {
	                if (!marked[w]) {
	                    edgeTo[w] = v;
	                    distTo[w] = distTo[v] + 1;
	                    marked[w] = true;
	                    q.enqueue(w);
	                }
	            }
	        }
	    }

	    // breadth-first search from multiple sources
	    private void bfs(Graph G, Iterable<Integer> sources) {
	        Queue<Integer> q = new Queue<Integer>();
	        for (int s : sources) {
	            marked[s] = true;
	            distTo[s] = 0;
	            q.enqueue(s);
	        }
	        while (!q.isEmpty()) {
	            int v = q.dequeue();
	            for (int w : G.adj(v)) {
	                if (!marked[w]) {
	                    edgeTo[w] = v;
	                    distTo[w] = distTo[v] + 1;
	                    marked[w] = true;
	                    q.enqueue(w);
	                }
	            }
	        }
	    }

	    /**
	     * Is there a path between the source vertex <tt>s</tt> (or sources) and vertex <tt>v</tt>?
	     * @param v the vertex
	     * @return <tt>true</tt> if there is a path, and <tt>false</tt> otherwise
	     */
	    public boolean hasPathTo(int v) {
	        return marked[v];
	    }

	    /**
	     * Returns the number of edges in a shortest path between the source vertex <tt>s</tt>
	     * (or sources) and vertex <tt>v</tt>?
	     * @param v the vertex
	     * @return the number of edges in a shortest path
	     */
	    public int distTo(int v) {
	        return distTo[v];
	    }

	    /**
	     * Returns a shortest path between the source vertex <tt>s</tt> (or sources)
	     * and <tt>v</tt>, or <tt>null</tt> if no such path.
	     * @param v the vertex
	     * @return the sequence of vertices on a shortest path, as an Iterable
	     */
	    public Iterable<Integer> pathTo(int v) {
	        if (!hasPathTo(v)) return null;
	        Stack<Integer> path = new Stack<Integer>();
	        int x;
	        for (x = v; distTo[x] != 0; x = edgeTo[x])
	            path.push(x);
	        path.push(x);
	        return path;
	    }


	    // check optimality conditions for single source
	    private boolean check(Graph G, int s) {

	        // check that the distance of s = 0
	        if (distTo[s] != 0) {
	        	System.out.println("distance of source " + s + " to itself = " + distTo[s]);
	            return false;
	        }

	        // check that for each edge v-w dist[w] <= dist[v] + 1
	        // provided v is reachable from s
	        for (int v = 0; v < G.V(); v++) {
	            for (int w : G.adj(v)) {
	                if (hasPathTo(v) != hasPathTo(w)) {
	                	System.out.println("edge " + v + "-" + w);
	                	System.out.println("hasPathTo(" + v + ") = " + hasPathTo(v));
	                	System.out.println("hasPathTo(" + w + ") = " + hasPathTo(w));
	                    return false;
	                }
	                if (hasPathTo(v) && (distTo[w] > distTo[v] + 1)) {
	                	System.out.println("edge " + v + "-" + w);
	                	System.out.println("distTo[" + v + "] = " + distTo[v]);
	                	System.out.println("distTo[" + w + "] = " + distTo[w]);
	                    return false;
	                }
	            }
	        }

	        // check that v = edgeTo[w] satisfies distTo[w] + distTo[v] + 1
	        // provided v is reachable from s
	        for (int w = 0; w < G.V(); w++) {
	            if (!hasPathTo(w) || w == s) continue;
	            int v = edgeTo[w];
	            if (distTo[w] != distTo[v] + 1) {
	            	System.out.println("shortest path edge " + v + "-" + w);
	            	System.out.println("distTo[" + v + "] = " + distTo[v]);
	                System.out.println("distTo[" + w + "] = " + distTo[w]);
	                return false;
	            }
	        }

	        return true;
	    }

	    /*
	    public static void main(String[] args) {
	        In in = new In(args[0]);
	        Graph G = new Graph(in);
	        // StdOut.println(G);

	        int s = Integer.parseInt(args[1]);
	        BreadthFirstPaths bfs = new BreadthFirstPaths(G, s);

	        for (int v = 0; v < G.V(); v++) {
	            if (bfs.hasPathTo(v)) {
	                StdOut.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
	                for (int x : bfs.pathTo(v)) {
	                    if (x == s) StdOut.print(x);
	                    else        StdOut.print("-" + x);
	                }
	                StdOut.println();
	            }

	            else {
	                StdOut.printf("%d to %d (-):  not connected\n", s, v);
	            }

	        }
	    }*/


	}
	
	// Generic graph code form text book
	public static class Graph {
	    private final int V;
	    private int E;
	    private Bag<Integer>[] adj;
	    
	    /**
	     * Initializes an empty graph with <tt>V</tt> vertices and 0 edges.
	     * param V the number of vertices
	     * @throws java.lang.IllegalArgumentException if <tt>V</tt> < 0
	     */
	    public Graph(int V) {
	        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
	        this.V = V;
	        this.E = 0;
	        adj = (Bag<Integer>[]) new Bag[V];
	        for (int v = 0; v < V; v++) {
	            adj[v] = new Bag<Integer>();
	        }
	    }
	    
	    /**
	     * Initializes a new graph that is a deep copy of <tt>G</tt>.
	     * @param G the graph to copy
	     */
	    public Graph(Graph G) {
	        this(G.V());
	        this.E = G.E();
	        for (int v = 0; v < G.V(); v++) {
	            // reverse so that adjacency list is in same order as original
	            Stack<Integer> reverse = new Stack<Integer>();
	            for (int w : G.adj[v]) {
	                reverse.push(w);
	            }
	            for (int w : reverse) {
	                adj[v].add(w);
	            }
	        }
	    }

	    /**
	     * Returns the number of vertices in the graph.
	     * @return the number of vertices in the graph
	     */
	    public int V() {
	        return V;
	    }

	    /**
	     * Returns the number of edges in the graph.
	     * @return the number of edges in the graph
	     */
	    public int E() {
	        return E;
	    }

	    // throw an IndexOutOfBoundsException unless 0 <= v < V
	    private void validateVertex(int v) {
	        if (v < 0 || v >= V)
	            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
	    }

	    /**
	     * Adds the undirected edge v-w to the graph.
	     * @param v one vertex in the edge
	     * @param w the other vertex in the edge
	     * @throws java.lang.IndexOutOfBoundsException unless both 0 <= v < V and 0 <= w < V
	     */
	    public void addEdge(int v, int w) {
	        validateVertex(v);
	        validateVertex(w);
	        E++;
	        adj[v].add(w);
	        adj[w].add(v);
	    }


	    /**
	     * Returns the vertices adjacent to vertex <tt>v</tt>.
	     * @return the vertices adjacent to vertex <tt>v</tt> as an Iterable
	     * @param v the vertex
	     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
	     */
	    public Iterable<Integer> adj(int v) {
	        validateVertex(v);
	        return adj[v];
	    }

	    /**
	     * Returns the degree of vertex <tt>v</tt>.
	     * @return the degree of vertex <tt>v</tt>
	     * @param v the vertex
	     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
	     */
	    public int degree(int v) {
	        validateVertex(v);
	        return adj[v].size();
	    }


	    /**
	     * Returns a string representation of the graph.
	     * This method takes time proportional to <em>E</em> + <em>V</em>.
	     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
	     *    followed by the <em>V</em> adjacency lists
	     */
	    public String toString() {
	        StringBuilder s = new StringBuilder();
	        String NEWLINE = System.getProperty("line.separator");
	        s.append(V + " vertices, " + E + " edges " + NEWLINE);
	        for (int v = 0; v < V; v++) {
	            s.append(v + ": ");
	            for (int w : adj[v]) {
	                s.append(w + " ");
	            }
	            s.append(NEWLINE);
	        }
	        return s.toString();
	    }


	    /*
	    public static void main(String[] args) {
	        In in = new In(args[0]);
	        Graph G = new Graph(in);
	        StdOut.println(G);
	    }*/

	}
	
	
	
	
	
	
	
			
}
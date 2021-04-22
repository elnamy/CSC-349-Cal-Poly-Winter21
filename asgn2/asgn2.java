import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class asgn2 {
    private static TreeMap<Integer, List<Integer>> adjacencyList = new TreeMap<>();
    private static TreeMap<Integer, List<Integer>> adjListReversed = new TreeMap<>();
    private static Stack<Integer> stack = new Stack<>();
    //List of List to hold the Strongly Connected Components found in the graph
    private static List<List<Integer>> SCCs = new ArrayList<>();
    //Counter for total number of strongly connected components in graph
    private static int count = 0;

    //Function to establish connections between the vertices
    public static void addEdge(int source, int destination)
    {
        adjacencyList.get(source).add(destination);
    }
    //Single vertex case
    public static void addEdge(int source)
    {
        adjacencyList.get(source).add(source);
    }

    public static void addReversedEdge(int source, int destination) {
            adjListReversed.get(source).add(destination);
    }

    public static void showSCCs() {
        Collections.sort(SCCs, new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> arg0, List<Integer> arg1) {
                return arg0.get(0).compareTo(arg1.get(0));//sort based on the first element from each line
            }
        });
        System.out.println(count + " Strongly Connected Component(s):");
        SCCs.forEach((value) -> System.out.println(value.toString().replace("[", "").replace("]","")));
    }

    public static List<Integer> secondDFS(int vertex, boolean visited[], List<Integer> connectedVertices) {
        visited[vertex] = true;
        //add to List the current vertex
        connectedVertices.add(vertex);
        for(Integer current : adjListReversed.get(vertex)) {
            if(!visited[current])
                secondDFS(current, visited, connectedVertices);
        }
        return connectedVertices;
    }

    public static void reverseGraph() {
        for(int i=0; i < adjacencyList.size(); i++) {
            for(int j=0; j < adjacencyList.get(i).size(); j++)
                addReversedEdge(adjacencyList.get(i).get(j), i);
        }
    }
    //first DFS to generate stack
    public static void firstDFS(int vertex, boolean[] visited) {
        visited[vertex] = true;
        for(Integer current : adjacencyList.get(vertex)) {
            if(!visited[current])
                firstDFS(current, visited);
        }
        stack.push(vertex);
    }

    public static void UtilFunction() {
        boolean [] visited = new boolean[adjacencyList.size()];
        for(int vertex = 0; vertex < adjacencyList.size(); vertex++) {
            if(!visited[vertex])
                firstDFS(vertex, visited);
        }
        reverseGraph();
        //clear visited array previous values
        for (int vertex = 0; vertex < adjacencyList.size(); vertex++)
            visited[vertex] = false;
        //Iterate over previously generated stack
        while(!stack.empty()) {
            int v = stack.pop();

            if(!visited[v]) {
                ArrayList<Integer> connectedVertices = new ArrayList<>();
                secondDFS(v, visited, connectedVertices);
                Collections.sort(connectedVertices);
                SCCs.add(connectedVertices);
                count++;
            }
        }
        showSCCs();
    }

    public static void load(String file) {
        //used to determine the total number of vertices of given graph
        TreeSet<Integer> vertices = new TreeSet<>();

        ///////////READ VERTICES 1st/////////////////////////////////////////////////////////////////
        try {
            Scanner inputFile = new Scanner(new File(file));
            while(inputFile.hasNext()) {
                String line = inputFile.nextLine();
                //break the string line based on commas and spaces
                String[] parts = line.split(", ");

                if(parts.length == 2) {//must be a pair of vertices
                    vertices.add(Integer.parseInt(parts[0]));
                    vertices.add(Integer.parseInt(parts[1]));
                } else {//must be a single vertex
                    vertices.add(Integer.parseInt(parts[0]));
                }
            }
            inputFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }

        for(Integer i : vertices)
            adjacencyList.put(i, new LinkedList<>());

        for(Integer i : vertices)
            adjListReversed.put(i, new LinkedList<>());

        ///////////READ EDGES NOW/////////////////////////////////////////////////////////////////
        try {
            Scanner inputFile = new Scanner(new File(file));
            while(inputFile.hasNext()) {
                String line = inputFile.nextLine();
                //break the string line based on commas and spaces
                String[] parts = line.split(", ");

                if(parts.length == 2) {
                    int source = Integer.parseInt(parts[0]);
                    int destination = Integer.parseInt(parts[1]);
                    //must add in ONLY one direction for (directed graph)
                    addEdge(source, destination);//pair of vertices
                } else {
                    int source = Integer.parseInt(parts[0]);
                    addEdge(source);//single vertex
                }
            }
            inputFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
    public static void main(String[] args) {
        if(args.length > 0) {
            load(args[0]);
            UtilFunction();
        } else
            System.exit(0);
    }
}

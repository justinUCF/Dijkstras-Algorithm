import java.io.*;
import java.util.*;
public class DijkstraAlgorithm {
    public static void main(String[] args){
        String inputFile = "cop3503-dijkstra-input.txt";
        String outputFile = "cop3503-dijkstra-output-gamboa-justin.txt";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
        BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            //Read first 3 lines from input file
            int vertices = Integer.parseInt(br.readLine());
            int sourceVertex = Integer.parseInt(br.readLine());
            int edges = Integer.parseInt(br.readLine());
            //Create new Adjacenymatrix
            AdjacenyMatrix graph = new AdjacenyMatrix(vertices, edges);
            //Read each edge from inputFile and update Graph
            for (int i = 0; i < graph.edges; i++) {
                String[] edgeData = br.readLine().split("\\s+");
                int vertex1 = Integer.parseInt(edgeData[0]) - 1;
                int vertex2 = Integer.parseInt(edgeData[1]) - 1;
                int weight = Integer.parseInt(edgeData[2]);
                //Undirected Graph
                graph.updateEdge(vertex1, vertex2, weight);
            }
            //Preform Dijstra's Algorithm and return InfoChart
            InfoChart info = graph.dijkstraAlgorithm(sourceVertex-1);
            info.updateIndex(sourceVertex-1, -1, -1, 1);
            //Write the InfoChart into outputFile
            bw.write(vertices+"\n"+info.toString());

            br.close();
            bw.close();            

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static class AdjacenyMatrix {
        public int vertices;
        public int edges;
        public int[][] adjacenyMatrix;
        
        public AdjacenyMatrix(int vertices, int edges){
            this.vertices = vertices;
            this.edges = edges;
            this.adjacenyMatrix = new int[vertices][vertices];
        }
        //Updates edge between two vertices
        public void updateEdge(int v1, int v2, int weight){
            adjacenyMatrix[v1][v2] = weight;
            adjacenyMatrix[v2][v1] = weight;
        }
        //Wrapper Function for Dijkstra's Algorithm
        public InfoChart dijkstraAlgorithm(int source){
            InfoChart info = new InfoChart(vertices);
            //Set Source vertex DIST to 0,PREV to -1, VIST to 0
            info.updateIndex(source,0,-1,0);
            //Call Recursive function return InfoChart
            return dijkstraAlgorithmRec(source, info);
        }
        //Preferms Dijstra's Algorithm on the given Source vertex
        //Returns an InforChart
        public InfoChart dijkstraAlgorithmRec(int vertex, InfoChart info){
            //If all vertices visited, return InfoChart
            if(vertex == -1) return info;
            //Update DIST for all adjacent vertex
            for(int i = 0; i < adjacenyMatrix[vertex].length; i++){
                if(adjacenyMatrix[vertex][i] != 0){//If edge exists
                    int oldDistance = info.getDist(i);
                    int newDistance = info.getDist(vertex) + adjacenyMatrix[vertex][i];
                    if(newDistance < oldDistance){
                        info.setDist(i, newDistance);
                        info.setPrev(i, vertex+1);
                    }
                }
            }
            info.setVist(vertex, 1);
            //Determine next Vertex based on DIST from current Vertex
            int nextVertex = -1;
            for(int i = 0; i < adjacenyMatrix[vertex].length; i++){
                if((adjacenyMatrix[vertex][i] != 0) && (info.getVist(i) != 1)){
                    if(nextVertex == -1) nextVertex = i;
                    else{
                        if(info.getDist(i) < info.getDist(nextVertex)){
                            nextVertex = i;
                        }
                    }
                }
            }
            //Recurse
            return dijkstraAlgorithmRec(nextVertex, info);
        }   
    }

    public static class InfoChart{
        private static final int DIST = 0; 
        private static final int PREV = 1;
        private static final int VIST = 2;
        private int[][] info;
        
        public InfoChart(int vertices){
            this.info = new int[vertices][3];
            for(int[] row:info){
                row[0] = Integer.MAX_VALUE;
            }
        }

        public void updateIndex(int index, int dist, int prev, int vist){
            info[index][DIST] = dist;
            info[index][PREV] = prev;
            info[index][VIST] = vist;
        }

        public int getDist(int index){
            return info[index][DIST];
        }

        public int getPrev(int index){
            return info[index][PREV];
        }

        public int getVist(int index){
            return info[index][VIST];
        }

        public void setDist(int index, int dist){
            info[index][DIST] = dist;
        }

        public void setPrev(int index, int prev){
            info[index][PREV] = prev;
        }

        public void setVist(int index, int vist){
            info[index][VIST] = vist;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < info.length; i++){
                sb.append((i+1)+" "+info[i][DIST]+" "+info[i][PREV]);
                if((i+1) != info.length) sb.append("\n");
            }
            return sb.toString();
        }
    }

}
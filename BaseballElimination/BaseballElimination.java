import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.In;

/**
 * Created by Bradley Pham on 12/2/2017.
 */
public class BaseballElimination {

    private String[] teamName; //to store the team name to return
    private int[] win; //parallel array to store win games of index team
    private int[] lose; //parallel array to store lost games of index team
    private int[] remain; //parallel array to store remaining games of index team
    private int[][] schedule; //matrix showing matches between teams
    private int numOfTeam;
    private FordFulkerson[] graphsElimination; //to store each FordFulkerson for each team
    private int[] edgeCapacity; //to store capacity of edges to compare with the max flow
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        //Initialise data structure
        In input = new In(filename);
        numOfTeam = Integer.parseInt(input.readLine());
        teamName = new String[numOfTeam];
        win = new int[numOfTeam];
        lose = new int[numOfTeam];
        remain = new int[numOfTeam];
        schedule = new int[numOfTeam][numOfTeam];
        graphsElimination = new FordFulkerson[numOfTeam];
        edgeCapacity = new int[numOfTeam];
        //reading data into structure
        for (int row = 0; row < numOfTeam; row++) {
            String line = input.readLine();
            line = line.trim();
            String[] breakLine = line.split("\\s+"); //\\s+: one or more white spaces
            teamName[row] = breakLine[0];
            win[row] = Integer.parseInt(breakLine[1]);
            lose[row] = Integer.parseInt(breakLine[2]);
            remain[row] = Integer.parseInt(breakLine[3]);
            //creating matrix columns
            int indexBreakLine = 4;
            for (int col = 0; col < numOfTeam; col++) {
                schedule[row][col] = Integer.parseInt(breakLine[indexBreakLine]);
                indexBreakLine++;
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return numOfTeam;
    }
    // all teams
    public Iterable<String> teams() {
        Queue<String> result = new Queue<>();
        for (int i = 0; i < numOfTeam; i++) {
            result.enqueue(teamName[i]);
        }
        return result;
    }
    // number of wins for given team
    public int wins(String team) {
        if (team == null)
            throw new IllegalArgumentException();
        int queryIndex = teamIndex(team);
        if (queryIndex == -1)
            throw new IllegalArgumentException();
        else {
            return win[queryIndex];
        }
    }
    // number of losses for given team
    public int losses(String team) {
        if (team == null)
            throw new IllegalArgumentException();
        int queryIndex = teamIndex(team);
        if (queryIndex == -1)
            throw new IllegalArgumentException();
        else {
            return lose[queryIndex];
        }
    }
    // number of remaining games for given team
    public int remaining(String team) {
        if (team == null)
            throw new IllegalArgumentException();
        int queryIndex = teamIndex(team);
        if (queryIndex == -1)
            throw new IllegalArgumentException();
        else {
            return remain[queryIndex];
        }
    }
    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (team1 == null || team2 == null)
            throw new IllegalArgumentException();
        int indexTeam1 = -1;
        int indexTeam2 = -1;
        for (int i = 0; i< numOfTeam; i++) {
            if (teamName[i].equals(team1))
                indexTeam1 = i;
            if (teamName[i].equals(team2))
                indexTeam2 = i;
        }
        if (indexTeam1 == -1 || indexTeam2 == -1)
            throw new IllegalArgumentException();
        return schedule[indexTeam1][indexTeam2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (team == null)
            throw new IllegalArgumentException();
        int queryIndex = teamIndex(team);
        if (queryIndex == -1)
            throw new IllegalArgumentException();
        //if the team is trivial eliminated
        if (isTrivialEliminated(queryIndex) != -1) {
            //System.out.println("Trivial");
            return true;
        }
        //to check if the graphs are already created through calling isEliminated() or certificateOfElimination
        if (graphsElimination[queryIndex] == null) {
            //constructing the graph for queried team
            FlowNetwork graph = createFlowGraph(schedule, queryIndex);
//            System.out.println(graph.toString());
            graphsElimination[queryIndex] = new FordFulkerson(graph, graph.V() - 2, graph.V() - 1);
        }
        return edgeCapacity[queryIndex] > graphsElimination[queryIndex].value();
    }
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (team == null)
            throw new IllegalArgumentException();
        int queryIndex = teamIndex(team);
        if (queryIndex == -1)
            throw new IllegalArgumentException();
        Queue<String> result = new Queue<>();
        int isTrivial = isTrivialEliminated(queryIndex);
        if (isTrivial != -1) {
            //System.out.println("Trivial");
            result.enqueue(teamName[isTrivial]);
            return result;
        }
        //to check if the graphs are already created through calling isEliminated() or certificateOfElimination
        if (graphsElimination[queryIndex] == null) {
            //constructing the graph for queried team
            FlowNetwork graph = createFlowGraph(schedule, queryIndex);
            graphsElimination[queryIndex] = new FordFulkerson(graph, graph.V() - 2, graph.V() - 1);

        }

        for (int i = 0; i < numOfTeam; i++) {
            if (graphsElimination[queryIndex].inCut(i + numOfTeam +choose(numOfTeam-1))) {
                result.enqueue(teamName[i]);
            }
        }
        if (result.size() == 0)
            return null;
        return result;
    }

    //-----------------HELPER METHODS--------------------------------
    private int choose(int totalChoice) {
        return ((totalChoice)*(totalChoice - 1)) / 2;
    }
    //to check if the team is in division
    //return the index if yes, -1 otherwise
    private int teamIndex(String team) {
        for (int i = 0; i < numOfTeam; i++) {
            if (teamName[i].equals(team)) {
                return i;
            }
        }
        return -1;
    }
    private FlowNetwork createFlowGraph(int[][] matrix, int team) {
        //each graph will have 2 conceptual points: s and t plus number of teams plus ways to pick 2 teams out
        //of total number of team vertices
        int choices = choose(matrix.length - 1);
        FlowNetwork graphTeam = new FlowNetwork(2 * matrix.length + choices + 2);
        int currentV = numOfTeam;
        int capacity = 0;
        int src = graphTeam.V() - 2; //src node
        int des = graphTeam.V() - 1; //destination node
        for (int row = 0; row < numOfTeam; row++) {
            if (row == team)
                continue;
            FlowEdge edge;
            for (int col = row + 1; col < numOfTeam; col++) {
                if(col == team)
                    continue;
                //add edges from one team to the other to represent matches among them
                edge = new FlowEdge(row, currentV, schedule[row][col]);
                capacity += schedule[row][col];
                graphTeam.addEdge(edge);
                //mimic edges from source to node representing team involved in a match (ex: team 0 with 1 2 3: connect from src
                //to 1, to 2, to 3. This is to take out the min cut
                edge = new FlowEdge(src, currentV,schedule[row][col]);
                graphTeam.addEdge(edge);
                //add edges to two nodes involved in the match (1-2) to 1 and 2
                edge = new FlowEdge(currentV, row + numOfTeam + choices, Double.POSITIVE_INFINITY);
                graphTeam.addEdge(edge);
                edge = new FlowEdge(currentV, numOfTeam + choices + col, Double.POSITIVE_INFINITY);
                graphTeam.addEdge(edge);
                currentV++;
            }
        }
        edgeCapacity[team] = capacity;
        for (int i = 0; i < numOfTeam; i++) {
            if (i == team)
                continue;
            //create edges to destination
            FlowEdge in = new FlowEdge(i + numOfTeam + choices, des, win[team] + remain[team] - win[i]);
            graphTeam.addEdge(in);
        }
        return graphTeam;
    }
    //if trivial eliminated, return the index that makes the team eliminated, otherwise, return -1
    private int isTrivialEliminated(int team) {
        for (int i = 0; i < numOfTeam; i++) {
            if ((win[team] + remain[team]) < win[i]) {
                return i;
            }
        }
        return -1;
    }
    //----------------------------------------------------------------

    public static void main(String[] args) {
//        Long begin = System.currentTimeMillis();
        BaseballElimination division = new BaseballElimination(args[0]);
//        Long end = System.currentTimeMillis();
//        System.out.printf("Construction time: %.2f\n", (end - begin)/1000.0);
//        Long beginEli = System.currentTimeMillis();
        for (String team : division.teams()) {
//            String team = "Detroit";
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                    StdOut.print(t + " ");
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
        System.out.println(division.against("New_York", "Boston"));
//        Long endEli = System.currentTimeMillis();
//        System.out.printf("Elimination time: %.2f", (endEli - beginEli)/1000.0);

    }
}

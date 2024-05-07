import java.util.LinkedList;
import java.util.Queue;

public class BFSTCells {
   static class Cell {
      int row;
      int col;
   Cell(int row, int col) {
      this.row = row;
      this.col = col;
      }
   }

   public static void breadthFirstSearch(int[][] board, int dimension, int start_i, int start_j) {
      int rows = dimension;
      int cols = rows;
      boolean[][] visited = new boolean[rows][cols];
      
      // Define the directions for exploring neighbors (up, down, left, right, right top-corner, left top-corner, right bottom-corner, left bottom-corner)
      int[] dr = {0, 0, 1, -1};
      int[] dc = {1, -1, 0, 0};
      
   
   Queue<Cell> queue = new LinkedList<>();
   
   queue.add(new Cell(start_i, start_j));
   visited[start_i][start_j] = true;
   
   while (!queue.isEmpty()) {
      Cell currentCell = queue.poll();
      int i = currentCell.row    ;
      int j = currentCell.col;
      System.out.println("Visiting cell at (" + i + ", " + j + ")"); // Print the visited cell
      
      for (int k = 0; k < 4; k++) { // Four directions: up, down, left, right
         int ni = i + dr[k];
         int nj = j + dc[k];
         
      // Check if the new cell (ni, nj) is within the grid boundaries
         if (ni >= 0 && ni < rows && nj >= 0 && nj < cols && !visited[ni][nj]) { 
            queue.add(new Cell(ni, nj));
            visited[ni][nj] = true;
            }
         }
      }
   }
   public static void main(String[] args) {
      int[][] grid = {
         {1, 0, 1, 0},
         {0, 1, 0, 1},
         {1, 0, 1, 0}
   };
      int start_i = 1;
      int start_j = 2;
      
      int dimension = 10;
      System.out.println("Starting BFS traversal from cell (1, 2) in the 2D array.");
      breadthFirstSearch(grid, dimension, start_i, start_j);
   }
}
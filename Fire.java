//this class creates fire when called on in Disaster
public class Fire extends Flow{

	public Fire(int row, int column, int startR, int startC, double initialAmount) {
	    super("Fire",0.5,startR,startC,initialAmount, row, column, 1.0,0.1,0.1);
	    // name, rate_of_flow, starting row and column and initial amount there, total number of rows and columns, and red, green and blue
	}
	
	@Override
	public void update(double[][] elevation){ 
		//System.out.println(" hi");
		amount[startR][startC] = 1;
		for (int r = 1; r < row-1; r++) {
		    for (int c = 1; c < column-1; c++) {
		        for (int rNear = r-1; rNear < r+2; rNear++) {
		            for (int cNear = c-1; cNear < c+2; cNear++) {
		            	if(amount[r][c] > 0){
		            		if(elevation[rNear][cNear] >= elevation[r][c]){
		            			amount[rNear][cNear] = 1;
		            		}
		            	}

		                // update amount[rNear][cNear] here

		            }
		         }
		    }
		 }
		
	}

	@Override
	public void startAt(int startRow, int startColumn) {
		amount[startRow][startColumn] = 1;
		
	}

}

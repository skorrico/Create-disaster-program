//this class is another disaster but its a surprise
public class newProblem extends Flow{
	
	public newProblem(int row, int column, int startR, int startC, double initialAmount) {
	    super("Locust Swarm Invasion",0.5,startR,startC,initialAmount, row,column, 0.1,1.0,0.1);
	    // name, rate_of_flow, starting row and column and initial amount there, total number of rows and columns, and red, green and blue
	}

	@Override
	public void startAt(int startRow, int startColumn) {
		amount[startRow][startColumn] = 1;
	}

	@Override
	public void update(double[][] elevation) {
		amount[startR][startC] = 3;
		for (int r = 1; r < row-1; r++) {
		    for (int c = 1; c < column-1; c++) {
		        for (int rNear = r-1; rNear < r+2; rNear++) {
		            for (int cNear = c-1; cNear < c+2; cNear++) {
		            	double difference = (elevation[r][c] + amount[r][c]) - (elevation[rNear][cNear] + amount[rNear][cNear]);
		            	if(difference > 0){
		            		if((difference * flowRate) > amount[r][c]){
		            		amount[r][c] += (amount[rNear][cNear] * flowRate);
		            		amount[rNear][cNear] -= amount[r][c] ;
		            		}
		            	else{
		            		amount[r][c] += (flowRate * 0.2);
		            		amount[rNear][cNear] -= flowRate;
		            	}
		            	}
		            }
		         }
		    }
		 }
	}

}

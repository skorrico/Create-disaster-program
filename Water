//this class creates water called in Disaster class
public class Water extends Flow{

	public Water(int row, int column, int startR, int startC, double initialAmount) {
	    super("Water",0.5,startR,startC,initialAmount, row, column, 0.1,0.1,1.0);
	    // name, rate_of_flow, starting row and column and initial amount there, total number of rows and columns, and red, green and blue
	}
	
	public void startAt(int startRow, int startColumn) {
		amount[startRow][startColumn] +=initialAmount;
		
	}
	
	@Override
	public void update(double[][] elevation) {
		amount[startR][startC] += initialAmount;
		for (int r = 1; r < row-1; r++) {
		    for (int c = 1; c < column-1; c++) {
		        for (int rNear = r-1; rNear < r+2; rNear++) {
		            for (int cNear = c-1; cNear < c+2; cNear++) {
		            	double difference = (elevation[r][c] + amount[r][c]) - (elevation[rNear][cNear] + amount[rNear][cNear]);
		            	if(difference > 0){
		            		if((difference * flowRate) > amount[r][c]){
		            		amount[r][c] -= (amount[r][c] * flowRate);
		            		amount[rNear][cNear] += amount[r][c] ;
		            		}
		            	else{
		            		amount[r][c] -= (flowRate * difference);
		            		amount[rNear][cNear] += (flowRate * difference);
		            	}
		            	}

		             }
		         }
		    }
		 }		
	}
	
	
}

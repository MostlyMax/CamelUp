import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Simulation {
	Game g;

	static HashMap<String, Integer> numWins;
	static HashMap<String, Integer> sumPlace;
	ArrayList<String> currentColors;
	
	
	public Simulation(Game g) {
		this.g = g;
		
		currentColors = new ArrayList<String>();
		currentColors.addAll(g.allColors);
		numWins = new HashMap<String, Integer>();//Keeps track of number of wins each camel gets
		sumPlace = new HashMap<>();
		
	}
	public void reset() {
		currentColors.clear();
		currentColors.addAll(g.allColors);
		start(null);
	}
	
	public void start(String delColor) {
		currentColors.remove(delColor);
		if(currentColors.isEmpty()) return;

		init();
		
		beginSimulate();
		System.out.println();
		
		//Just printing results
		System.out.println("|ORANGE||BLUE  ||YELLOW||GREEN ||WHITE |");
		//System.out.println(numWins);
		
		double magicNum = (wtfactorial(currentColors.size())*Math.pow(3, currentColors.size()));

		for(String s : g.allColors) {
			String output = String.format("|%6.3f|", ((double)(numWins.get(s)))/(magicNum/100));//calculates percentage of times won
			System.out.print(output);
		}
        System.out.println();
        for(String s : g.allColors) {
            String output = String.format("|%6.3f|", ((double)(sumPlace.get(s)))/magicNum);//calculates percentage of times won
            System.out.print(output);
        }
		System.out.println();
		
	}
	
	/***
	 * first simulation. Doesn't remove color
	 */
	@SuppressWarnings("unchecked")
	public void beginSimulate() {
		//colors!*3^colors
		HashMap<String, Camel> camelColors = cloneCamelColors(g.camelColors);
		HashMap<String, Camel> saveCamelColors = cloneCamelColors(camelColors);
		
		Board simBoard = new Board(camelColors.values());
		simBoard.updateLocations();
		
		ArrayList<String> colorList = new ArrayList<String>();
		colorList.addAll(currentColors);
	
		//System.out.println(colorList);
		
		for (String color : colorList) { //iterates through all colors
			
			
			//save(0);//creates a save of all camels and board in original positition
			
			
			
			for (int i=1; i<=3; i++) {//iterates through possible dice numbers
				Camel testCamel = camelColors.get(color);//gets test camel
				
				testCamel.move(i);//moves camel
				testCamel.stackOnTop(simBoard.locations[testCamel.tile]);//updates stack
				simBoard.updateLocations();//updates locations of camels
				
				HashMap<String,Camel> simCamelColors = cloneCamelColors(camelColors);
				System.out.print("*");
				simulate(1, (ArrayList<String>) colorList.clone(), simCamelColors,new Board(simCamelColors.values()), color);//begins recursive simulate
				System.out.print("*");
				
				camelColors = cloneCamelColors(saveCamelColors);
				simBoard = new Board(camelColors.values());
				simBoard.updateLocations();
				//revert(saves.get(0));//reverts camels and stuffs to original positions
			}
		
		}
	}
	
	/**
	 * recursive simulate
	 */
	@SuppressWarnings("unchecked")
	public void simulate(int simNum, ArrayList<String> simColors, HashMap<String, Camel> camelColors, Board simBoard, String delColor) {
		HashMap<String, Camel> saveCamelColors= cloneCamelColors(camelColors);
		simBoard.updateLocations();
		//System.out.println("Depth: "+simNum);
		simColors.remove(delColor);//removes previously used color
		
		if (simColors.isEmpty()) {//end condition (No more colors)
			
			findWinner(simBoard.camelList);//calculates current winner
            addPlaces(simBoard);
			return;
		}
			
		//System.out.println(simColors);
		
		//Camel saveCamel = new Camel("clone");//Creates new clone camel
		
		for (String color : simColors) {//iterates through remaining colors
			
			//save(simNum);//creates a save of all camels and board in original positition
			
			
			for (int i=1; i<=3; i++) {
				Camel testCamel = camelColors.get(color);
				
				testCamel.move(i);//moves camel
				testCamel.stackOnTop(simBoard.locations[testCamel.tile]);//updates stack
				simBoard.updateLocations();//updates locations of camels
				//g.printInfo();
				
				HashMap<String,Camel> simCamelColors = cloneCamelColors(camelColors);
				simulate(simNum+1, (ArrayList<String>) simColors.clone(),simCamelColors,new Board(simCamelColors.values()), color);//recursive simulate
				
				camelColors = cloneCamelColors(saveCamelColors);
				simBoard = new Board(camelColors.values());
				simBoard.updateLocations();
				//revert(saves.get(simNum));//reverts camels and stuffs to original positions
			}
		}
	}
	
	/**
	 * Finds current winner of simulation
	 */
	private void findWinner(ArrayList<Camel> camelList) {
		Camel winner = camelList.get(0);
		
		for (Camel c : camelList) {
			if (c.tile>winner.tile) {
				winner = c;
			}
			else if (c.tile==winner.tile) {
				if (c.stack>winner.stack) {
					winner = c;
				}
			}
		}
		
		numWins.replace(winner.color, numWins.get(winner.color)+1);
	}

	private void addPlaces(Board board){
		int maxTile = board.maxTile();
		int place = 1;

		for (int i = maxTile; i>=0; i--){
		    if (board.locations[i]!=null){
		        Camel c = board.locations[i];
                sumPlace.replace(c.color, sumPlace.get(c.color)+place);
                place++;

                for (int j=1; j<=c.stack; j++){
                    sumPlace.replace(c.getBelow(j).color, sumPlace.get(c.getBelow(j).color)+place);
                    place++;
                }
            }
        }
	}
	
	public HashMap<String,Camel> cloneCamelColors(HashMap<String,Camel> camelColors){
		HashMap<String,Camel> cloneColors = new HashMap<String,Camel>();
		Set<String> stringKey = camelColors.keySet();
		
		for (String col : stringKey) {//clones all the camels
			cloneColors.put(col, camelColors.get(col).cloneCamel());
		}
		
		for (String col : stringKey) {//updates stack for cloned camels
			Camel temp = cloneColors.get(col);
			if(temp.stack>0) {
				temp.below = cloneColors.get(temp.below.color);
				temp.below.above = temp;
			}
		}
		
		return cloneColors;
	}

	private void init(){
		numWins.clear();//clears wins
		numWins.put("ORANGE", 0);
		numWins.put("BLUE", 0);
		numWins.put("YELLOW", 0);
		numWins.put("GREEN", 0);
		numWins.put("WHITE", 0);

		sumPlace.clear();
		sumPlace.put("ORANGE", 0);
		sumPlace.put("BLUE", 0);
		sumPlace.put("YELLOW", 0);
		sumPlace.put("GREEN", 0);
		sumPlace.put("WHITE", 0);


	}

	
	private int wtfactorial(int num) {
		int retval=num;
		
		while(num!=1) {
			num--;
			retval*=num;
		}
		return retval;
	}
	
	

}

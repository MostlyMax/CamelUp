import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Game {
	
	Camel orangeCamel;
	Camel blueCamel;
	Camel yellowCamel;
	Camel greenCamel;
	Camel whiteCamel;
	
	Scanner sc;
	
	ArrayList<Camel> camelList;
	ArrayList<String> allColors;
	ArrayList<Trap> trapList;

	HashMap<String, Camel> camelColors;
	Board b;
	
	public static void main(String[] args) {
		
		Game game = new Game();
	}
	
	public Game() {
		
		init();

		int i=0;
		int diceRoll;
		String diceColor;
		sc = new Scanner(System.in);

		startOrder();
		
		Simulation sim = new Simulation(this);
		
		while(i<20) {
            b.updateLocations();//updates locations on game board
			if(i%allColors.size()==0) sim.reset();//if a rotation has gone by, reset
			
			i++;//general counter

            //Getting dice input
			System.out.println("Enter dice roll:");
			diceRoll = sc.nextInt();
			System.out.println("Enter dice color:");
			diceColor = sc.next();
			diceColor = diceColor.toUpperCase();
			
			System.out.println(diceRoll+" "+diceColor);
			
			Camel movingCamel = camelColors.get(diceColor);

			//moves camel and updates locations again
			movingCamel.move(diceRoll);
			movingCamel.stackOnTop(b.locations[movingCamel.tile]);
            b.updateLocations();

            //printInfo();
            //Starts simulation
			sim.start(diceColor);

			
		}
		sc.close();
	}

    /**
     * Gets the start stack order of the camels(bottom to top)
     */
	private void startOrder() {
		Camel prev = null;
		String input;
		
		System.out.println("Enter the stack of camels from bottom to top");
		
		for(int i=0; i<allColors.size(); i++) {
			input = sc.next();
			input = input.toUpperCase();
			
			camelColors.get(input).stackOnTop(prev);
			prev = camelColors.get(input);
		}
	}

    /**
     * Initializes everything
     */
	private void init() {
		
		
		camelList = new ArrayList<Camel>();
		trapList = new ArrayList<Trap>();
		camelColors = new HashMap<String, Camel>();
		allColors = new ArrayList<String>();
		
		allColors.add("ORANGE");
		allColors.add("BLUE");
		allColors.add("YELLOW");
		allColors.add("GREEN");
		allColors.add("WHITE");
		
		orangeCamel = new Camel("ORANGE");
		blueCamel = new Camel("BLUE");
		yellowCamel = new Camel("YELLOW");
		greenCamel = new Camel("GREEN");
		whiteCamel = new Camel("WHITE");
			
		camelList.add(orangeCamel);
		camelList.add(blueCamel);
		camelList.add(yellowCamel);
		camelList.add(greenCamel);
		camelList.add(whiteCamel);
		
		for (Camel c : camelList) {camelColors.put(c.color, c);}
		b = new Board(camelList);
	}

    /**
     * Prints info of all camels
     */
	public void printInfo() {
		for (Camel c:camelList) {
			System.out.println("Camel: "+c.color);
			System.out.println("Tile: "+c.tile);
			//if(c.above!=null)
			//System.out.println("Above: "+c.above.color);
			if(c.below!=null)
			System.out.println("Below: "+c.below.color);
			System.out.println("-----------------------");
		}
	}

}

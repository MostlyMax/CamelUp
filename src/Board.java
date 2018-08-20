import java.util.ArrayList;
import java.util.Collection;

public class Board {
	ArrayList<Camel> camelList;
	Camel[] locations;
	Trap[] trapLocations;
	
	public Board(Collection<Camel> camels){
		locations = new Camel[100];
		camelList = new ArrayList<Camel>();
		camelList.addAll(camels);
	
	}
	
	public Board cloneBoard() {
		Board clone = new Board(camelList);
		clone.locations = this.locations;
		
		return clone;
	}

	public int maxTile(){
		int retval = 0;

		for (Camel c : locations){
			if(c!=null){
				if (c.tile>retval){
					retval = c.tile;
				}
			}

		}
		return retval;
	}
	
	@SuppressWarnings("unchecked")
	public void copyBoard(Board copy) {
		copy.camelList = (ArrayList<Camel>) camelList.clone();
		copy.locations = locations.clone();
	}
	
	public void updateLocations() {
		
		for (int i=0; i<locations.length-1; i++) {
			locations[i]=null;
		}
		
		for (Camel c : camelList) {
			if (c.above==null) {
				locations[c.tile]=c;	
			}	
		}
	}
	
	public Camel getBottomCamel(Camel camel) {
		if (camel.below==null) return camel;
		else return getBottomCamel(camel.below);
	}
	
	public Camel getTopCamel(Camel camel) {
		if (camel.above==null) return camel;
		else return getTopCamel(camel.above);
	}
}

import java.awt.Color;
import java.util.ArrayList;

public class Camel {
	String color;
	
	Camel above;
	Camel below;
	
	int tile;
	int stack;
	
	public Camel(String color) {
		tile = 0;
		stack = 0;
		this.color = color;
	}

	public Camel getBelow(int i) {
		if (i==1) return below;
		else return below.getBelow(i-1);
	}

	public void move(int amount) {
		if (below!= null) below.above=null;
		below = null;
		
		tile+=amount;
		if (above!=null) above.scooch(amount);
	}
	
	private void scooch(int amount) {
		tile+=amount;
		if (above!=null) above.scooch(amount);
	}
	
	public void moveBack(int amount) {
		if (above!=null) above.below = below;
		if (below!= null) below.above=above;
		below = null;
		above = null;
		
		tile-=amount;
		
	}
	
	public void stackOnTop(Camel camel) {
		if(camel==null) {
			below=null;
			stack = 0;
		}
		
		else{
			camel.above = this;
			below = camel;
			stack = camel.stack+1;
		}
		
		if(above!=null) {
			above.updateStack();
		}
		
	}
	
	public void updateStack() {
		stack = below.stack+1;
		if(above!=null) {
			above.updateStack();
		}
	}
	
	public void stackBelow(Camel camel) {
		if (camel==null){
			above = null;
		}
		camel.below = this;
		above = camel;
	}
	
	public Camel cloneCamel() {
		Camel clone = new Camel(this.color);
		clone.above = this.above;
		clone.below = this.below;
		clone.stack = this.stack;
		clone.tile = this.tile;
		
		return clone;
	}
	
	
	public void cloneCamel(Camel clone) {
		clone.above = this.above;
		clone.below = this.below;
		clone.stack = this.stack;
		clone.tile = this.tile;
		clone.color = this.color;
		
	}

}

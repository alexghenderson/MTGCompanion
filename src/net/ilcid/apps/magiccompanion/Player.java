package net.ilcid.apps.magiccompanion;

public class Player {

	private Integer PoisonCounters = 0;
	private Integer Health = 20;
	private String Name = "";
	
	public Player(String name) {
		this.Name = name;
	}
	
	public int getPoisonCounters() {
		// TODO Auto-generated method stub
		return this.PoisonCounters;
	}

	public int getHealth() {
		// TODO Auto-generated method stub
		return this.Health;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return this.Name;
	}

	public void setPoisonCounters(int poisonCounters) {
		this.PoisonCounters = poisonCounters;
	}
	
	public void decrementPoisonCounters(int value) {
		this.PoisonCounters -= value;
		if(this.PoisonCounters < 0) {
			this.PoisonCounters = 0;
		}
	}
	
	public void incrementPoisonCounters(int value) {
		this.PoisonCounters += value;
	}
	
	public void setHealth(int health) {
		this.Health = health;
	}
	
	public void incrementHealth(int value) {
		this.Health += value;
	}
	
	public void decrementHealth(int value) {
		this.Health -= value;
		if(this.Health < 0) {
			this.Health = 0;
		}
	}
	
	public void reset() {
		this.PoisonCounters = 0;
		this.Health = 20;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o.getClass().equals(this.getClass())) {
			Player p = (Player)o;
			if(this.getHealth() == p.getHealth()
					&& this.getPoisonCounters() == p.getPoisonCounters()
					&& this.getName() == p.getName()) {
				return true;
			}	
		}
		return false;
		
	}
}

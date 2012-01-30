package net.ilcid.apps.magiccompanion;

import java.util.ArrayList;

public class Game {
	private static Game mInstance;
	private ArrayList<Player> Players;
	
	private Game() {
		this.Players = new ArrayList<Player>();
	}
	
	public static Game getInstance() {
		if(Game.mInstance == null)
			mInstance = new Game();
		return mInstance;
	}
	
	public ArrayList<Player> getPlayers() {
		return this.Players;
	}
	
	public void addPlayer(String name) {
		this.Players.add(new Player(name));
	}
	
	public void removePlayer(Player p) {
		this.Players.remove(p);
	}

}

package org.nnhl.api;

public class Request
{
	private Player player;
	
	private Subscription subscription;

	public Request(Player player, Subscription subscription) {
		super();
		this.player = player;
		this.subscription = subscription;
	}

	public Player getPlayer() {
		return player;
	}

	public Subscription getSubscription() {
		return subscription;
	}
		
}

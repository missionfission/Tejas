package net;

import main.ArchitecturalComponent;
import dram.MainMemoryDRAMController;
import generic.CommunicationInterface;
import generic.Event;

public class BusInterface implements CommunicationInterface{

	Bus bus;
	public BusInterface(Bus bus) {
		super();
		this.bus = bus;
	}
	
	/*
	 * Messages are coming from simulation elements(cores, cache banks) in order to pass it to another
	 * through electrical snooping Bus.
	 */
	//@Override
	public void sendMessage(Event event) {
		bus.sendBusMessage(event);		
	}

	public MainMemoryDRAMController getNearestMemoryController(int chanNum) {
		// TODO Auto-generated method stub
		return ArchitecturalComponent.getMainMemoryDRAMController(this,chanNum);
	}
}

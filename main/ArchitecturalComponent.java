package main;
/*
/*****************************************************************************
				GPUTejas Simulator
------------------------------------------------------------------------------------------------------------

   Copyright [2014] [Indian Institute of Technology, Delhi]
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
------------------------------------------------------------------------------------------------------------

	Contributors:  Seep Goel, Geetika Malhotra, Harinder Pal
*****************************************************************************/ 


import config.SystemConfig;
import config.TpcConfig;
import memorysystem.SMMemorySystem;
import memorysystem.MemorySystem;
import generic.SM;
import dram.MainMemoryDRAMController;
import emulatorinterface.communication.IpcBase;
import generic.CommunicationInterface;
import generic.Core;
//import generic.CoreBcastBus;
import generic.EventQueue;
import generic.LocalClockperSm;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import config.CacheConfig;
import config.MainMemoryConfig;
import config.SystemConfig;

import memorysystem.Cache;
//import memorysystem.CoreMemorySystem;
import dram.MainMemoryDRAMController;
import memorysystem.MemorySystem;
//import memorysystem.coherence.Coherence;
//import memorysystem.nuca.NucaCache;
import net.Bus;
import net.BusInterface;
import net.InterConnect;
import net.NOC;
import net.Router;

public class ArchitecturalComponent {

	//public static Vector<Vector<SM>> sm= new Vector<Vector<SM>>(); 
	private static SM[][] cores;
	public static Vector<Cache> sharedCaches = new Vector<Cache>();
	public static Vector<Cache> caches = new Vector<Cache>();
//	public static HashMap<String, NucaCache> nucaList=  new HashMap<String, NucaCache>();
	private static InterConnect interconnect;
//	public static CoreBcastBus coreBroadcastBus;
	public static Vector<MainMemoryDRAMController> memoryControllers = new Vector<MainMemoryDRAMController>();


	public static void setInterConnect(InterConnect i) {
	interconnect = i;
}
	
public static void createChip() {
		// Interconnect
			// Core
			// Coherence
			// Shared Cache
			// Main Memory Controller

		if(SystemConfig.interconnect ==  SystemConfig.Interconnect.Bus) {
			ArchitecturalComponent.setInterConnect(new Bus());
			createElementsOfBus();
		} else if(SystemConfig.interconnect == SystemConfig.Interconnect.Noc) {
			ArchitecturalComponent.setInterConnect(new NOC(SystemConfig.nocConfig));
			//createElementsOfNOC();			
			((NOC)interconnect).ConnectNOCElements();
		}
		
//		MemorySystem.createLinkBetweenCaches();
//		MemorySystem.setCoherenceOfCaches();
//		initCoreBroadcastBus();
		LocalClockperSm.systemTimingSetUp(getCores());
	}
	public static SM[][] initCores()
	{
		System.out.println("initializing cores...");
		
		
		SM[][] sms = new SM[SystemConfig.NoOfTPC][TpcConfig.NoOfSM];
		for (int i=0; i<SystemConfig.NoOfTPC; i++)
		{
			for(int j =0; j<TpcConfig.NoOfSM; j++)
			{
				sms[i][j] = new SM(i, j);
			}
		
		}
		return sms;
	}
	
	public static SM[][] getCores() {
		return cores;
	}

	public static void setCores(SM[][] cores) {
		ArchitecturalComponent.cores = cores;
	}

	public static long getNoOfInstsExecuted()
	{
		long noOfInstsExecuted = 0;
		for(int i = 0; i < ArchitecturalComponent.getCores().length; i++)
		{
			for(int j =0 ; j<ArchitecturalComponent.getCores()[i].length ; j++ )
			{
				noOfInstsExecuted += ArchitecturalComponent.getCores()[i][j].getNoOfInstructionsExecuted();
			}
			
		}
		return noOfInstsExecuted;
	}

		
	private static SMMemorySystem[][] coreMemSysArray;
	public static SMMemorySystem[][] getCoreMemSysArray()
	{
		return coreMemSysArray;
	}
private static void createElementsOfBus() {
	
	/*
	 * Added By Gantavya : I have added the SM's in place of the core, because in the NOCs the SM will communicate. So
	 */
		
		Bus bus = new Bus();
		BusInterface busInterface;
		SM[][] sms = initCores();
		for(int i=0;i<SystemConfig.NoOfTPC;i++){
			for(int j=0;j<TpcConfig.NoOfSM;j++){
				busInterface= new BusInterface(bus);
				sms[i][j].setComInterface(busInterface);
			}
		}
		setCores(sms);
		
		// Create Cores
		//		for(int i=0; i<SystemConfig.NoOfCores; i++) {
		//			Core core = createCore(i);
		//			busInterface = new BusInterface(bus);
		//			core.setComInterface(busInterface);
		//			cores.add(core);
		//		}
		
		// Create Shared Cache
		// PS : Directory will be created as a special shared cache
		
		for(CacheConfig cacheConfig : SystemConfig.sharedCacheConfigs) {
			busInterface = new BusInterface(bus);
			Cache c = MemorySystem.createSharedCache(cacheConfig.cacheName, busInterface);
			c.setComInterface(busInterface);
		}
		
		// Create Main Memory Controller
		//Note: number of physical channels = number of Memory Controllers
		
		//added later by kush
		//in case we use simple (fixed latency) memory controllers, num channels not taken into consideration currently (default to 1)
		if(SystemConfig.memControllerToUse==true){
			for(int i=0;i<SystemConfig.mainMemoryConfig.numChans;i++){
				MainMemoryDRAMController mainMemController = new MainMemoryDRAMController(SystemConfig.mainMemoryConfig);
				mainMemController.setChannelNumber(i);
				busInterface = new BusInterface(bus);
				mainMemController.setComInterface(busInterface);
				memoryControllers.add(mainMemController);
			}
		}
		else{
			MainMemoryDRAMController mainMemController = new MainMemoryDRAMController(SystemConfig.mainMemoryConfig);
				mainMemController.setChannelNumber(0);
				busInterface = new BusInterface(bus);
				mainMemController.setComInterface(busInterface);
				memoryControllers.add(mainMemController);
		}
		
		}
//	
private static void createElementsOfNOC() {
	
	/*
	 * Added By Gantavya:
	 * This method creates the NOC out of the given file of NOC. 
	 * In this method first of all I am initializing the cores, 
	 * ASSUMPTION : I am assuming that in the noc file, the total number of TPC and the number of SM inside those will be consistent 
	 * 				with those in the config file. Thus We have just added the communication inteface to those of the SM.
	 * The TPC_number will help us to take a proper count of TPC and the number of SM inside them is taken care by the variable  SM_number_withinTPC
	 * On appearance of each TPC, I will increase its count, and will set the SM number to -1;
	 */
	
	
	int TPC_number=-1;
	int SM_number_withinTPC;    
	setCores(initCores());
	//create elements mentioned as topology file
	BufferedReader readNocConfig = NOC.openTopologyFile(SystemConfig.nocConfig.NocTopologyFile);
	
	// Skip the first line. It contains numrows/cols information
	try {
		readNocConfig.readLine();
	} catch (IOException e1) {
		misc.Error.showErrorAndExit("Error in reading noc topology file !!");
	}
	
	int numRows = ((NOC)interconnect).getNumRows();
	int numColumns = ((NOC)interconnect).getNumColumns();
	for(int i=0;i<numRows;i++)
	{
		String str = null;
		try {
			str = readNocConfig.readLine();
		} catch (IOException e) {
			misc.Error.showErrorAndExit("Error in reading noc topology file !!");
		}
		
		//StringTokenizer st = new StringTokenizer(str," ");
		StringTokenizer st = new StringTokenizer(str);
		
		for(int j=0;j<numColumns;j++)
		{
			String nextElementToken = (String)st.nextElement();
			
			//System.out.println("NOC [" + i + "][" + j + "] = " + nextElementToken);
			
			CommunicationInterface comInterface = ((NOC)interconnect).getNetworkElements()[i][j];
			if(nextElementToken.equals("TPC")){
				TPC_number++;
				SM_number_withinTPC=-1;
			}
			else if(nextElementToken.equals("SM")){
				if(TPC_number==-1){
					misc.Error.showErrorAndExit("There should be a TPC before any SM !!");
				}else{
					SM_number_withinTPC++;
					cores[TPC_number][SM_number_withinTPC].setComInterface(comInterface);
				}
	//			Core core = createCore(cores.size());
	//			cores.add(core);
	//			core.setComInterface(comInterface);
			} else if(nextElementToken.equals("M")) {
				MainMemoryDRAMController mainMemController = new MainMemoryDRAMController(SystemConfig.mainMemoryConfig);
				memoryControllers.add(mainMemController);
				mainMemController.setComInterface(comInterface);
			} else if(nextElementToken.equals("-")) {
				//do nothing
			} else {
				Cache c = MemorySystem.createSharedCache(nextElementToken, comInterface);
				//TODO split and multiple shared caches
			} 
		}
	}
}	
	public static void initMemorySystem(SM[][] sms) {
		coreMemSysArray = MemorySystem.initializeMemSys(ArchitecturalComponent.getCores());		
	}
	private static ArrayList<Router> nocRouterList = new ArrayList<Router>();
	
	public static void addNOCRouter(Router router) {
		nocRouterList.add(router);		
	}
	
	public static SM createSM(int tpc_number, int sm_number){
		return new SM(tpc_number, sm_number);
	}
	
	public static ArrayList<Router> getNOCRouterList() {
		return nocRouterList;
	}

//	public static void initCoreBroadcastBus() {
//		coreBroadcastBus = new CoreBcastBus();		
//	}

	public static MainMemoryDRAMController getMainMemoryDRAMController(CommunicationInterface comInterface,int chanNum) {
		//TODO : return the nearest memory controller based on the location of the communication interface
		return memoryControllers.get(chanNum);
	}
	
	public static Vector<Cache> getCacheList() {
		return caches;
	}
	
	public static Vector<Cache> getSharedCacheList() {
		return sharedCaches;
	}
}



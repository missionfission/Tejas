package pipeline.multi_issue_inorder;

import java.io.FileWriter;
import java.io.IOException;

import config.SimulationConfig;
import pipeline.FunctionalUnitType;
import pipeline.OpTypeToFUTypeMapping;
import generic.SM;
import pipeline.StageLatch_MII;
import generic.Event;
import generic.EventQueue;
import generic.LocalClockperSm;
import generic.Instruction;
import generic.OperandType;
import generic.OperationType;
import generic.PortType;
import generic.SimulationElement;

public class DecodeUnit_MII extends SimulationElement{
	
	SM core;
	MultiIssueInorderExecutionEngine containingExecutionEngine;
	StageLatch_MII ifIdLatch;
	StageLatch_MII idExLatch; 
	
	long numBranches;
	long numMispredictedBranches;
	long lastValidIPSeen;
	
	long numAccesses;
	
	long instCtr; //for debug
	
	public DecodeUnit_MII(SM core, MultiIssueInorderExecutionEngine execEngine)
	{
		/*
		 * numPorts and occupancy = -1 => infinite ports 
		 * Latency = 1 . 
		 * 
		*/
		super(PortType.Unlimited, -1, -1 , -1, -1);
		this.core = core;
		containingExecutionEngine = execEngine;
		ifIdLatch = execEngine.getIfIdLatch();
		idExLatch = execEngine.getIdExLatch();
		
		numBranches = 0;
		numMispredictedBranches = 0;
		lastValidIPSeen = -1;
		
		instCtr = 0;
	}

	
//	public void performDecode(MultiIssueInorderPipeline inorderPipeline){
//		
//		if(containingExecutionEngine.getMispredStall() > 0)
//		{
//			return;
//		}
//		
//		containingExecutionEngine.getExecutionCore().clearPortUsage();
//		
//		Instruction ins = null;
//				
//		while(ifIdLatch.isEmpty() == false
//				&& idExLatch.isFull() == false)
//		{
//			ins = ifIdLatch.peek(0);
//			OperationType opType;
//				
//			if(ins!=null)
//			{
//				opType = ins.getOperationType();
//				
//				if(checkDataHazard(ins))	//Data Hazard Detected,Stall Pipeline
//				{
//					containingExecutionEngine.incrementDataHazardStall(1);
//					break;
//				}
//				
//				//check for structural hazards
//				long FURequest = 0;
//				if(OpTypeToFUTypeMapping.getFUType(ins.getOperationType()) != FunctionalUnitType.inValid)
//				{
//					FURequest = containingExecutionEngine.getExecutionCore().requestFU(
//							OpTypeToFUTypeMapping.getFUType(ins.getOperationType()));
//				
//					if(FURequest > 0)
//					{
//						break;
//					}
//				}
//				
//				incrementNumDecodes(1);
//   				
//				//add destination register of ins to list of outstanding registers
//				if(ins.getOperationType() == OperationType.load)
//				{
//					addToValueReadyArray(ins.getDestinationOperand(), Long.MAX_VALUE);
//				}
//				else if(ins.getOperationType() == OperationType.xchg)
//				{
//					addToValueReadyArray(ins.getSourceOperand1(),
//							LocalClockperSm.getCurrentTime()
//								+ containingExecutionEngine.getExecutionCore().getFULatency(
//										OpTypeToFUTypeMapping.getFUType(ins.getOperationType())));
//					if(ins.getSourceOperand1().getValue() != ins.getSourceOperand2().getValue()
//							|| ins.getSourceOperand1().getOperandType() != ins.getSourceOperand2().getOperandType())
//					{
//						addToValueReadyArray(ins.getSourceOperand2(),
//								LocalClockperSm.getCurrentTime()
//									+ containingExecutionEngine.getExecutionCore().getFULatency(
//											OpTypeToFUTypeMapping.getFUType(ins.getOperationType())));
//					}
//				}
//				else
//				{
//					if(ins.getDestinationOperand() != null)
//					{
//						addToValueReadyArray(ins.getDestinationOperand(),
//											LocalClockperSm.getCurrentTime()
//												+ containingExecutionEngine.getExecutionCore().getFULatency(
//														OpTypeToFUTypeMapping.getFUType(ins.getOperationType())));
//					}
//				}
//				
//				//update last valid IP seen
//				if(ins.getCISCProgramCounter() != -1)
//				{
//					lastValidIPSeen = ins.getCISCProgramCounter();
//				}
//				
//				//perform branch prediction
//				if(ins.getOperationType()==OperationType.branch)
//				{
//					boolean prediction = containingExecutionEngine.getBranchPredictor().predict(
//																		lastValidIPSeen,
//																		ins.isBranchTaken());
//					if(prediction != ins.isBranchTaken())
//					{
//						//Branch mispredicted
//						//stall pipeline for appropriate cycles
//						containingExecutionEngine.setMispredStall(core.getBranchMispredictionPenalty());
//						numMispredictedBranches++;
//					}
//					this.containingExecutionEngine.getBranchPredictor().incrementNumAccesses(1);
//	
//					//Train Branch Predictor
//					containingExecutionEngine.getBranchPredictor().Train(
//							ins.getCISCProgramCounter(),
//							ins.isBranchTaken(),
//							prediction
//							);
//					this.containingExecutionEngine.getBranchPredictor().incrementNumAccesses(1);
//					
//					numBranches++;
//				}
//				
//				if(ins.getOperationType() != OperationType.inValid)
//				{
//					misc.Error.showErrorAndExit("decode out of order!!");
//				}
//				instCtr++;
//				
//				//move ins to next stage
//				idExLatch.add(ins, LocalClockperSm.getCurrentTime() + 1);
//				ifIdLatch.poll();
//			
//				//if a branch/jump instruction is issued, no more instructions to be issued this cycle
//			
//			}
//			else
//			{
//				break;
//			}
//		}
//	}


	@Override
	public void handleEvent(EventQueue eventQ, Event event) {
		
	}

	public long getNumBranches() {
		return numBranches;
	}

	public long getNumMispredictedBranches() {
		return numMispredictedBranches;
	}
	
	void incrementNumDecodes(int incrementBy)
	{
		numAccesses += incrementBy;
	}


}

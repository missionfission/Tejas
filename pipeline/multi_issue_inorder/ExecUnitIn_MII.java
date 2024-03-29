//package pipeline.multi_issue_inorder;
//
//import java.io.FileWriter;
//
//
//import java.io.IOException;
//import pipeline.FunctionalUnitType;
//import pipeline.OpTypeToFUTypeMapping;
//import pipeline.multi_issue_inorder.MultiIssueInorderExecutionEngine;
//import config.SimulationConfig;
//import generic.SM;
//import generic.Event;
//import generic.EventQueue;
//import generic.LocalClockperSm;
//import generic.Instruction;
//import generic.OperationType;
//import generic.PortType;
//import generic.SimulationElement;
//import pipeline.StageLatch_MII;
//
//
//public class ExecUnitIn_MII extends SimulationElement{
//	SM core;
//	EventQueue eventQueue;
//	MultiIssueInorderExecutionEngine containingExecutionEngine;
//	StageLatch_MII idExLatch;
//	StageLatch_MII exMemLatch;
//	long[] instructionCompletesAt;
//	
//	long instCtr; //for debug
//	
//	long numResultsBroadCastBusAccess;
//	
//	public ExecUnitIn_MII(SM core, MultiIssueInorderExecutionEngine execEngine)
//	{
//		super(PortType.Unlimited, -1, -1 , -1, -1);
//		this.core = core;
//		this.eventQueue = core.getEventQueue();
//		containingExecutionEngine = execEngine;
//		idExLatch = execEngine.getIdExLatch();
//		exMemLatch = execEngine.getExMemLatch();
//		
//		instructionCompletesAt = new long[containingExecutionEngine.getIssueWidth()];
//		for(int i = 0; i < containingExecutionEngine.getIssueWidth(); i++)
//		{
//			instructionCompletesAt[i] = -1;
//		}
//		
//		instCtr = 0;
//	}
//	
//	public void execute(MultiIssueInorderPipeline inorderPipeline)
//	{
//		if(containingExecutionEngine.getMispredStall() > 0)
//		{
//			return;
//		}
//		
//		Instruction ins = null;
//		
//		while(idExLatch.isEmpty() == false
//				&& exMemLatch.isFull() == false)
//		{
//			ins = idExLatch.peek(0);
//			if(ins!=null)
//			{
//				FunctionalUnitType FUType = OpTypeToFUTypeMapping.getFUType(ins.getOperationType());
//				long lat = 1;
//				
//				if(FUType != FunctionalUnitType.memory
//						&& FUType != FunctionalUnitType.inValid)
//				{
//					lat = containingExecutionEngine.getExecutionCore().getFULatency(FUType);
//				}
//				/*
//				 * memory address computation for loads/stores happens in this cycle
//				 * assumed as one cycle operation
//				 */
//				
//				if(ins.getOperationType() != OperationType.inValid)
//				{
//					misc.Error.showErrorAndExit("exec out of order!!");
//				}
//				instCtr++;
//				
//				//move ins to next stage
//				exMemLatch.add(ins, idExLatch.getInstructionCompletesAt(ins) + lat);
//				idExLatch.poll();
//				
////			
////				if(SimulationConfig.debugMode)
////				{
////					System.out.println("executed : " + GlobalClock.getCurrentTime()/core.getStepSize() + "\n"  + ins + "\n");
////				}
//			}
//			else
//			{
//				break;
//			}
//		}
//	}
//	
//	@Override
//	public void handleEvent(EventQueue eventQ, Event event) {
//
//		
//	}
//	
//	void incrementResultsBroadcastBusAccesses(int incrementBy)
//	{
//		numResultsBroadCastBusAccess += incrementBy;
//	}
//
//
//}

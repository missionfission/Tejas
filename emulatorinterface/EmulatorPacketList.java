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
package emulatorinterface;

import java.util.ArrayList;

import emulatorinterface.communication.Packet;

public class EmulatorPacketList {
	
	ArrayList<Packet> packetList;
	int size = 0;
	final int listSize = 5; // load + store + branch + assembly + control-flow(thread)

	public EmulatorPacketList() {
		super();
		this.packetList = new ArrayList<Packet>();
		
		for(int i=0; i<listSize; i++) {
			packetList.add(new Packet());
		}
	}
	
	public void add(Packet p) {
		if(size==packetList.size()) {
			return;
		}
		this.packetList.get(size).set(p);
		size++;
	}
	
	public void clear() {
		size = 0;
	}
	
	public Packet get(int index) {
		if(index>size) {
			System.out.println("trying to access element outside packetList size" + 
				"size = " + size + "\tindex = " + index);
		}

		return packetList.get(index);
	}
	
	public int size() {
		return size;
	}
}

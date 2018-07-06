package regfiles;

import emulatorinterface.*;
import emulatorinterface.CalculateWarpOccupancy;
public class regfilecontroller {

}
//TODO
// Add Register Files 
// Create a Warp Class 
// Store memory requested threads in the Register Files
// Change CalculateWarpOccupancy function in the emulator interface
// when warp has a memory barrier add it to the register file
// shader.cc contains the functions regarding the warp scheduling 
// Barrier Table has a commented and unresolved tgt 


















//unsigned gpgpu_shader_registers;
//int gpgpu_warpdistro_shader;
//int gpgpu_warp_issue_shader;
//unsigned gpgpu_num_reg_banks;
//bool gpgpu_reg_bank_use_warp_id;
//bool gpgpu_local_mem_map;


//unsigned gpgpu_sim::finished_kernel()
//{
//    if( m_finished_kernel.empty() ) 
//        return 0;
//    unsigned result = m_finished_kernel.front();
//    m_finished_kernel.pop_front();
//    return result;
//}
//
//void gpgpu_sim::set_kernel_done( kernel_info_t *kernel ) 
//{ 
//    unsigned uid = kernel->get_uid();
//    m_finished_kernel.push_back(uid);
//    std::vector<kernel_info_t*>::iterator k;
//    for( k=m_running_kernels.begin(); k!=m_running_kernels.end(); k++ ) {
//        if( *k == kernel ) {
//            *k = NULL;
//            break;
//        }
//    }
//    assert( k != m_running_kernels.end() ); 
//}
//




//void shader_core_ctx::writeback()
//{
//
//	unsigned max_committed_thread_instructions=m_config->warp_size * (m_config->pipe_widths[EX_WB]); //from the functional units
//	m_stats->m_pipeline_duty_cycle[m_sid]=((float)(m_stats->m_num_sim_insn[m_sid]-m_stats->m_last_num_sim_insn[m_sid]))/max_committed_thread_instructions;
//
//    m_stats->m_last_num_sim_insn[m_sid]=m_stats->m_num_sim_insn[m_sid];
//    m_stats->m_last_num_sim_winsn[m_sid]=m_stats->m_num_sim_winsn[m_sid];
//
//    warp_inst_t** preg = m_pipeline_reg[EX_WB].get_ready();
//    warp_inst_t* pipe_reg = (preg==NULL)? NULL:*preg;
//    while( preg and !pipe_reg->empty()) {
//    	/*
//    	 * Right now, the writeback stage drains all waiting instructions
//    	 * assuming there are enough ports in the register file or the
//    	 * conflicts are resolved at issue.
//    	 */
//    	/*
//    	 * The operand collector writeback can generally generate a stall
//    	 * However, here, the pipelines should be un-stallable. This is
//    	 * guaranteed because this is the first time the writeback function
//    	 * is called after the operand collector's step function, which
//    	 * resets the allocations. There is one case which could result in
//    	 * the writeback function returning false (stall), which is when
//    	 * an instruction tries to modify two registers (GPR and predicate)
//    	 * To handle this case, we ignore the return value (thus allowing
//    	 * no stalling).
//    	 */
//
//        m_operand_collector.writeback(*pipe_reg);
//        unsigned warp_id = pipe_reg->warp_id();
//        m_scoreboard->releaseRegisters( pipe_reg );
//        m_warp[warp_id].dec_inst_in_pipeline();
//        warp_inst_complete(*pipe_reg);
//        m_gpu->gpu_sim_insn_last_update_sid = m_sid;
//        m_gpu->gpu_sim_insn_last_update = gpu_sim_cycle;
//        m_last_inst_gpu_sim_cycle = gpu_sim_cycle;
//        m_last_inst_gpu_tot_sim_cycle = gpu_tot_sim_cycle;
//        pipe_reg->clear();
//        preg = m_pipeline_reg[EX_WB].get_ready();
//        pipe_reg = (preg==NULL)? NULL:*preg;
//    }
//}


//void shader_core_ctx::issue_warp( register_set& pipe_reg_set, const warp_inst_t* next_inst, const active_mask_t &active_mask, unsigned warp_id )
//{
//    warp_inst_t** pipe_reg = pipe_reg_set.get_free();
//    assert(pipe_reg);
//    
//    m_warp[warp_id].ibuffer_free();
//    assert(next_inst->valid());
//    **pipe_reg = *next_inst; // static instruction information
//    (*pipe_reg)->issue( active_mask, warp_id, gpu_tot_sim_cycle + gpu_sim_cycle, m_warp[warp_id].get_dynamic_warp_id() ); // dynamic instruction information
//    m_stats->shader_cycle_distro[2+(*pipe_reg)->active_count()]++;
//    func_exec_inst( **pipe_reg );
//    if( next_inst->op == BARRIER_OP ){
//    	m_warp[warp_id].store_info_of_last_inst_at_barrier(*pipe_reg);
//        m_barriers.warp_reaches_barrier(m_warp[warp_id].get_cta_id(),warp_id,const_cast<warp_inst_t*> (next_inst));
//
//    }else if( next_inst->op == MEMORY_BARRIER_OP ){
//        m_warp[warp_id].set_membar();
//    }
//
//    updateSIMTStack(warp_id,*pipe_reg);
//    m_scoreboard->reserveRegisters(*pipe_reg);
//    m_warp[warp_id].set_next_pc(next_inst->pc + next_inst->isize);
//}














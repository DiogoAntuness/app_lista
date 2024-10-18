package com.example.listatarefas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listatarefas.databinding.ItemTarefaBinding
import com.example.listatarefas.model.Tarefa

class TarefaAdapter(
    val onClickExcluir: (Int) -> Unit,
    val onClickEditar: (Tarefa) -> Unit
) : RecyclerView.Adapter<TarefaAdapter.TarefaViewHolder>() {

    private var listaTarefas: List<Tarefa> = emptyList() // Cria uma lista vazia de Tarefa

    fun adicionarLista( lista: List<Tarefa>){
        this.listaTarefas = lista
        notifyDataSetChanged()//Recarrega o app
    }

    inner class TarefaViewHolder(itemBinding: ItemTarefaBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {

        private val binding: ItemTarefaBinding

        init {
            binding = itemBinding
        }

        fun bind(tarefa: Tarefa){
            binding.textDescricao.text = tarefa.descricao
            binding.textData.text = tarefa.dataCadastro

            binding.btnExcluir.setOnClickListener{ //Apagar as tarefas
                onClickExcluir( tarefa.idTarefa )
            }

            binding.btnEditar.setOnClickListener{
                onClickEditar( tarefa )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarefaViewHolder {
        val itemTarefaBinding = ItemTarefaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TarefaViewHolder(itemTarefaBinding)
    }

    override fun onBindViewHolder(holder: TarefaViewHolder, position: Int) {
        val tarefa = listaTarefas[position]
        holder.bind( tarefa )
    }

    override fun getItemCount(): Int {
        return listaTarefas.size
    }

}
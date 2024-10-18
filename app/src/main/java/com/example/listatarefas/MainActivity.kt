package com.example.listatarefas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listatarefas.database.TarefaDAO
import com.example.listatarefas.databinding.ActivityMainBinding
import com.example.listatarefas.model.Tarefa

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var listaTarefas = emptyList<Tarefa>()
    private var tarefaAdapter: TarefaAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AdicionarTarefaActivity::class.java )
            startActivity( intent )
        }

        //Recyclerview
        tarefaAdapter = TarefaAdapter(
            { id -> confirmarExclusao(id)},
            {tarefa -> editar(tarefa)  } // EDITAR E APAGAR
        )
        binding.rvTarefas.adapter = tarefaAdapter

        binding.rvTarefas.layoutManager = LinearLayoutManager(this )

    }

    private fun editar(tarefa: Tarefa) {

        val intent = Intent(this, AdicionarTarefaActivity::class.java)
        intent.putExtra( "tarefa", tarefa)
        startActivity( intent )

    }

    private fun confirmarExclusao(id: Int) {

        val alertBuilder = AlertDialog.Builder(this)

        alertBuilder.setTitle("Vai apagar mesmo?")
        alertBuilder.setMessage("Deseja apagar a tarefa?")

        alertBuilder.setPositiveButton("Sim"){_, _ ->

            val tarefaDAO = TarefaDAO(this)
            if ( tarefaDAO.remover( id ) ){
                atualizarListaTarefas() //ATUALIZAR / IMPORTANTE /
                Toast.makeText(this, "Tarrefa apagada!",
                    Toast.LENGTH_LONG)
                    .show()
            }else{
                Toast.makeText(this, "Tarrefa nao foi excluida!",
                    Toast.LENGTH_LONG)
                    .show()
            }

        }
        alertBuilder.setNegativeButton("Não"){_, _ ->

        }
        alertBuilder.create().show()

    }

    private fun atualizarListaTarefas(){

        val TarefaDAO = TarefaDAO(this )
        listaTarefas = TarefaDAO.listar()
        tarefaAdapter?.adicionarLista( listaTarefas )
    }

    override fun onStart() {
        super.onStart()
        atualizarListaTarefas()

    }
}
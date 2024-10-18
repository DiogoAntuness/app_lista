package com.example.listatarefas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.listatarefas.database.TarefaDAO
import com.example.listatarefas.databinding.ActivityAdicionarTarefaBinding
import com.example.listatarefas.model.Tarefa

class AdicionarTarefaActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAdicionarTarefaBinding.inflate( layoutInflater )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Recuperar tarefa passada
        var tarefa: Tarefa? = null
        val bundle = intent.extras
        if ( bundle != null ){
            tarefa = bundle.getSerializable("tarefa") as Tarefa
            binding.editTarefa.setText( tarefa.descricao )
        }

        binding.btnSalvar.setOnClickListener {

            if ( binding.editTarefa.text.isNotEmpty() ){      //SE nao estiver vazia

               if ( tarefa != null ){
                    editar( tarefa )
               }else{
                   salvar()
               }

            }else{
                Toast.makeText(this, "Preencha uma tarefa",
                    Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun editar(tarefa: Tarefa) {

        val descricao = binding.editTarefa.text.toString()
        val tarefaAtualizar = Tarefa(
            tarefa.idTarefa, descricao, "default"
        )

        val tarefaDAO = TarefaDAO(this)
        if ( tarefaDAO.atualiar( tarefaAtualizar ) ) {
            Toast.makeText(
                this, "Atualizada com sucesso",
                Toast.LENGTH_LONG
            ).show()
            finish()//fechar a activity
        }
    }

    private fun salvar() {
        val descricao = binding.editTarefa.text.toString()
        val tarefa = Tarefa(
            -1, descricao, "default"
        )
        val tarefaDAO = TarefaDAO(this)
        if (tarefaDAO.salvar(tarefa)) {
            Toast.makeText(
                this, "Cadastrada com sucesso",
                Toast.LENGTH_LONG
            ).show()
            finish()//fechar a activity
        }
    }
}
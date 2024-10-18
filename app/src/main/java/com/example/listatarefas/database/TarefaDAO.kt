package com.example.listatarefas.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.listatarefas.model.Tarefa
import java.lang.Exception

class TarefaDAO(context: Context) : ITarefaDAO {

    private val escrita = DatabaseHelper(context).writableDatabase
    private val leitura = DatabaseHelper(context).readableDatabase

    override fun salvar(tarefa: Tarefa): Boolean {//SALVAR AS TAREFAS

        val conteudos = ContentValues()
        conteudos.put("${DatabaseHelper.COLUNA_DESCRICAO}", tarefa.descricao)

        try {
            escrita.insert(
                DatabaseHelper.NOME_TABELA_TAREFAS,
                null,
                conteudos
            )
            Log.i("info_db", "SECESSO AO SALVAR TAREFAS")
        }catch (e: Exception){
            e.printStackTrace()
            Log.i("info_db", "ERRO AO SALVAR TAREFAS")
            return false
        }

        return true
    }

    override fun atualiar(tarefa: Tarefa): Boolean {

        val args = arrayOf( tarefa.idTarefa.toString() )
        val conteudo = ContentValues()
        conteudo.put("${DatabaseHelper.COLUNA_DESCRICAO}", tarefa.descricao )

        try {
            escrita.update(
                DatabaseHelper.NOME_TABELA_TAREFAS,
                conteudo,
                "${ DatabaseHelper.COLUNA_ID_TARERFA } = ?",
                args
            )
            Log.i("info_db", "SECESSO AO ATUALIZAR TAREFA")
        }catch (e: Exception){
            e.printStackTrace()
            Log.i("info_db", "ERRO AO ATUALIZAR TAREFA")
            return false
        }

        return true

    }

    override fun remover(idTarefa: Int): Boolean {

        val args = arrayOf( idTarefa.toString() )
        try {
            escrita.delete(
                DatabaseHelper.NOME_TABELA_TAREFAS,
                "${ DatabaseHelper.COLUNA_ID_TARERFA } = ?",
                args
            )
            Log.i("info_db", "SECESSO AO APAGAR TAREFA")
        }catch (e: Exception){
            e.printStackTrace()
            Log.i("info_db", "ERRO AO APAGAR TAREFA")
            return false
        }

        return true
      }


    override fun listar(): List<Tarefa> {

        val listaTarefas = mutableListOf<Tarefa>()

        //SELECAO NO DB
        val sql = "SELECT ${DatabaseHelper.COLUNA_ID_TARERFA}," +
                " ${DatabaseHelper.COLUNA_DESCRICAO}, " +
                "strftime('%d/%m/%Y %H:%M', ${DatabaseHelper.COLUNA_DATA}) AS ${DatabaseHelper.COLUNA_DATA} " +
                "FROM ${DatabaseHelper.NOME_TABELA_TAREFAS}"//Colocando data e hora Br

        //RECUPERAR DO DB
        val cursor = leitura.rawQuery(sql, null)

        val indeceId        = cursor.getColumnIndex( DatabaseHelper.COLUNA_ID_TARERFA )
        val indeceDescrecao = cursor.getColumnIndex( DatabaseHelper.COLUNA_DESCRICAO )
        val indeceData      = cursor.getColumnIndex( DatabaseHelper.COLUNA_DATA )

        while ( cursor.moveToNext() ){

            val idTarefa     = cursor.getInt( indeceId )
            val descricao    = cursor.getString( indeceDescrecao )
            val data         = cursor.getString( indeceData )

            listaTarefas.add(//ADD OS INTENS
                Tarefa(idTarefa, descricao, data)
            )
        }

        return  listaTarefas
    }
}
package gestor;

import java.time.LocalDate;

public class Task {

         private String nome;
         private int prioridade;
         private LocalDate dueDate;
         private LocalDate dataInicio;

         public Task(String nome, int prioridade, LocalDate dataInicio, LocalDate dueDate) {
                  this.nome = nome;
                  this.prioridade = prioridade;
                  this.dueDate = dueDate;
                  this.dataInicio = dataInicio;
         }

         public String getNome() {
                  return nome;
         }

         public int getPrioridade() {
                  return prioridade;
         }

         public LocalDate getDueDate() {
                  return dueDate;
         }

         public LocalDate getDataInicio() {
                  return dataInicio;
         }

         public void setPrioridade(int prioridade) {
                  this.prioridade = prioridade;
         }

         public void setDueDate(LocalDate dueDate) {
                  this.dueDate = dueDate;
         }

         @Override
         public String toString() {
                  return "Tarefa: " + nome + " | Prioridade: " + prioridade + " | Data Inicio: " + dataInicio + " | Data Limite: " + dueDate;
         }
}

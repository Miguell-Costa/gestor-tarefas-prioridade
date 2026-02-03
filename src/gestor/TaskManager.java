package gestor;

import java.time.LocalDate;
import java.util.*;

public class TaskManager {

         private final PriorityQueue<Task> tarefas;
         private final Map<String, Task> tarefasMap;

         public TaskManager() {
                  //Criação da priorityQueue
                  tarefas = new PriorityQueue<>(new TaskPriorityComparator());
                  //Criação do HashMap para facilitar na procura pelas tarefas atraves do nome
                  tarefasMap = new HashMap<>();
         }

         public boolean adicionarTarefa(String nome, int prioridade, LocalDate data) {
                  //Criar tarefa 
                  Task nova = new Task(nome, prioridade, LocalDate.now(), data);
                  boolean flag = false;
                  
                  //verificar se existe alguma tarefa com o mesmo nome
                  for(Task t: tarefas){
                           if(nome.equals(t.getNome())){
                                    flag = true;
                                    break;
                           }
                  }
                  
                  if(!flag){
                         //Adiciona na priorityQueue
                           tarefas.add(nova);
                           //Adiciona no Map
                           tarefasMap.put(nome, nova); 
                           return true;
                  }else{
                           return false;
                  }
                  
         }

         public Task pesquisarTarefa(String nome) {
                  //Pesquisar tarefa pelo nome atrave do Map
                  return tarefasMap.get(nome);
         }

         public void editarTarefa(String nome, int prioridade, LocalDate data) {
                  //Pesquisar tarefa pelo nome atrave do Map
                  Task tarefa = tarefasMap.get(nome);

                  tarefa.setPrioridade(prioridade);

                  tarefa.setDueDate(data);
         }

         //verificar se a tarefa existe
         public boolean verificarTarefa(String nome) {
                  return tarefasMap.containsKey(nome);
         }

         public boolean removerTarefa(String nome) {
                  Task tarefaRemover = null;

                  for (Task t : tarefas) {
                           // Verificar se a tarefa existe procurando pelo nome
                           if (nome.equals(t.getNome())) {
                                    tarefaRemover = t;
                           }
                  }

                  if (tarefaRemover != null) {
                           //Se existir ´re removida na priorityQueue e no Map
                           tarefas.remove(tarefaRemover);
                           tarefasMap.remove(nome);
                           return true;
                  } else {
                           return false;
                  }

         }

         public boolean removerConjunto(int prioridade) {
                  boolean flag = false;
                  //remover em segurança da lista
                  Iterator<Task> iterator = tarefas.iterator();

                  //percorrer a lista
                  while (iterator.hasNext()) {
                           Task t = iterator.next();
                           if (t.getPrioridade() == prioridade) {
                                    iterator.remove(); // remove da lista 
                                    tarefasMap.remove(t.getNome()); // remove do mapa pelo nome
                                    flag = true;
                           }
                  }
                  return flag;
         }
         
         public boolean removerTodasTarefas(){
                  //verifica se existem tarefas
                  if(tarefas.isEmpty()){
                           return true;
                  }
                  //remove todas as tarefas
                  tarefas.clear();
                  tarefasMap.clear();
                  return false;
         }

         public Map<Integer, Integer> contarPrioridades() {
                  Map<Integer, Integer> contagem = new HashMap<>();
                  
                  for (Task t : tarefas) {
                           int p = t.getPrioridade();
                           // Se já existir essa prioridade no mapa, soma 1 ao contador
                           if (contagem.containsKey(p)) {
                                    contagem.put(p, contagem.get(p) + 1);
                           } else {
                                    // Se ainda não existir, adiciona com valor inicial 1
                                    contagem.put(p, 1);
                           }
                  }
                  return contagem;
         }

         public Task obterMaiorPrioridade(){
          //encontra a tarefa de maior prioridade e remove
          Task t = tarefas.poll();
          
          if(t != null){
                   //remover no map
                   tarefasMap.remove(t.getNome());
          }
         
          return t;
}
         
         public boolean atrasoTarefa(String nome){
                  Task tarefa = tarefasMap.get(nome);
                  
                  if(tarefa != null){
                           //verificar se esta atrasada
                           return tarefa.getDueDate().isBefore(LocalDate.now());
                  }
                  return false;
         }
         
         public List<Task> getTodasTarefas() {
                  return new ArrayList<>(tarefas);
         }

}

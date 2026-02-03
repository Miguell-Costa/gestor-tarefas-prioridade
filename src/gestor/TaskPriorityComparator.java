package gestor;

import java.util.Comparator;

public class TaskPriorityComparator implements Comparator<Task> {

         @Override
         public int compare(Task t1, Task t2) {
                   // Compara prioridades invertendo a ordem para ter prioridade maior primeiro
                  return Integer.compare(t2.getPrioridade(), t1.getPrioridade());
         }
}        

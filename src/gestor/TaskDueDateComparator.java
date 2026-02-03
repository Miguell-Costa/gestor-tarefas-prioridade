package gestor;

import java.util.Comparator;

public class TaskDueDateComparator implements Comparator<Task> {

         @Override
         public int compare(Task t1, Task t2) {
                  // Compara prioridades invertendo a ordem para ter prioridade maior primeiro
                  return t1.getDueDate().compareTo(t2.getDueDate());
         }
}

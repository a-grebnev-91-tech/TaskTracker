package tasktracker.manager;

import java.util.Collection;
import java.util.List;

/**
 * комментарий для ревьюера - на мой взгляд хранить задачи где-либо кроме менеджера не совсем правильно,
 * поэтому вместо передачи ссылок на объекты задач в HistoryManager я пошел по пути передачи айди задачи
 * и реализации работы класса HistoryManager только с айдишниками задач. Не уверен правилен ли такой подход,
 * но то, что предлагается в задании мне кажется еще менее правильным.
 */
public interface HistoryManager {

    void add(Integer id);

    void add(Collection<Integer> IDs);

    void remove(Integer id);

    void remove(Collection<Integer> IDs);

    List<Integer> getHistory();
}
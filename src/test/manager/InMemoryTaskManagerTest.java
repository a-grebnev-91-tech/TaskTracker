package manager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import taskdata.EpicTask;
import taskdata.Subtask;
import taskdata.Task;
import taskdata.TaskStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static util.Managers.*;

class InMemoryTaskManagerTest {

    @MethodSource("test1MethodSource")
    @ParameterizedTest(name = "{index}. Check epic status with {2}")
    void test1_checkEpicStatus(List<Subtask> subtasks, TaskStatus expected, String testResultDescription) {
        TaskManager manager = getDefault();
        EpicTask epic = new EpicTask("0", "0");
        manager.createTask(epic);
        for (Subtask subtask : subtasks) {
            manager.createTask(subtask);
        }
        TaskStatus epicStatus = manager.getEpicTask(1).getStatus();
        assertEquals(expected, epicStatus);
    }

    private static Stream<Arguments> test1MethodSource() {
        List<Subtask> emptyList = Collections.EMPTY_LIST;
        List<Subtask> newSubtasks = new ArrayList<>(List.of(
                new Subtask(1, "1", "1"),
                new Subtask(1, "2", "2"),
                new Subtask(1, "3", "3")
        ));
        List<Subtask> doneSubtasks = new ArrayList<>(List.of(
                new Subtask(0, 1, TaskStatus.DONE, "1", "1"),
                new Subtask(0, 1, TaskStatus.DONE, "2", "2"),
                new Subtask(0, 1, TaskStatus.DONE, "3", "3")
        ));
        List<Subtask> newDoneSubtasks = new ArrayList<>(List.of(
                new Subtask(0, 1, TaskStatus.DONE, "1", "1"),
                new Subtask(0, 1, TaskStatus.DONE, "2", "2"),
                new Subtask(0, 1, TaskStatus.NEW, "3", "3")
        ));
        List<Subtask> inProgressSubtasks = new ArrayList<>(List.of(
                new Subtask(0, 1, TaskStatus.IN_PROGRESS, "1", "1"),
                new Subtask(0, 1, TaskStatus.IN_PROGRESS, "2", "2"),
                new Subtask(0, 1, TaskStatus.IN_PROGRESS, "3", "3")
        ));
        List<Subtask> allStatusesSubtasks = new ArrayList<>(List.of(
                new Subtask(0, 1, TaskStatus.NEW, "1", "1"),
                new Subtask(0, 1, TaskStatus.IN_PROGRESS, "2", "12"),
                new Subtask(0, 1, TaskStatus.DONE, "3", "3")
        ));
        return Stream.of(
                Arguments.of(emptyList, TaskStatus.NEW, "empty subtasks"),
                Arguments.of(newSubtasks, TaskStatus.NEW, "new subtasks"),
                Arguments.of(doneSubtasks, TaskStatus.DONE, "done subtasks"),
                Arguments.of(newDoneSubtasks, TaskStatus.IN_PROGRESS, "new and done subtasks"),
                Arguments.of(inProgressSubtasks, TaskStatus.IN_PROGRESS, "in progress subtasks"),
                Arguments.of(allStatusesSubtasks, TaskStatus.IN_PROGRESS, "all types of statuses of subtasks")
        );
    }

    @MethodSource("test2MethodSource")
    @ParameterizedTest(name = "{index}. Check history with {1}")
    void test2_checkHistoryManagerAddMethod(List<Task> tasks, String testResultDescription) {
        TaskManager manager = getDefault();
        for (Task task : tasks) {
            manager.createTask(task);
        }
        for (Task task : tasks) {
            manager.getTask(task.getID());
        }
        List<Task> tasksFromHistory = manager.history();
        assertNotNull(tasksFromHistory, "Задачи не возвращаются");
        assertEquals(tasks, tasksFromHistory, "Задачи не совпадают");
    }

    private static Stream<Arguments> test2MethodSource() {
        return Stream.of(
//                Arguments.of(new ArrayList<>(), "empty history"),
//                Arguments.of(getSimpleTasks(), "simple, not repeating tasks")
        );
    }

    @Test
    void test3_checkHistoryAddingRepeatingTasks() {
        TaskManager manager = getDefault();
        Task task = new Task("a", "a");
        long id = manager.createTask(task);
        manager.getTask(id);
        manager.getTask(id);
        assertNotNull(manager.history(), "Задачи не возвращаются");
        assertEquals(1, manager.history().size(), "Задачи дублируются");
    }

    @MethodSource("test4MethodSource")
    @ParameterizedTest(name = "{index}. Check remove from {2} of history")
    void test4_removingFromHistory(
            List<Task> tasks,
            List<Task> expectedTasks,
            Long idToRemove,
            String testResultDescription
    ) {
        HistoryManager history = new InMemoryHistoryManager();
        for (Task task : tasks) {
            history.add(task);
        }
        history.remove(idToRemove);

    }
}
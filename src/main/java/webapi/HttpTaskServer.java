package webapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.TaskManager;
import taskdata.EpicTask;
import taskdata.Subtask;
import taskdata.Task;
import util.Managers;
import util.web.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    public static final String CHARSET_NAME = "utf-8";
    public static final Charset DEFAULT_CHARSET = Charset.forName(CHARSET_NAME);
    public static final String TASKS_PATH = "/tasks/task";
    public static final String EPICS_PATH = "/tasks/epic";
    public static final String SUBTASKS_PATH = "/tasks/subtask";
    public static final String HISTORY_PATH = "/tasks/history";
    public static final String PRIORITIZED_PATH = "/tasks";
    private final Gson gson;
    private final HttpServer server;
    private final int PORT = 8080;
    private final TaskManager manager;

    public HttpTaskServer() throws IOException {
        gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .registerTypeAdapter(EpicTask.class, new EpicTaskAdapter())
                .registerTypeAdapter(Subtask.class, new SubtaskAdapter())
                .create();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        manager = Managers.getDefault();
        //TODO del setupManager
        setupManager();
        //TODO^^^^^^^^^^^^^
        HttpHandler taskHandler = new TaskHandler(manager, gson);
        server.createContext(TASKS_PATH, taskHandler);
        server.createContext(EPICS_PATH, taskHandler);
        server.createContext(SUBTASKS_PATH, taskHandler);
        server.createContext(HISTORY_PATH, new HistoryHandler(manager, gson));
        server.createContext(PRIORITIZED_PATH, new PrioritizedHandler(manager, gson));
        server.start();
        System.out.println("Server start on port " + PORT);
    }

    //TODO delete (for test purposes)
    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer();
    }

    private void setupManager() {
        manager.createTask(new Task("a", "a"));
        manager.createTask(new Task("aa", "aa", LocalDateTime.now(), Duration.ofHours(1)));
        manager.createTask(new EpicTask("b", "b"));
        manager.createTask(new EpicTask("bb", "bb"));
        manager.createTask(new Subtask(3, "c", "c"));
        manager.createTask(new Subtask(3, "cc", "cc"));
        manager.getTask(1);
        manager.getTask(2);
    }
    // TODO ^^^^^^^^^^^^^^^^^^^^^^
}

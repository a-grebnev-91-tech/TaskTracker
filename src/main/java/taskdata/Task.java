package taskdata;

import util.exceptions.TaskInvalidException;
import util.exceptions.TaskTimeException;
import util.tasks.TaskDateTime;
import util.tasks.TaskToString;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task implements Cloneable {

    private String description;
    private long id;
    private String name;
    private TaskStatus status;
    private TaskDateTime taskDateTime;

    public Task(String name, String description) {
        if (name == null || name.isBlank())
            throw new TaskInvalidException("Cannot create unnamed task");
        if (description == null)
            description = "";
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        taskDateTime = new TaskDateTime(null, null);
    }

    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        this(name, description);
        taskDateTime = new TaskDateTime(startTime, duration);
    }

    public Task(long id, TaskStatus status, String name, String description) {
        this(name, description);
        if (status == null) throw new TaskInvalidException("Cannot create task with null status");
        this.status = status;
        if (id < 0) throw new TaskInvalidException("Cannot create task with ID less than 0");
        this.id = id;
    }

    public Task(long id,
                TaskStatus status,
                String name,
                String description,
                LocalDateTime startTime,
                Duration duration) {
        this(name, description, startTime, duration);
        if (status == null) throw new TaskInvalidException("Cannot create task with null status");
        this.status = status;
        if (id < 0) throw new TaskInvalidException("Cannot create task with ID less than 0");
        this.id = id;
    }

    @Override
    public Task clone() {
        LocalDateTime startTime = null;
        Duration duration = null;
        if (this.taskDateTime != null) {
            startTime = this.taskDateTime.getStartTime();
            duration = this.taskDateTime.getDuration();
        }
        return new Task(this.id, this.status, this.name, this.description, startTime, duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != task.id) return false;
        if (status != task.status) return false;
        if (!Objects.equals(name, task.name)) return false;
        if (!Objects.equals(description, task.description)) return false;
        return Objects.equals(taskDateTime, task.taskDateTime);
    }

    public String getDescription() {
        return description;
    }

    public Duration getDuration() {
        if (taskDateTime == null) return null;
        return taskDateTime.getDuration();
    }

    public TaskDateTime getTaskDateTime() {
        return this.taskDateTime;
    }

    public LocalDateTime getEndTime() {
        if (taskDateTime == null) return null;
        return taskDateTime.getEndTime();
    }

    public long getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public LocalDateTime getStartTime() {
        if (taskDateTime == null) return null;
        return taskDateTime.getStartTime();
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (taskDateTime != null ? taskDateTime.hashCode() : 0);
        return result;
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }

    public void setDuration(Duration duration) {
        setTime(getStartTime(), duration);
    }

    public void setID(long id) {
        if (id >= 0)
            this.id = id;
    }

    public void setName(String name) {
        if (name != null && !name.isBlank())
            this.name = name;
    }

    public void setStatus(TaskStatus status) {
        if (status != null)
            this.status = status;
    }

    public void setStartTime(LocalDateTime time) {
        setTime(time, getDuration());
    }

    public void setTime(LocalDateTime startTime, Duration duration) {
        if (startTime == null ^ duration == null)
            throw new TaskTimeException("Cannot set start time as null and duration as not null and vice versa");
        this.taskDateTime = new TaskDateTime(startTime, duration);
    }

    @Override
    public String toString() {
        return TaskToString.getString(this);
    }
}

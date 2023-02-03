package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Change<T> {
    private T objectChanged;
    private T oldValue;
    private T newValue;
    private User user;
    private LocalDateTime localDateTime;

    public Change(T objectChanged, T oldValue, T newValue, User user, LocalDateTime localDateTime) {
        this.objectChanged = objectChanged;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.user = user;
        this.localDateTime = localDateTime;
    }

    public T getObjectChanged() {
        return objectChanged;
    }

    public void setObjectChanged(T objectChanged) {
        this.objectChanged = objectChanged;
    }

    public T getOldValue() {
        return oldValue;
    }

    public void setOldValue(T oldValue) {
        this.oldValue = oldValue;
    }

    public T getNewValue() {
        return newValue;
    }

    public void setNewValue(T newValue) {
        this.newValue = newValue;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Change<?> change = (Change<?>) o;
        return Objects.equals(objectChanged, change.objectChanged) && Objects.equals(oldValue, change.oldValue) && Objects.equals(newValue, change.newValue) && Objects.equals(user, change.user) && Objects.equals(localDateTime, change.localDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectChanged, oldValue, newValue, user, localDateTime);
    }

    @Override
    public String toString() {
        return objectChanged + " " + oldValue + " " + newValue + " " + user + " " + localDateTime;
    }

    public Change setObjectChangedBuilder(T obj)
    {
        this.objectChanged = obj;
        return this;
    }

    public Change setOldValueBuilder(T obj)
    {
        this.oldValue = obj;
        return this;
    }

    public Change setNewValueBuilder(T obj)
    {
        this.newValue = obj;
        return this;
    }

    public Change setUserWhoChangedBuilder(User obj)
    {
        this.user = obj;
        return this;
    }

    public Change setLocalDateTimeBuilder(LocalDateTime obj)
    {
        this.localDateTime = obj;
        return this;
    }
}

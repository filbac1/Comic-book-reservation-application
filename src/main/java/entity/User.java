package entity;

import java.nio.file.Path;
import java.util.Objects;

public final class User implements UserReader {

    private static final User userInstance = new User();
    private Long id;
    private String username;
    private String password;
    private UserRole role;

    public static User getUserInstance() {
        return userInstance;
    }

    public User() {}

    public User(Long id, String username, String password, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, role);
    }

    @Override
    public String toString() {
        return id.toString();
    }


    @Override
    public boolean checkPasswordStrength(User user) {
        if(user.getPassword().length() < 3) {
            return false;
        } else {
            return true;
        }
    }

    public User setUserIDBuilder(Long obj)
    {
        this.id = obj;
        return this;
    }

    public User setUserUsernameBuilder(String obj)
    {
        this.username = obj;
        return this;
    }

    public User setUserPasswordBuilder(String obj)
    {
        this.password = obj;
        return this;
    }

    public User setUserRoleBuilder(UserRole obj)
    {
        this.role = obj;
        return this;
    }
}

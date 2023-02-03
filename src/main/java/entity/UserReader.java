package entity;

public sealed interface UserReader permits User {
    boolean checkPasswordStrength(User user);

}

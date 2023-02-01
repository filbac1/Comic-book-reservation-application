package entity;

import java.util.Set;

public sealed interface UserReader permits User {
    boolean checkPasswordStrength(User user);

}

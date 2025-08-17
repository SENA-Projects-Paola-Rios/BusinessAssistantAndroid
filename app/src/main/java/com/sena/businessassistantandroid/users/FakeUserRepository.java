package com.sena.businessassistantandroid.users;

import java.util.ArrayList;
import java.util.List;

public class FakeUserRepository {
    private final List<User> data = new ArrayList<>();
    private int nextId = 1;

    public FakeUserRepository() {
        // datos de ejemplo
        add(new User(0, "Paola Rios", "paola7@sena.com", "ADMIN", "1234"));
        add(new User(1, "Juan Perez", "juanp@sena.com", "USER", "abcd"));
        add(new User(2, "Maria Lopez", "marial@sena.com", "USER", "password"));
    }

    public List<User> getAll() {
        return data;
    }

    public User add(User u) {
        u.id = nextId++;
        data.add(u);
        return u;
    }

    public void update(User u) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).id == u.id) {
                data.set(i, u);
                return;
            }
        }
    }

    public void delete(User u) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).id == u.id) {
                data.remove(i);
                return;
            }
        }
    }
}

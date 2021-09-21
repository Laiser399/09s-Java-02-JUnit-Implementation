package org.mai.sn;

import org.mai.sn.exceptions.UserNotRegisteredException;

import java.util.*;

public class SocialNetworkImpl implements SocialNetwork {
    private final Map<String, Set<String>> connections = new HashMap<>();// user -> connectedUsers

    @Override
    public void addPerson(String name) {
        if (connections.containsKey(name)) {
            return;
        }

        connections.put(name, new HashSet<>());
    }

    @Override
    public void addConnection(String from, String to) {
        if (from.equals(to)) {
            throw new IllegalArgumentException("Received equals users.");
        }

        var fromConnections = connections.get(from);
        var toConnections = connections.get(to);

        if (fromConnections == null) {
            throw new UserNotRegisteredException("User with name \"%s\" not registered.".formatted(from));
        }
        if (toConnections == null) {
            throw new UserNotRegisteredException("User with name \"%s\" not registered.".formatted(to));
        }

        fromConnections.add(to);
        toConnections.add(from);
    }

    @Override
    public List<String> getFriends(String user, int level) {
        if (level <= 0) {
            throw new IllegalArgumentException("level <= 0");
        }

        if (!connections.containsKey(user)) {
            throw new UserNotRegisteredException("User with name \"%s\" not registered.".formatted(user));
        }

        var friends = new HashSet<String>();
        friends.add(user);
        findFriendsRecursively(user, level, friends);
        friends.remove(user);

        return new ArrayList<>(friends);
    }

    private void findFriendsRecursively(String user, int level, Set<String> addedUsers) {
        if (level == 0) {
            return;
        }

        for (var connectedUser : connections.get(user)) {
            if (addedUsers.contains(connectedUser)) {
                continue;
            }

            addedUsers.add(connectedUser);
            findFriendsRecursively(connectedUser, level - 1, addedUsers);
        }
    }
}

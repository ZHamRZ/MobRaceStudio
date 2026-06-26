package com.mobracestudio.arena;

import java.util.*;
import java.util.stream.Collectors;

public class ArenaManager {
    private final Map<String, Arena> arenas = new LinkedHashMap<>();
    private String activeArenaId;

    public void addArena(Arena arena) {
        arenas.put(arena.getId(), arena);
    }

    public void removeArena(String id) {
        arenas.remove(id);
        if (id.equals(activeArenaId)) {
            activeArenaId = null;
        }
    }

    public Arena getArena(String id) {
        return arenas.get(id);
    }

    public Arena getArenaByName(String name) {
        return arenas.values().stream()
            .filter(a -> a.getName() != null && a.getName().equalsIgnoreCase(name))
            .findFirst().orElse(null);
    }

    public List<Arena> getAllArenas() {
        return new ArrayList<>(arenas.values());
    }

    public List<String> getArenaNames() {
        return arenas.values().stream()
            .map(Arena::getName)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    public boolean containsArena(String name) {
        return arenas.values().stream()
            .anyMatch(a -> a.getName() != null && a.getName().equalsIgnoreCase(name));
    }

    public void setActiveArena(String id) {
        this.activeArenaId = id;
    }

    public Arena getActiveArena() {
        return activeArenaId != null ? arenas.get(activeArenaId) : null;
    }

    public int getArenaCount() {
        return arenas.size();
    }

    public void clear() {
        arenas.clear();
        activeArenaId = null;
    }

    public void importArenas(Collection<Arena> arenaList) {
        arenaList.forEach(a -> arenas.put(a.getId(), a));
    }
}

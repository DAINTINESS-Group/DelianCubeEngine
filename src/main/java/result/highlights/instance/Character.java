package result.highlights.instance;

import cubemanager.cubebase.Level;

/**
 * A Character: an OLAP dimension member, typed by the cube {@link Level} it belongs to.
*/
public final class Character {
    public final Level type;          // Character Type = a cube Level
    public final String id;           // the member, e.g. "Prague"
    public final String description;  // human-relatable text

    public Character(Level type, String id, String description) {
        this.type = type;
        this.id = id;
        this.description = description;
    }

    public Character(Level type, String id) { this(type, id, id); }
}

package result.highlights.instance;

import java.util.Objects;

import cubemanager.cubebase.Level;

/**
 * A Character: an OLAP dimension member, typed by the cube {@link Level} it belongs to. Two characters are
 * equal when they name the same member of the same level — value equality on (level name, member), so that
 * characters can be compared and used in member-set algebra ({@code involved}, theme, spine, intersection).
 * The {@code description} is human-relatable text and does not participate in identity.
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

    /** The level's name — the identity of the character's type, independent of the {@link Level} instance. */
    private String typeName() {
        return type == null ? null : type.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Character)) return false;
        Character other = (Character) o;
        return Objects.equals(typeName(), other.typeName()) && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName(), id);
    }
}

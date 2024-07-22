package com.pibic.tags.units;

import com.pibic.tags.Tag;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TagTest {

    @Test
    public void ShouldCreateTag() {
        var tag = Tag.create("Java", true, true);
        assertEquals("Java", tag.getName());
        assertTrue(tag.isPublished());
    }

    @Test
    public void ShouldNotCreateTagWithNonUniqueName() {
        assertThrows(IllegalArgumentException.class, () -> Tag.create("Java", true, false));
    }

    @Test
    public void ShouldCreateUnPublishedTagNonAdmin() {
        var tag = Tag.create("Java", false, true);
        assertEquals("Java", tag.getName());
        assertFalse(tag.isPublished());
    }

    @Test
    public void ShouldUpdateName()
    {
        var tag = Tag.create("Java", true, true);
        tag.updateName("Java 8", true);
        assertEquals("Java 8", tag.getName());
    }

    @Test
    public void ShouldNotUpdateNameWithNonUniqueName()
    {
        var tag = Tag.create("Java", true, true);
        assertThrows(IllegalArgumentException.class, () -> tag.updateName("Java", false));
    }
}
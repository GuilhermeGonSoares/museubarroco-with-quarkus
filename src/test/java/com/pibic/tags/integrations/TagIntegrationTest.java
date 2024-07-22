package com.pibic.tags.integrations;

import com.pibic.tags.Tag;
import com.pibic.tags.TagRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class TagIntegrationTest {
    @Inject
    TagRepository tagRepository;

    @Test
    @TestTransaction
    public void ShouldSaveTag() {
        var tag = Tag.create("Java", true, true);
        tagRepository.persist(tag);
        var savedTag = tagRepository.findByName("Java").orElseThrow();
        assertEquals(savedTag.getId(), 1L);
        assertEquals("Java", savedTag.getName());
        assertTrue(savedTag.isPublished());
    }
}

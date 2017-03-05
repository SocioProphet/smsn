package net.fortytwo.smsn.brain.io.wiki;

import net.fortytwo.smsn.brain.model.Note;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;

public class WikiParserTest {
    private WikiParser wikiParser = new WikiParser();

    @Before
    public void setUp() throws Exception {
        wikiParser = new WikiParser();
    }

    @Test
    public void testExample1() throws Exception {
        List<Note> notes = wikiParser.parse(
                getClass().getResourceAsStream("wiki-example-1.txt")).getChildren();
        assertEquals(7, notes.size());

        Note indentation = notes.get(1);
        assertNull(indentation.getId());
        assertEquals("indentation", indentation.getTitle());
        assertEquals("and this", indentation.getChildren()
                .get(2).getChildren()
                .get(0).getChildren()
                .get(0).getChildren()
                .get(0).getTitle());

        Note atts = notes.get(3);
        assertEquals("http://example.org/ns/attributes", atts.getAlias());
        assertEquals(0.75f, atts.getWeight());

        Note ws = notes.get(4);
        assertEquals(4, ws.getChildren().size());

        assertEquals("leading and trailing whitespace are ignored", ws.getChildren().get(2).getTitle());

        Note ids = notes.get(5);
        assertEquals("ids", ids.getTitle());
        assertEquals("0txXBm", ids.getChildren().get(0).getId());
        assertEquals("cE85nD", ids.getChildren().get(1).getId());
    }

    @Test
    public void testExample2() throws Exception {
        Note root = wikiParser.parse(getClass().getResourceAsStream("wiki-example-2.txt"));

        assertEquals("http://example.org/ns/top-level-attributes-are-allowed", root.getAlias());
        assertEquals(1.0f, root.getWeight());
        assertEquals(0.75f, root.getSharability());
        assertEquals(0.5f, root.getPriority());

        assertEquals(1, root.getChildren().size());
        assertEquals(1, root.getChildren().get(0).getChildren().size());
    }

    @Test
    public void testEmptyLinesIgnored() throws Exception {
        List<Note> notes = readNotes(
                "* one\n" +
                        "   \n" +     // empty line with additional whitespace
                        "* two" +
                        "\n" +        // empty line without additional whitespace
                        "* three");
        assertEquals(3, notes.size());
    }

    @Test(expected = IOException.class)
    public void pageIsOnlyAllowedInCanonicalFormat() throws IOException {
        wikiParser.setUseCanonicalFormat(false);
        readNotes("* Arthur Dent\n" +
                "\n" +
                "He's a jerk.\n" +
                "A complete kneebiter.");
    }

    @Test
    public void nonEmptyPageIsCopiedVerbatim() throws Exception {
        wikiParser.setUseCanonicalFormat(true);
        List<Note> notes = readNotes("* Arthur Dent\n" +
                "\n" +
                "He's a jerk.\n" +
                "A complete kneebiter.");
        assertEquals(1, notes.size());
        Note root = notes.get(0);
        assertEquals("Arthur Dent", root.getTitle());
        assertEquals("He's a jerk.\nA complete kneebiter.", root.getPage());
    }

    @Test
    public void emptyPageIsIgnored() throws Exception {
        wikiParser.setUseCanonicalFormat(true);
        List<Note> notes = readNotes("* Arthur Dent\n" +
                "\n" +
                "   ");
        assertEquals(1, notes.size());
        Note root = notes.get(0);
        assertEquals("Arthur Dent", root.getTitle());
        assertNull(root.getPage());
    }

    @Test(expected = IOException.class)
    public void testEmptyValuesNotAllowedForNewNotes() throws Exception {
        readNotes("* ");
    }

    @Test
    public void testEmptyValuesAllowedForExistingNotes() throws Exception {
        readNotes("* :1234567: ");
    }

    @Test
    public void testEmptyAliasAttributeAllowed() throws Exception {
        readNotes("@alias ");
    }

    @Test(expected = IOException.class)
    public void testEmptyPriorityAttributeNotAllowed() throws Exception {
        readNotes("@priority ");
    }

    @Test(expected = IOException.class)
    public void testEmptySharabilityAttributeNotAllowed() throws Exception {
        readNotes("@sharability ");
    }

    @Test(expected = IOException.class)
    public void testEmptyWeightAttributeNotAllowed() throws Exception {
        readNotes("@weight ");
    }

    @Test(expected = IOException.class)
    public void testLineTruncationSequenceNotAllowed() throws Exception {
        readNotes("" +
                "* this is a note whose value was truncated for readability [...]\n" +
                "   * you wouldn't want to lose the actual value because of a careless copy and paste, would you?");
    }

    @Test
    public void testLegalIds() throws Exception {
        List<Note> notes = readNotes("+ :LTWrf62: courage\n" +
                "+ :COAZgCU: justice\n" +
                "+ :g20vP2u: prudence\n" +
                "+ :Ifkv0cj: temperance\n" +
                "+ :rAr-qLh: detachment\n" +
                "+ :pXOAO_S: sincerity\n");
        assertEquals(6, notes.size());
        assertEquals("LTWrf62", notes.get(0).getId());
        assertEquals("rAr-qLh", notes.get(4).getId());

        notes = readNotes("" +
                "* :a:        short IDs are OK, although 7-byte IDs are 'standard'\n" +
                "* :aaaaaaaa: longer IDs are OK, too");
        assertEquals(2, notes.size());
        assertEquals("a", notes.get(0).getId());
        assertEquals("aaaaaaaa", notes.get(1).getId());
    }

    @Test
    public void testInvalidIdCharacters() throws Exception {
        List<Note> notes = readNotes("" +
                "* :123@456: the 'ID' of this note contains a character not in [A-Za-z0-9-_]\n" +
                "* it does not actually become an ID; just more value text");
        assertEquals(2, notes.size());
        assertNotSame("123@456", notes.get(0).getId());
    }

    @Test
    public void unicodeIsHandledAsExpected() throws IOException {
        List<Note> notes = readNotes("+ :UAk6ejU: foo bar\n\u00b7 :hSsMqzT: quux\n");
        assertEquals(2, notes.size());
    }

    /*
original:
+ :UAk6ejU: goya goya\\n    \\u00b7 :_8UG7N6: nimbwana\\n
   \\u00b7 :vPXpqj1: nole\\n    \\u00b7 :AGebVR5: nole\\n+ :hSsMqzT: nanonao\\
n    \\u00b7 :L5g2mUg: gorbalev\\n    \\u00b7 :gd_BOrG: and another\\n. new

unwrapped:
+ :UAk6ejU: goya goya\\n    \\u00b7 :_8UG7N6: nimbwana\\n   \\u00b7 :vPXpqj1: nole\\n    \\u00b7 :AGebVR5: nole\\n+ :hSsMqzT: nanonao\\n    \\u00b7 :L5g2mUg: gorbalev\\n    \\u00b7 :gd_BOrG: and another\\n. new

unescaped
+ :UAk6ejU: goya goya\n    \u00b7 :_8UG7N6: nimbwana\n   \u00b7 :vPXpqj1: nole\n    \u00b7 :AGebVR5: nole\n+ :hSsMqzT: nanonao\n    \u00b7 :L5g2mUg: gorbalev\n    \u00b7 :gd_BOrG: and another\n. new

inside unwrapped
+ :UAk6ejU: goya goya\\n    \\u00b7 :_8UG7N6: nimbwana\\n    \\u00b7 :vPXpqj1: nole\\n    \\u00b7 :AGebVR5: nole\\n+ :hSsMqzT: nanonao\\n   \\u00b7 :L5g2mUg: gorbalev\\n    \\u00b7 :gd_BOrG: and another\\n. new

inside unescaped
+ :UAk6ejU: goya goya\n    \u00b7 :_8UG7N6: nimbwana\n    \u00b7 :vPXpqj1: nole\n    \u00b7 :AGebVR5: nole\n+ :hSsMqzT: nanonao\n   \u00b7 :L5g2mUg: gorbalev\n    \u00b7 :gd_BOrG: and another\n. new
     */
    @Test
    public void testOther() throws Exception {
        List<Note> notes = readNotes(
                "+ :UAk6ejU: goya goya\n    \u00b7 :_8UG7N6: nimbwana\n    \u00b7 :vPXpqj1: nole\n    \u00b7 :AGebVR5: nole\n+ :hSsMqzT: nanonao\n   \u00b7 :L5g2mUg: gorbalev\n    \u00b7 :gd_BOrG: and another\n. new"        );
        assertEquals(3, notes.size());
    }

    private List<Note> readNotes(final String s) throws IOException {
        try (InputStream in = new ByteArrayInputStream(s.getBytes())) {
            return wikiParser.parse(in).getChildren();
        }
    }
}

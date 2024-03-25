/*
 * Copyright (c) 2022 University of Southampton.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
*
 * Contributor:
 *   University of Southampton - Initial API and implementation
 */

package tests;

import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test Part 1 of the Coursework.
 * <ul>
 * <li>{@link TestPersonSignature}: Test the signature of the {@link people.Person} class.</li>
 * <li>{@link TestPersonSpecification}: Test the specification of the {@link people.Person} class.</li>
 * <li>{@link TestMusicianSignature}: Test the signature of the {@link people.musicians.Musician} interface.</li>
 * <li>{@link TestViolinistSignature}: Test the signature of the {@link people.musicians.Violinist} class.</li>
 * <li>{@link TestViolinistSpecification}: Test the specification of the {@link people.musicians.Violinist} class.</li>
 * </ul>
 *
 * @author htson - v1.0 - Initial API and implementation
 * @version 1.0
 */
public class TestEcsBandAidPart1 extends TestCoursework {

    /**
     * Test class for {@link people.Person}'s signature.
     *
     * @author htson
     * @version 1.0
     * @see TestCoursework.AbstractTestPerson
     */
    @Nested
    public class TestPersonSignature extends AbstractTestPerson {

        /**
         * Test the signature of {@link people.Person} class
         */
        @Test
        @DisplayName("Test people.Person's signature")
        public void testPerson_Signature() {
            assertClass("");
            assertDeclaredField("", "name", String.class);
            assertConstructor("", String.class);
            assertMethod("", String.class, "getName");
        }

    }

    /**
     * Test class for {@link people.Person}'s specification.
     *
     * @author htson
     * @version 1.0
     * @see TestCoursework.AbstractTestPerson
     */
    @Nested
    public class TestPersonSpecification extends AbstractTestPerson {

        @BeforeEach
        public void setup() {
            super.setup();
        }

        /**
         * Test the name and getName() of {@link people.Person} class
         */
        @Test
        @DisplayName("Test people.Person's name and getName()")
        public void testPerson_name() {
            assertTimeout(ofMillis(5000), () -> {
                Constructor<?> personConstructor = assertConstructor(PRECONDITION, String.class);
                Method getNameMethod = assertMethod(PRECONDITION, String.class, "getName");
                getNameMethod.setAccessible(true);
                Object alice;
                try {
                    alice = personConstructor.newInstance("Alice");
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                         | InvocationTargetException e) {
                    e.printStackTrace();
                    fail(PRECONDITION + ": Failed to create a person using Person(String) constructor");
                    return;
                }

                try {
                    Object name = getNameMethod.invoke(alice);
                    assertEquals("Alice", name, ": Incorrect person's name");
                } catch (IllegalAccessException | IllegalArgumentException
                         | InvocationTargetException e) {
                    e.printStackTrace();
                    fail(": Failed to execute Person.getName() method");
                }

            });
        }

    }

    /**
     * Test class for {@link people.musicians.Musician}'s signature.
     *
     * @author htson
     * @version 1.0
     * @see TestCoursework.AbstractTestMusician
     */
    @Nested
    public class TestMusicianSignature extends AbstractTestMusician {

        /**
         * Test the signature of {@link people.musicians.Musician} interface
         */
        @Test
        @DisplayName("Test people.musicians.Musician's signature")
        public void testMusician_Signature() {
            assertInterface("");
            assertMethod("", "setSeat", int.class);
            assertMethod("", "readScore", INT_ARRAY_CLASS, boolean.class);
            assertMethod("", "playNextNote");
        }

    }

    /**
     * Test class for {@link people.musicians.Violinist}'s signature.
     *
     * @author htson
     * @version 1.0
     * @see TestCoursework.AbstractTestViolinist
     */
    @Nested
    public class TestViolinistSignature extends AbstractTestViolinist {

        private Class<?> personClass;

        private Class<?> musicianInterface;

        private Class<?> soundSystemClass;

        @BeforeEach
        public void setup() {
            super.setup();
            personClass = getClassOrInterface("people.Person");
            assertClass(PRECONDITION, personClass);
            musicianInterface = getClassOrInterface("people.musicians.Musician");
            assertInterface(PRECONDITION, musicianInterface);
            soundSystemClass = getClassOrInterface("utils.SoundSystem");
            assertClass(PRECONDITION, soundSystemClass);
        }


        /**
         * Test the signature of {@link people.musicians.Violinist} class
         */
        @Test
        @DisplayName("Test people.musicians.Violinist's signature")
        public void testViolinist_Signature() {
            assertClass("");
            assertImplementOrExtend("", personClass);
            assertImplementOrExtend("", musicianInterface);
            assertInheritedField("", "instrumentID");
            assertInheritedField("", "notes", INTEGER_LIST_CLASS);
            assertInheritedField("", "nextNote", INTEGER_ITERATOR_CLASS);
            assertInheritedField("", "soundSystem", soundSystemClass);
            assertInheritedField("", "seat");
            assertInheritedField("", "loudness");
            assertConstructor("", String.class, soundSystemClass);
            assertAccessibleMethod("", "setSeat", int.class);
            assertConstructor("", String.class, soundSystemClass, int.class);
            assertAccessibleMethod("", "readScore", INT_ARRAY_CLASS, boolean.class);
            assertAccessibleMethod("", "playNextNote");
        }

    }

    /**
     * Test class for {@link people.musicians.Violinist}'s specification.
     *
     * @author htson
     * @version 1.0
     * @see TestCoursework.AbstractTestViolinist
     */
    @Nested
    public class TestViolinistSpecification extends AbstractTestViolinist {

        private Class<?> personClass;

        private Class<?> musicianInterface;

        private Class<?> soundSystemClass;

        private Constructor<?> soundSystemConstructor;

        @BeforeEach
        public void setup() {
            super.setup();
            personClass = getClassOrInterface("people.Person");
            assertClass(PRECONDITION, personClass);
            musicianInterface = getClassOrInterface("people.musicians.Musician");
            assertInterface(PRECONDITION, musicianInterface);
            soundSystemClass = getClassOrInterface("utils.SoundSystem");
            assertClass(PRECONDITION, soundSystemClass);
            soundSystemConstructor = assertConstructor(soundSystemClass, "");
        }


        /**
         * Test the {@link people.musicians.Violinist#Violinist(String, utils.SoundSystem)} constructor
         * of {@link people.musicians.Violinist} class
         */
        @Test
        @DisplayName("Test people.musicians.Violinist#Violinist(String, SoundSystem) constructor'")
        public void testViolinist_constructor1() {
            assertTimeout(ofMillis(5000), () -> {
                Field soundSystemField = assertInheritedField(PRECONDITION, "soundSystem");
                Constructor<?> violinistConstructor = assertConstructor(PRECONDITION, String.class, soundSystemClass);
                Method getNameMethod = assertAccessibleMethod(PRECONDITION, "getName");
                Object soundSystem = null;
                try {
                    soundSystem = soundSystemConstructor.newInstance();
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                    fail(PRECONDITION + ": Failed to create a sound system using SoundSystem()");
                }

                Object alice = null;
                try {
                    alice = violinistConstructor.newInstance("Alice", soundSystem);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                    fail(": Failed to create a violinist using Violinist(String, SoundSystem)");
                }
                try {
                    assertEquals((Object) "Alice", getNameMethod.invoke(alice), "Incorrect violinist's name");
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                    fail(": Failed to call Violist#getName() method");
                }
                assertEquals((Object) soundSystem, soundSystemField.get(alice),
                        "Incorrect violinist's SoundSystem");
            });
        }

        /**
         * Test the {@link people.musicians.Violinist#Violinist(String, utils.SoundSystem)} constructor
         * of {@link people.musicians.Violinist} class
         */
        @Test
        @DisplayName("Test people.musicians.Violinist#Violinist(String, SoundSystem, int) constructor'")
        public void testViolinist_constructor2() {
            assertTimeout(ofMillis(5000), () -> {
                Field seatField = assertInheritedField("", "seat");
                Field soundSystemField = assertInheritedField("", "soundSystem");
                Constructor<?> violinistConstructor = assertConstructor("", String.class, soundSystemClass,
                        int.class);
                Method getNameMethod = assertAccessibleMethod("", "getName");
                try {
                    Object soundSystem = soundSystemConstructor.newInstance();
                    Object alice = violinistConstructor.newInstance("Alice", soundSystem, 3);
                    assertEquals((Object) "Alice", getNameMethod.invoke(alice), "Incorrect violinist's name");
                    assertEquals((Object) soundSystem, soundSystemField.get(alice),
                            "Incorrect violinist's SoundSystem");
                    assertEquals((Object) 3, seatField.get(alice), "Incorrect violinist's seat");
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
            });
        }

        /**
         * Test the {@link people.musicians.Violinist#setSeat(int)} method of {@link
         * people.musicians.Violinist} class
         */
        @Test
        @DisplayName("Test people.musicians.Violinist#setSeat(int)")
        public void testViolinist_setSeat() {
            assertTimeout(ofMillis(5000), () -> {
                Field seatField = assertInheritedField("", "seat");
                Constructor<?> violinistConstructor = assertConstructor("", String.class, soundSystemClass);
                Method setSeatMethod = assertAccessibleMethod("", "setSeat", int.class);
                Object alice = null;
                try {
                    Object soundSystem = soundSystemConstructor.newInstance();
                    alice = violinistConstructor.newInstance("Alice", soundSystem);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                    fail(": Failed to set up");
                }
                try {
                    setSeatMethod.invoke(alice, 4);
                    assertEquals((Object) 4, seatField.get(alice), "Incorrect violinist's seat");
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                    fail(": Failed to run the test");
                }
            });
        }

        /**
         * Test the {@link people.musicians.Violinist#readScore(int[], boolean)} method of {@link
         * people.musicians.Violinist} class
         */
        @Test
        @DisplayName("Test people.musicians.Violinist#readScore(int[], boolean)")
        public void testViolinist_readScore() {
            assertTimeout(ofMillis(5000), () -> {
                Field notesField = assertInheritedField("", "notes", INTEGER_LIST_CLASS);
                Constructor<?> violinistConstructor = assertConstructor("", String.class, soundSystemClass);
                Method readScoreMethod = assertAccessibleMethod("", "readScore", INT_ARRAY_CLASS,
                        boolean.class);
                Object alice = null;
                try {
                    Object soundSystem = soundSystemConstructor.newInstance();
                    alice = violinistConstructor.newInstance("Alice", soundSystem);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                    fail(": Failed to set up");
                }
                try {
                    int[] notes = {60, 61, 62};
                    readScoreMethod.invoke(alice, notes, true);
                    List actualNotes = (List) notesField.get(alice);
                    Object[] expectedNotes = new Object[]{60, 61, 62};
                    assertArrayEquals(expectedNotes, actualNotes.toArray(new Object[actualNotes.size()]),
                            "Incorrect violinist's notes");
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                    fail(": Failed to run the test");
                }
            });
        }

        /**
         * Test the {@link people.musicians.Violinist#playNextNote()} method of {@link
         * people.musicians.Violinist} class
         */
        @Test
        @DisplayName("Test people.musicians.Violinist#playNextNote()")
        public void testViolinist_playNextNote() {
            assertTimeout(ofMillis(5000), () -> {
                Field nextNoteField = assertInheritedField("", "nextNote", INTEGER_ITERATOR_CLASS);
                Constructor<?> violinistConstructor = assertConstructor("", String.class, soundSystemClass);
                Method setSeatMethod = assertAccessibleMethod("", "setSeat", int.class);
                Method readScoreMethod = assertAccessibleMethod("", "readScore", INT_ARRAY_CLASS,
                        boolean.class);
                Method playNextNoteMethod = assertAccessibleMethod("", "playNextNote");
                Method setSilentModeMethod = assertMethod(soundSystemClass, "", "setSilentMode",
                        boolean.class);
                Object alice = null;
                try {
                    Object soundSystem = soundSystemConstructor.newInstance();
                    setSilentModeMethod.invoke(soundSystem, true);
                    alice = violinistConstructor.newInstance("Alice", soundSystem);
                    int[] notes = {60, 61, 62};
                    setSeatMethod.invoke(alice, 4);
                    readScoreMethod.invoke(alice, notes, true);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                    fail(": Failed to set up");
                }
                try {
                    playNextNoteMethod.invoke(alice);
                    Iterator actualNextNote = (Iterator) nextNoteField.get(alice);
                    assertTrue(actualNextNote.hasNext(), "There should be more notes to play");
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                    fail(": Failed to run the test");
                }
            });
        }

    }

}



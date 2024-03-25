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
 **************************************************************************************************/
package tests;

import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tests.TestCoursework.AbstractTestOrchestra;

/**
 * Test Part 2 of the Coursework.
 * <ul>
 * </ul>
 *
 * @author htson - v1.0 - Initial API and implementation
 * @author htson - v1.1 - Turn on silent mode for sound system.
 * @author htson - v1.2 - Update the display names for the tests.
 * @version 1.2
 */
@DisplayName("Test Part 2 for ECS Band Aid Coursework")
public class TestEcsBandAidPart2 {

    /**
     * Test class for {@link orchestra.Orchestra}'s signature.
     *
     * @author htson
     * @version 1.0
     * @see TestCoursework.AbstractTestOrchestra
     */
    @Nested
    @DisplayName("Test orchestra.Orchestra's signature")
    public class TestOrchestraSignature extends AbstractTestOrchestra {

        private Class<?> musicianInterface;

        @BeforeEach
        @Override
        public void setup() {
            super.setup();
            musicianInterface = assertClassOrInterface("", "people.musicians.Musician");
        }

        /**
         * Test the signature of {@link orchestra.Orchestra} class
         */
        @Test
        @DisplayName("Test orchestra.Orchestra's field, constructor, and method signature")
        public void testOrchestra_Signature() {
            assertClass("");
            assertDeclaredField("", "seating", HashMap.class);
            assertConstructor("");
            assertMethod("", int.class, "sitDown", musicianInterface);
            assertMethod("", boolean.class, "isSeated", musicianInterface);
            assertMethod("", "standUp", musicianInterface);
        }

    }

    /**
     * Test class for {@link orchestra.Orchestra}'s specification.
     *
     * @author htson
     * @version 1.0
     * @see TestCoursework.AbstractTestOrchestra
     */
    @Nested
    @DisplayName("Test orchestra.Orchestra's specification")
    public class TestOrchestraSpecification extends AbstractTestOrchestra {

        private Class<?> musicianInterface;

        private Class<?> soundSystemClass;

        private Class<?> violinistClass;

        @BeforeEach
        @Override
        public void setup() {
            super.setup();
            musicianInterface = assertClassOrInterface(PRECONDITION, "people.musicians.Musician");
            soundSystemClass = getClassOrInterface("utils.SoundSystem");
            assertClass(PRECONDITION, soundSystemClass);
            violinistClass = getClassOrInterface("people.musicians.Violinist");
            assertClass(PRECONDITION, violinistClass);
        }


        /**
         * Test the signature of {@link orchestra.Orchestra#Orchestra()} constructor.
         */
        @Test
        @DisplayName("Test orchestra.Orchestra#Orchestra() constructor")
        public void testOrchestra_Constructor() {
            assertTimeout(ofMillis(5000), () -> {
                Field seatingField = assertDeclaredField(PRECONDITION, "seating", HashMap.class);
                Constructor<?> orchestraConstructor = assertConstructor(PRECONDITION);

                // Test the constructor
                final Object[] orchestraArray = new Object[1];
                new TestConstructor(": Failed to create an orchestra using Orchestra() constructor") {
                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        orchestraArray[0] = orchestraConstructor.newInstance();
                    }
                }.run();
                Object orchestra = orchestraArray[0];

                // Test Field seating
                final HashMap<?, ?>[] seatingArray = new HashMap<?, ?>[1];
                new TestField("Failed to get the seating field") {
                    @Override
                    public void testField() throws IllegalAccessException, IllegalArgumentException {
                        seatingArray[0] = (HashMap<?, ?>) seatingField.get(orchestra);
                    }
                }.run();
                Object seating = seatingArray[0];

                // seating must not be null
                assertNotNull(seating, "The seating arrangement should not be null");
            });
        }

        /**
         * Test the specification of {@link orchestra.Orchestra#sitDown(people.musicians.Musician)} method.
         */
        @Test
        @DisplayName("Test orchestra.Orchestra#sitDown(Musician) method")
        public void testOrchestra_addMusician1() {
            assertTimeout(ofMillis(5000), () -> {

                // PRECONDITION
                Field seatingField = assertDeclaredField(PRECONDITION, "seating", HashMap.class);
                Constructor<?> orchestraConstructor = assertConstructor(PRECONDITION);
                Method sitDownMethod = assertMethod(PRECONDITION, int.class, "sitDown",
                        musicianInterface);
                Field seatField = assertInheritedField(violinistClass, PRECONDITION, "seat");
                Method setSilentModeMethod = assertMethod(soundSystemClass, PRECONDITION,"setSilentMode",
                        boolean.class);

                // create a new orchestra
                final Object[] orchestraArray = new Object[1];
                new TestConstructor(PRECONDITION + ": Failed to create an orchestra with Orchestra()") {

                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        orchestraArray[0] = orchestraConstructor.newInstance();
                    }
                }.run();
                Object orchestra = orchestraArray[0];

                // create a sound system
                Constructor<?> soundSystemConstructor = assertConstructor(soundSystemClass, PRECONDITION);
                assertNotNull(soundSystemConstructor,
                        PRECONDITION + ": Constructor Violinist(String, SoundSystem) should not be null");

                final Object[] soundSystemArray = new Object[1];
                new TestConstructor(PRECONDITION + ": Failed to create a sound system with SoundSystem()") {
                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        soundSystemArray[0] = soundSystemConstructor.newInstance();
                    }
                }.run();
                Object soundSystem = soundSystemArray[0];
                new TestMethod(PRECONDITION + ": Failed to call soundSystem.setSilentMode(true)") {
                    @Override
                    public void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        setSilentModeMethod.invoke(soundSystem, true);
                    }
                }.run();

                // create a violinist
                Constructor<?> violinistConstructor = assertConstructor(violinistClass, PRECONDITION,
                        String.class,
                        soundSystemClass);
                assertNotNull(violinistConstructor,
                        PRECONDITION + ": Constructor Violinist(String, SoundSystem) should not be null");
                final Object[] violinist = new Object[2];
                new TestConstructor(
                        PRECONDITION + ": Failed to create violinists with Violinist(String, SoundSystem)") {

                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        violinist[0] = violinistConstructor.newInstance("Alice", soundSystem);
                        violinist[1] = violinistConstructor.newInstance("Bob", soundSystem);
                    }
                }.run();
                Object alice = violinist[0];
                Object bob = violinist[1];

                // TEST
                // Test orchestra.sitDown(alice)
                final Object[] resultArray = new Object[1];
                new TestMethod(": Failed to run orchestra.sitDown(alice)") {
                    @Override
                    protected void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        resultArray[0] = sitDownMethod.invoke(orchestra, alice);
                    }
                }.run();
                Object result = resultArray[0];
                assertEquals(0, result, "Incorrect return result for orchestra.sitDown(alice)");
                // Get field seating
                final HashMap<?, ?>[] seatingArray = new HashMap<?, ?>[1];
                new TestField(PRECONDITION + ": Failed to get the seating field") {
                    @Override
                    public void testField() throws IllegalAccessException, IllegalArgumentException {
                        seatingArray[0] = (HashMap<?, ?>) seatingField.get(orchestra);
                    }
                }.run();
                HashMap<?, ?> seating = seatingArray[0];
                assertTrue(seating.containsValue(alice), "Alice should be added to the seating");
                // Check Alice's sitting
                int seat;
                for (seat = 0; seat < 16; seat++) {
                    if (alice.equals(seating.get(seat))) {
                        break;
                    }
                }
                final int finalSeat1 = seat;
                new TestField(PRECONDITION + "Failed to get the seat field for Alice") {
                    @Override
                    public void testField() throws IllegalAccessException, IllegalArgumentException {
                        assertEquals(finalSeat1, seatField.get(alice),
                                "Inconsistent seat for Alice and the orchestra");
                    }
                }.run();

                // Test orchestra.addMusician(alice) again
                new TestMethod(": Failed to run orchestra.sitDown(alice)") {
                    @Override
                    public void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        resultArray[0] = sitDownMethod.invoke(orchestra, alice);
                    }
                }.run();
                result = resultArray[0];
                assertEquals(2, result, "Incorrect return result for orchestra.sitDown(alice) again");

                // Test orchestra.addMusician(bob) again
                new TestMethod(": Failed to run orchestra.sitDown(bob)") {
                    @Override
                    protected void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        resultArray[0] = sitDownMethod.invoke(orchestra, bob);
                    }
                }.run();
                result = resultArray[0];
                assertEquals(0, result, "Incorrect return result for orchestra.sitDown(bob)");
                // Get field seating
                new TestField(PRECONDITION + ": Failed to get the seating field") {
                    @Override
                    public void testField() throws IllegalAccessException, IllegalArgumentException {
                        seatingArray[0] = (HashMap<?, ?>) seatingField.get(orchestra);
                    }
                }.run();
                seating = seatingArray[0];
                assertTrue(seating.containsValue(bob), "Bob should be added to the seating");
                // Check Bob's sitting
                for (seat = 0; seat < 16; seat++) {
                    if (bob.equals(seating.get(seat))) {
                        break;
                    }
                }
                final int finalSeat2 = seat;
                new TestField(PRECONDITION + "Failed to get the seat field for Bob") {
                    @Override
                    public void testField() throws IllegalAccessException, IllegalArgumentException {
                        assertEquals(finalSeat2, seatField.get(bob),
                                "Inconsistent seat for Bob and the orchestra");
                    }
                }.run();
            });
        }

        /**
         * Test the specification of {@link orchestra.Orchestra#isSeated(people.musicians.Musician)} method.
         */
        @Test
        @DisplayName("Test orchestra.Orchestra#isSeated(Musician) method")
        public void testOrchestra_isSeated() {
            assertTimeout(ofMillis(5000), () -> {

                // PRECONDITION
                Constructor<?> orchestraConstructor = assertConstructor(PRECONDITION);
                Method sitDownMethod = assertMethod(PRECONDITION, int.class, "sitDown",
                        musicianInterface);
                Method isSeatedMethod = assertMethod(PRECONDITION, boolean.class, "isSeated",
                        musicianInterface);
                Method setSilentModeMethod = assertMethod(soundSystemClass, PRECONDITION, "setSilentMode",
                        boolean.class);

                // create a new orchestra
                final Object[] orchestraArray = new Object[1];
                new TestConstructor(PRECONDITION + ": Failed to create an orchestra with Orchestra()") {
                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        orchestraArray[0] = orchestraConstructor.newInstance();
                    }
                }.run();
                Object orchestra = orchestraArray[0];

                // create a sound system
                Constructor<?> soundSystemConstructor = assertConstructor(soundSystemClass, PRECONDITION);
                assertNotNull(soundSystemConstructor,
                        PRECONDITION + ": Constructor Violinist(String, SoundSystem) should not be null");

                final Object[] soundSystemArray = new Object[1];
                new TestConstructor(PRECONDITION + ": Failed to create a sound system with SoundSystem()") {
                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        soundSystemArray[0] = soundSystemConstructor.newInstance();
                    }
                }.run();
                Object soundSystem = soundSystemArray[0];
                new TestMethod(PRECONDITION + ": Failed to call soundSystem.setSilentMode(true)") {
                    @Override
                    public void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        setSilentModeMethod.invoke(soundSystem, true);
                    }
                }.run();

                // create a violinist
                Constructor<?> violinistConstructor = assertConstructor(violinistClass, PRECONDITION,
                        String.class,
                        soundSystemClass);
                assertNotNull(violinistConstructor,
                        PRECONDITION + ": Constructor Violinist(String, SoundSystem) should not be null");
                final Object[] violinist = new Object[2];
                new TestConstructor(
                        PRECONDITION + ": Failed to create violinists with Violinist(String, SoundSystem)") {

                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        violinist[0] = violinistConstructor.newInstance("Alice", soundSystem);
                        violinist[1] = violinistConstructor.newInstance("Bob", soundSystem);
                    }
                }.run();
                Object alice = violinist[0];
                Object bob = violinist[1];
                // Let Alice sit down
                new TestMethod(PRECONDITION + ": Failed to run orchestra.sitDown(alice)") {
                    @Override
                    protected void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        sitDownMethod.invoke(orchestra, alice);
                    }
                }.run();

                // TEST
                // Test orchestra.isSeated(alice)
                new TestMethod(": Failed to run orchestra.isSeated(alice)") {
                    @Override
                    protected void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        assertEquals(true, isSeatedMethod.invoke(orchestra, alice), "Alice should be seated");
                    }
                }.run();

                // Test orchestra.isSeated(bob)
                new TestMethod(": Failed to run orchestra.isSeated(bob)") {
                    @Override
                    protected void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        assertEquals(false, isSeatedMethod.invoke(orchestra, bob), "Bob should not be seated");
                    }
                }.run();

            });

        }

        /**
         * Test the specification of {@link orchestra.Orchestra#playNextNote()} method.
         */
        @Test
        @DisplayName("Test orchestra.Orchestra#standUp(Musician) method")
        public void testOrchestra_standUp() {
            assertTimeout(ofMillis(5000), () -> {
                // PRECONDITION
                Constructor<?> orchestraConstructor = assertConstructor(PRECONDITION);
                Method sitDownMethod = assertMethod(PRECONDITION, int.class, "sitDown",
                        musicianInterface);
                Method standUpMethod = assertMethod(PRECONDITION, "standUp",
                        musicianInterface);
                Method isSeatedMethod = assertMethod(PRECONDITION, boolean.class, "isSeated",
                        musicianInterface);
                Method setSilentModeMethod = assertMethod(soundSystemClass, PRECONDITION, "setSilentMode",
                        boolean.class);

                // create a new orchestra
                final Object[] orchestraArray = new Object[1];
                new TestConstructor(PRECONDITION + ": Failed to create an orchestra with Orchestra()") {

                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        orchestraArray[0] = orchestraConstructor.newInstance();
                    }
                }.run();
                Object orchestra = orchestraArray[0];

                // create a sound system
                Constructor<?> soundSystemConstructor = assertConstructor(soundSystemClass, PRECONDITION);
                assertNotNull(soundSystemConstructor,
                        PRECONDITION + ": Constructor Violinist(String, SoundSystem) should not be null");

                final Object[] soundSystemArray = new Object[1];
                new TestConstructor(PRECONDITION + ": Failed to create a sound system with SoundSystem()") {
                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        soundSystemArray[0] = soundSystemConstructor.newInstance();
                    }
                }.run();
                Object soundSystem = soundSystemArray[0];
                new TestMethod(PRECONDITION + ": Failed to call soundSystem.setSilentMode(true)") {
                    @Override
                    public void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        setSilentModeMethod.invoke(soundSystem, true);
                    }
                }.run();

                // create a violinist
                Constructor<?> violinistConstructor = assertConstructor(violinistClass, PRECONDITION,
                        String.class,
                        soundSystemClass);
                assertNotNull(violinistConstructor,
                        PRECONDITION + ": Constructor Violinist(String, SoundSystem) should not be null");
                final Object[] violinist = new Object[1];
                new TestConstructor(
                        PRECONDITION + ": Failed to create violinists with Violinist(String, SoundSystem)") {
                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        violinist[0] = violinistConstructor.newInstance("Alice", soundSystem);
                    }
                }.run();
                Object alice = violinist[0];
                // Let Alice sit down
                new TestMethod(PRECONDITION + ": Failed to run orchestra.sitDown(alice)") {
                    @Override
                    protected void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        sitDownMethod.invoke(orchestra, alice);
                    }
                }.run();

                // TEST
                // Let Alice stand up
                new TestMethod(": Failed to run orchestra.standUp(alice)") {
                    @Override
                    protected void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        standUpMethod.invoke(orchestra, alice);
                    }
                }.run();
                // Test orchestra.isSeated(alice)
                new TestMethod(": Failed to run orchestra.isSeated(alice)") {
                    @Override
                    protected void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        assertEquals(false, isSeatedMethod.invoke(orchestra, alice),
                                "Alice should not be seated now");
                    }
                }.run();


            });

        }

        /**
         * Test the specification of {@link orchestra.Orchestra#standUp(people.musicians.Musician)} method.
         */
        @Test
        @DisplayName("Test orchestra.Orchestra#playNextNote() method")
        public void testOrchestra_playNextNote() {
            assertTimeout(ofMillis(5000), () -> {
                // PRECONDITION
                Constructor<?> orchestraConstructor = assertConstructor(PRECONDITION);
                Method sitDownMethod = assertMethod(PRECONDITION, int.class, "sitDown",
                        musicianInterface);
                Method playNextNoteMethod = assertMethod(PRECONDITION, "playNextNote");
                Method readScoreMethod = assertAccessibleMethod(violinistClass, PRECONDITION, "readScore", INT_ARRAY_CLASS,
                        boolean.class);
                Field nextNoteField = assertInheritedField(violinistClass, PRECONDITION, "nextNote", INTEGER_ITERATOR_CLASS);
                Method setSilentModeMethod = assertMethod(soundSystemClass, PRECONDITION, "setSilentMode", boolean.class);

                // create a new orchestra
                final Object[] orchestraArray = new Object[1];
                new TestConstructor(PRECONDITION + ": Failed to create an orchestra with Orchestra()") {

                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        orchestraArray[0] = orchestraConstructor.newInstance();
                    }
                }.run();
                Object orchestra = orchestraArray[0];

                // create a sound system
                Constructor<?> soundSystemConstructor = assertConstructor(soundSystemClass, PRECONDITION);

                final Object[] soundSystemArray = new Object[1];
                new TestConstructor(PRECONDITION + ": Failed to create a sound system with SoundSystem()") {
                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        soundSystemArray[0] = soundSystemConstructor.newInstance();
                    }
                }.run();
                Object soundSystem = soundSystemArray[0];
                new TestConstructor(PRECONDITION + ": Failed to call soundSystem.setSilentMode(true)") {
                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        setSilentModeMethod.invoke(soundSystem, true);
                    }
                }.run();

                // create a violinist
                Constructor<?> violinistConstructor = assertConstructor(violinistClass, PRECONDITION,
                        String.class,
                        soundSystemClass);
                assertNotNull(violinistConstructor,
                        PRECONDITION + ": Constructor Violinist(String, SoundSystem) should not be null");
                final Object[] violinist = new Object[2];
                new TestConstructor(
                        PRECONDITION + ": Failed to create violinists with Violinist(String, SoundSystem)") {

                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        violinist[0] = violinistConstructor.newInstance("Alice", soundSystem);
                        violinist[1] = violinistConstructor.newInstance("Bob", soundSystem);
                    }
                }.run();
                Object alice = violinist[0];
                Object bob = violinist[1];
                // Let Alice sit down
                new TestMethod(PRECONDITION + ": Failed to run orchestra.sitDown(alice)") {
                    @Override
                    protected void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        sitDownMethod.invoke(orchestra, alice);
                    }
                }.run();
                // Let Bob sit down
                new TestMethod(PRECONDITION + ": Failed to run orchestra.sitDown(bob)") {
                    @Override
                    protected void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        sitDownMethod.invoke(orchestra, bob);
                    }
                }.run();

                int[] notes = {60};
                // Let Aice read score
                new TestMethod(PRECONDITION + ": Failed to run alice.readScore(notes, true)") {
                    @Override
                    protected void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        readScoreMethod.invoke(alice, notes, true);
                    }
                }.run();
                // Let Bob read score
                new TestMethod(PRECONDITION + ": Failed to run bob.readScore(notes, true)") {
                    @Override
                    protected void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        readScoreMethod.invoke(bob, notes, true);
                    }
                }.run();

                // TEST
                // Let the orchestra play next note
                new TestMethod(PRECONDITION + ": Failed to run orchestra.playNextNote()") {
                    @Override
                    protected void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        playNextNoteMethod.invoke(orchestra);
                    }
                }.run();

                // Test if both Alice and Bob play their notes
                final Iterator<Object>[] nextNote = new Iterator[1];
                // Get the iterator for Alice
                new TestField(PRECONDITION + ": Failed to get alice#nextNote") {
                    @Override
                    public void testField()
                            throws IllegalAccessException, IllegalArgumentException {
                        nextNote[0] = (Iterator<Object>) nextNoteField.get(alice);
                    }
                }.run();
                assertFalse(nextNote[0].hasNext(), "There should be NO more notes to play for Alice");
                // Get the iterator for Bob
                new TestField(PRECONDITION + ": Failed to get bob#nextNote") {
                    @Override
                    public void testField()
                            throws IllegalAccessException, IllegalArgumentException {
                        nextNote[0] = (Iterator<Object>) nextNoteField.get(bob);
                    }
                }.run();
                assertFalse(nextNote[0].hasNext(), "There should be NO more notes to play for Bob");


            });
        }
    }
}

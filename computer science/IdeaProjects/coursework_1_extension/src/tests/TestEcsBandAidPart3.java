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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTimeout;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tests.TestCoursework.AbstractTestComposition;
import tests.TestCoursework.AbstractTestConductor;
import tests.TestCoursework.AbstractTestMusicScore;

/**
 * Test Part 3 of the Coursework.
 * <ul>
 * </ul>
 *
 * @author htson - v1.0 - Initial API and implementation
 * @version 1.0
 */
@DisplayName("Test Part 3 for ECS Band Aid Coursework")
public class TestEcsBandAidPart3 {

    /**
     * Test class for {@link music.MusicScore}'s signature.
     *
     * @author htson
     * @version 1.0
     * @see AbstractTestMusicScore
     */
    @Nested
    @DisplayName("Test the signature of the music.MusicScore class")
    public class TestMusicScoreSignature extends AbstractTestMusicScore {

        /**
         * Test the signature of {@link music.MusicScore} class
         */
        @Test
        @DisplayName("Test music.MusicScore's constructor and methods' signature")
        public void testMusicScore_Signature() {
            assertClass("");
            assertConstructor("", String.class, INT_ARRAY_CLASS, boolean.class);
            assertMethod("", int.class, "getInstrumentID");
            assertMethod("", INT_ARRAY_CLASS, "getNotes");
            assertMethod("", boolean.class, "isSoft");
        }

    }

    /**
     * Test class for {@link music.MusicScore}'s specification.
     *
     * @author htson
     * @version 1.0
     * @see AbstractTestMusicScore
     */
    @Nested
    @DisplayName("Test the specification of the music.MusicScore class")
    public class TestMusicScoreSpecification extends AbstractTestMusicScore {

        /**
         * Test the signature of {@link music.MusicScore#MusicScore(String, int[], boolean)}
         * constructor.
         */
        @Test
        @DisplayName("Test music.MusicScore#MusicScore(String, int[], boolean) constructor")
        public void testMusicScore_Constructor() {
            assertTimeout(ofMillis(5000), () -> {
                Constructor<?> musicScoreConstructor = assertConstructor(PRECONDITION, String.class,
                        INT_ARRAY_CLASS, boolean.class);

                // Test the constructor
                new TestConstructor(
                        ": Failed to create a music score using MusicScore(String, int[], boolean) constructor") {
                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        musicScoreConstructor.newInstance("Piano", new int[]{60, 61}, true);
                    }
                }.run();
            });
        }

        /**
         * Test the signature of {@link music.MusicScore#getInstrumentID()} method.
         */
        @Test
        @DisplayName("Test music.MusicScore#getInstrument() method")
        public void testMusicScore_getInstrumentID() {
            assertTimeout(ofMillis(5000), () -> {
                Constructor<?> musicScoreConstructor = assertConstructor(PRECONDITION, String.class,
                        INT_ARRAY_CLASS, boolean.class);
                Method getInstrumentIDMethod = assertMethod("", int.class, "getInstrumentID");

                // Construct some object
                final Object[] musicScoreArray = new Object[1];
                new TestConstructor(PRECONDITION
                        + ": Failed to create a music score using MusicScore(String, int[], boolean) constructor") {
                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        musicScoreArray[0] = musicScoreConstructor.newInstance("Violin", new int[]{60, 61},
                                true);
                    }
                }.run();
                Object violinMusicScore = musicScoreArray[0];

                // TEST
                new TestMethod(": Failed to call violinMusicScore.getInstrumentID()") {

                    @Override
                    protected void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        assertEquals(41, getInstrumentIDMethod.invoke(violinMusicScore),
                                "Incorrect ID for Violin");
                    }
                }.run();
            });
        }

        /**
         * Test the signature of {@link music.MusicScore#getNotes()} method.
         */
        @Test
        @DisplayName("Test music.MusicScore#getNotes() method")
        public void testMusicScore_getNotes() {
            assertTimeout(ofMillis(5000), () -> {
                Constructor<?> musicScoreConstructor = assertConstructor(PRECONDITION, String.class,
                        INT_ARRAY_CLASS, boolean.class);
                Method getNotesMethod = assertMethod(PRECONDITION, INT_ARRAY_CLASS, "getNotes");

                // Construct some object
                final Object[] musicScoreArray = new Object[1];
                new TestConstructor(PRECONDITION
                        + ": Failed to create a music score using MusicScore(String, int[], boolean) constructor") {
                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        musicScoreArray[0] = musicScoreConstructor.newInstance("Violin", new int[]{60, 61},
                                true);
                    }
                }.run();
                Object violinMusicScore = musicScoreArray[0];

                // TEST
                new TestMethod(": Failed to call violinMusicScore.getNotes()") {
                    @Override
                    protected void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        int[] notes = (int[]) getNotesMethod.invoke(violinMusicScore);
                        assertArrayEquals(new int[]{60, 61}, notes, "Incorrect notes for the music score");
                    }
                }.run();
            });
        }

        /**
         * Test the signature of {@link music.MusicScore#isSoft()} method.
         */
        @Test
        @DisplayName("Test music.MusicScore#isSoft() method")
        public void testMusicScore_isSoft() {
            assertTimeout(ofMillis(5000), () -> {
                Constructor<?> musicScoreConstructor = assertConstructor(PRECONDITION, String.class,
                        INT_ARRAY_CLASS, boolean.class);
                Method isSoftMethod = assertMethod(PRECONDITION, boolean.class, "isSoft");

                // Construct some object
                final Object[] musicScoreArray = new Object[1];
                new TestConstructor(PRECONDITION
                        + ": Failed to create a music score using MusicScore(String, int[], boolean) constructor") {
                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        musicScoreArray[0] = musicScoreConstructor.newInstance("Violin", new int[]{60, 61},
                                true);
                    }
                }.run();
                Object violinMusicScore = musicScoreArray[0];

                // TEST
                new TestMethod(": Failed to call violinMusicScore.isSoft()") {
                    @Override
                    protected void testMethod()
                            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        assertEquals(true, isSoftMethod.invoke(violinMusicScore),
                                "The music score should be soft");
                    }
                }.run();
            });
        }

    }

    /**
     * Test class for {@link music.Composition}'s signature.
     *
     * @author htson
     * @version 1.0
     * @see AbstractTestComposition
     */
    @Nested
    @DisplayName("Test the signature of the music.Composition interface")
    public class TestCompositionSignature extends AbstractTestComposition {

        Class<?> musicScoreArrayClass;

        @Override
        @BeforeEach
        public void setup() {
            super.setup();
            Class<?> musicScoreClass = assertClassOrInterface(PRECONDITION, "music.MusicScore");
            Object musicScoreArray = Array.newInstance(musicScoreClass, 3);
            musicScoreArrayClass = musicScoreArray.getClass();
        }

        /**
         * Test the signature of {@link music.Composition} class
         */
        @Test
        @DisplayName("Test music.Composition's methods' signature")
        public void testComposition_Signature() {
            assertInterface("");
            assertMethod("", String.class, "getName");
            assertMethod("", "addScore", String.class, STRING_LIST_CLASS, boolean.class);
            assertMethod("", musicScoreArrayClass, "getScores");
            assertMethod("", int.class, "getLength");
            assertMethod("", int.class, "getNoteLength");
        }

    }

    /**
     * Test class for {@link people.conductors.Conductor}'s signature.
     *
     * @author htson
     * @version 1.0
     * @see AbstractTestConductor
     */
    @Nested
    @DisplayName("Test the signature of the people.conductors.Conductor class")
    public class TestConductorSignature extends AbstractTestConductor {

        Class<?> soundSystemClass;

        Class<?> personClass;

        Class<?> musicianInterface;

        Class<?> compositionInterface;

        @Override
        @BeforeEach
        public void setup() {
            super.setup();
            soundSystemClass = assertClassOrInterface(PRECONDITION, "utils.SoundSystem");
            personClass = assertClassOrInterface(PRECONDITION, "people.Person");
            musicianInterface = assertClassOrInterface(PRECONDITION, "people.musicians.Musician");
            compositionInterface = assertClassOrInterface(PRECONDITION, "music.Composition");
        }

        /**
         * Test the signature of {@link people.conductors.Conductor} class
         */
        @Test
        @DisplayName("Test people.conductors.Conductor's constructors and methods' signature")
        public void testConductor_Signature() {
            assertClass("");
            assertImplementOrExtend("", personClass);
            assertConstructor("", String.class, soundSystemClass);
            assertAccessibleMethod("", "registerMusician", musicianInterface);
            assertAccessibleMethod("", "playComposition", compositionInterface);
        }

    }

    /**
     * Test class for {@link people.conductors.Conductor}'s specification.
     *
     * @author htson
     * @version 1.0
     * @see AbstractTestConductor
     */
    @Nested
    @DisplayName("Test the specification of the people.conductors.Conductor class")
    public class TestConductorSpecification extends AbstractTestConductor {

        Class<?> soundSystemClass;

        Class<?> personClass;

        Class<?> musicianInterface;

        Class<?> violinistClass;

        Class<?> compositionInterface;

        @Override
        @BeforeEach
        public void setup() {
            super.setup();
            soundSystemClass = assertClassOrInterface(PRECONDITION, "utils.SoundSystem");
            personClass = assertClassOrInterface(PRECONDITION, "people.Person");
            musicianInterface = assertClassOrInterface(PRECONDITION, "people.musicians.Musician");
            compositionInterface = assertClassOrInterface(PRECONDITION, "music.Composition");
            violinistClass = assertClassOrInterface(PRECONDITION, "people.musicians.Violinist");
        }

        /**
         * Test the {@link people.conductors.Conductor#Conductor(String, utils.SoundSystem)} constructor.
         */
        @Test
        @DisplayName("Test people.conductors.Conductor#Conductor(String, SoundSystem) constructor")
        public void testConductor_constructor() {
            assertTimeout(ofMillis(5000), () -> {
                Constructor<?> conductorConstructor = assertConstructor(PRECONDITION, String.class,
                        soundSystemClass);
                Method setSilentModeMethod = assertMethod(soundSystemClass, PRECONDITION, "setSilentMode",
                        boolean.class);
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

                // Test the constructor
                new TestConstructor(
                        ": Failed to create a conductor using Conductor(String, SoundSystem) constructor") {
                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        conductorConstructor.newInstance("Bob Geldof", soundSystem);
                    }
                }.run();

            });
        }

        /**
         * Test the {@link people.conductors.Conductor#registerMusician(people.musicians.Musician)} method.
         */
        @Test
        @DisplayName("Test people.conductors.Conductor#registerMusician(Musician) method")
        public void testConductor_registerMusician() {
            assertTimeout(ofMillis(5000), () -> {
                Constructor<?> conductorConstructor = assertConstructor(PRECONDITION, String.class,
                        soundSystemClass);
                Method registerMusicianMethod = assertMethod(PRECONDITION, "registerMusician",
                        musicianInterface);
                Method setSilentModeMethod = assertMethod(soundSystemClass, PRECONDITION, "setSilentMode",
                        boolean.class);
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
                final Object[] conductorArray = new Object[1];
                new TestConstructor(PRECONDITION +
                        ": Failed to create a conductorArray using Conductor(String, SoundSystem) constructor") {
                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        conductorArray[0] = conductorConstructor.newInstance("Bob Geldof", soundSystem);
                    }
                }.run();
                Object conductor = conductorArray[0];
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

                // Test registerMusician
                new TestConstructor(PRECONDITION +
                        ": Failed to create a conductorArray using Conductor(String, SoundSystem) constructor") {
                    @Override
                    public void testConstructor()
                            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        registerMusicianMethod.invoke(conductor, alice);
                    }
                }.run();
            });
        }

    }

}

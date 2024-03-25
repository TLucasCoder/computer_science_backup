package tests;

/***************************************************************************************************
 * Copyright (c) 2022 University of Southampton.
 *
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

/*
 * Abstract class for testing the ECS Orchestra coursework.
 *
 * @author Son Hoang - v1.0.0 - Initial API and implementation.
 * @version 1.0.0
 */
public abstract class TestCoursework {

    /**
     * Abstract test class for {@link people.Person}.
     *
     * @author htson
     * @version 1.0
     * @see AbstractTestClass
     */
    protected abstract static class AbstractTestPerson extends AbstractTestClass {

        /**
         * We are testing {@link people.Person}.
         */
        protected Class<?> getTestClass() {
            return assertClassOrInterface("", "people.Person");
        }
    }

    /**
     * Abstract test class for {@link people.musicians.Musician}.
     *
     * @author htson
     * @version 1.0
     * @see AbstractTestClass
     */
    protected abstract static class AbstractTestMusician extends AbstractTestClass {

        protected Class<?> getTestClass() {
            return assertClassOrInterface("", "people.musicians.Musician");
        }
    }

    /**
     * Abstract test class for {@link people.musicians.Violinist}.
     *
     * @author htson
     * @version 1.0
     * @see AbstractTestClass
     */
    protected abstract static class AbstractTestViolinist extends AbstractTestClass {

        protected Class<?> getTestClass() {
            return assertClassOrInterface("", "people.musicians.Violinist");
        }
    }

    /**
     * Abstract test class for {@link people.musicians.Cellist}.
     *
     * @author htson
     * @version 1.0
     * @see AbstractTestClass
     */
    protected abstract static class AbstractTestCellist extends AbstractTestClass {

        protected Class<?> getTestClass() {
            return assertClassOrInterface("", "people.musicians.Cellist");
        }
    }

    /**
     * Abstract test class for {@link people.musicians.Pianist}.
     *
     * @author htson
     * @version 1.0
     * @see AbstractTestClass
     */
    protected abstract static class AbstractTestPianist extends AbstractTestClass {

        protected Class<?> getTestClass() {
            return assertClassOrInterface("", "people.musicians.Pianist");
        }
    }

    /**
     * Abstract test class for {@link orchestra.Orchestra}.
     *
     * @author htson
     * @version 1.0
     * @see AbstractTestClass
     */
    protected abstract static class AbstractTestOrchestra extends AbstractTestClass {

        protected Class<?> getTestClass() {
            return assertClassOrInterface("", "orchestra.Orchestra");
        }
    }

    /**
     * Abstract test class for {@link music.MusicScore}.
     *
     * @author htson
     * @version 1.0
     * @see AbstractTestClass
     */
    protected abstract static class AbstractTestMusicScore extends AbstractTestClass {

        protected Class<?> getTestClass() {
            return assertClassOrInterface("", "music.MusicScore");
        }
    }

    /**
     * Abstract test class for {@link music.Composition}.
     *
     * @author htson
     * @version 1.0
     * @see AbstractTestClass
     */
    protected abstract static class AbstractTestComposition extends AbstractTestClass {

        protected Class<?> getTestClass() {
            return assertClassOrInterface("", "music.Composition");
        }
    }

    /**
     * Abstract test class for {@link people.conductors.Conductor}.
     *
     * @author htson
     * @version 1.0
     * @see AbstractTestClass
     */
    protected abstract static class AbstractTestConductor extends AbstractTestClass {

        protected Class<?> getTestClass() {
            return assertClassOrInterface("", "people.conductors.Conductor");
        }
    }

}


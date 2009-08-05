/*******************************************************************************
 Copyright 2009 Sun Microsystems, Inc.,
 4150 Network Circle, Santa Clara, California 95054, U.S.A.
 All rights reserved.

 U.S. Government Rights - Commercial software.
 Government users are subject to the Sun Microsystems, Inc. standard
 license agreement and applicable provisions of the FAR and its supplements.

 Use is subject to license terms.

 This distribution may include materials developed by third parties.

 Sun, Sun Microsystems, the Sun logo and Java are trademarks or registered
 trademarks of Sun Microsystems, Inc. in the U.S. and other countries.
 ******************************************************************************/

package com.sun.fortress.useful;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class BASetJUTest extends TestCaseWrapper {

    public static void main(String[] args) {
        junit.swingui.TestRunner.run(BASetJUTest.class);
    }

    BASet<String> t = empty();

    String[] animals = {
            "alpaca", "ant", "auk", "bat", "beetle", "bison", "buffalo", "camel", "cat", "cavy", "crab", "deer",
            "dingo", "dodo", "dog", "dove", "eagle", "eel", "eland", "elephant", "elk", "emu", "finch", "gar",
            "giraffe", "gnu", "guanaco", "gull", "hawk", "hedgehog", "hyena", "ibis", "iguana", "jackal", "jaguar",
            "kangaroo", "koala", "lemur", "leopard", "llama", "manatee", "mule", "narwhal", "nutria", "octopus",
            "osprey", "ostrich", "owl", "penguin", "pigeon", "piranha", "puffin", "quagga", "quail", "quokka", "rat",
            "ray", "seal", "shark", "snake", "spider", "tern", "tiger", "turtle", "unicorn", "vicuna", "vole",
            "vulture", "walrus", "warthog", "worm", "xiphias", "yak", "zebra"
    };

    private static BASet<String> empty() {
        return new BASet<String>(String.CASE_INSENSITIVE_ORDER);
    }

    public void testRemoveIterator() {
        t.addArray(animals);
        int all = t.size();
        assertEquals(t.size(), animals.length);
        int odds = 0;
        BASet<String> o = new BASet<String>(String.CASE_INSENSITIVE_ORDER);
        for (Iterator<String> i = t.iterator(); i.hasNext();) {
            String s = i.next();
            if (1 == (s.length() & 1)) {
                odds++;
                i.remove();
                o.add(s);
            }
        }
        assertEquals(t.size(), all - odds);
        assertTrue(odds > 0);

        for (String s : o) {
            assertEquals(s.length() & 1, 1);
        }

        for (String s : t) {
            assertEquals(s.length() & 1, 0);
        }

    }

    public void testEmpty() {
        assertEquals(t.size(), 0);
        assertEquals(t.min(), null);
        assertEquals(t.max(), null);
        t.ok();
    }

    public void testOne() {
        t.add("cat");
        assertEquals(t.size(), 1);
        assertEquals(t.min(), "cat");
        assertEquals(t.max(), "cat");
        t.ok();
    }

    public void testAddsAscending() {
        int l = animals.length;

        for (int i = 0; i < l; i++) {
            t.add(animals[i]);
            assertEquals(t.size(), i + 1);
            assertEquals(t.min(), animals[0]);
            assertEquals(t.max(), animals[i]);
            t.ok();
        }
        t.ok();
    }

    public void testAddsDescending() {
        int l = animals.length;

        for (int i = 0; i < l; i++) {
            int j = l - 1 - i;
            t.add(animals[j]);
            assertEquals(t.size(), i + 1);
            assertEquals(t.min(), animals[j]);
            assertEquals(t.max(), animals[l - 1]);
            t.ok();
        }
        t.ok();
    }

    public void testAddsDeletesAscending() {
        testAddsAscending();
        int l = animals.length;
        for (int i = 0; i < l; i++) {
            assertEquals(t.min(), animals[i]);
            assertEquals(t.max(), animals[l - 1]);
            t.remove(animals[i]);
            assertEquals(t.size(), l - i - 1);
            t.ok();
        }
    }

    public void testAddsDeletesDescending() {
        testAddsAscending();
        int l = animals.length;
        for (int i = 0; i < l; i++) {
            int j = l - 1 - i;
            assertEquals(t.min(), animals[0]);
            assertEquals(t.max(), animals[j]);
            t.remove(animals[j]);
            assertEquals(t.size(), j);
            t.ok();
        }
    }

    public void testAddsDeletesRandom() {

        Random r = new Random(0x12345555);

        for (int k = 0; k < 1000; k++) {

            testAddsAscending();
            int l = animals.length;
            for (int i = 0; i < l; i++) {
                int j = r.nextInt(l - i);
                String s = t.getKey(j);
                t.remove(s);
                assertEquals(t.size(), l - i - 1);
                t.ok();
            }
        }
    }

    BASet<String> randomSet(Random r) {
        BASet<String> s = empty();
        for (int k = 0; k < 15; k++) {
            int j = r.nextInt(animals.length);
            s.add(animals[j]);
        }
        s.ok();
        return s;
    }

    public void testUnionRandom() {
        Random r = new Random();
        BASet<String> a = randomSet(r);
        BASet<String> b = randomSet(r);
        BASet<String> c = a.copy();
        c.addAll(b);
        c.ok();
        assertTrue(c.containsAll(a));
        assertTrue(c.containsAll(b));
        for (String sc : c) {
            assertTrue(a.contains(sc) || b.contains(sc));
        }
    }

    public void testIntersectionRandom() {
        Random r = new Random();
        BASet<String> a = randomSet(r);
        BASet<String> b = randomSet(r);
        BASet<String> c = a.copy();
        c.retainAll(b);
        c.ok();
        for (String sc : c) {
            assertTrue(a.contains(sc));
            assertTrue(b.contains(sc));
        }
        for (String sa : a) {
            assertEquals(b.contains(sa), c.contains(sa));
        }
    }

    public void testToArray() {
        testAddsAscending();
        assertTrue(Arrays.equals(animals, t.toArray(new String[0])));
    }

}

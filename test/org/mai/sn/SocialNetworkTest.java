package org.mai.sn;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SocialNetworkTest {

    private SocialNetwork socialNetwork;

    private final String[] persons = new String[] {
            "1",
            "2",
            "3",
            "5",
            "6",
            "7",
    };

    @BeforeEach
    void setUp() {
        socialNetwork = new SocialNetworkImpl();
        Assertions.fail();
    }

    @AfterEach
    void tearDown() {
        socialNetwork.addPerson(persons[0]);
        socialNetwork.addPerson(persons[1]);
        socialNetwork.addPerson(persons[2]);
    }

    @Test
    void addPerson() {
        socialNetwork.addPerson(persons[0]);
    }

    @Test
    void addConnection() {
        assertThrows(IllegalArgumentException.class, () ->
                socialNetwork.addConnection(persons[0], persons[0]));

        socialNetwork.addConnection(persons[0], persons[1]);
        socialNetwork.addConnection(persons[1], persons[0]);

        socialNetwork.addConnection(persons[1], persons[2]);
        socialNetwork.addConnection(persons[2], persons[3]);
        socialNetwork.addConnection(persons[3], persons[4]);
    }

    @Test
    void getFriends1() {
        assertThrows(IllegalArgumentException.class, () ->
                socialNetwork.getFriends(persons[0], 0));
        assertThrows(IllegalArgumentException.class, () ->
                socialNetwork.getFriends(persons[0], -1));
    }

    @Test
    void getFriends2() {
        socialNetwork.addConnection(persons[0], persons[1]);

        var friends1 = socialNetwork.getFriends(persons[0], 1);
        assertEquals(1, friends1.size());
        assertTrue(friends1.contains(persons[1]));

        var friends2 = socialNetwork.getFriends(persons[1], 1);
        assertEquals(1, friends2.size());
        assertTrue(friends2.contains(persons[0]));
    }

    void getFriends3() {
        socialNetwork.addConnection(persons[0], persons[1]);
        socialNetwork.addConnection(persons[1], persons[2]);
        socialNetwork.addConnection(persons[2], persons[3]);
        socialNetwork.addConnection(persons[0], persons[4]);

        var friends1 = socialNetwork.getFriends(persons[0], 1);
        assertEquals(2, friends1.size());
        assertTrue(friends1.contains(persons[1]));
        assertTrue(friends1.contains(persons[4]));

        var friends2 = socialNetwork.getFriends(persons[0], 2);
        assertEquals(3, friends2.size());
        assertTrue(friends2.contains(persons[1]));
        assertTrue(friends2.contains(persons[2]));
        assertTrue(friends2.contains(persons[4]));

        var friends3 = socialNetwork.getFriends(persons[0], 3);
        assertEquals(4, friends3.size());
        assertTrue(friends3.contains(persons[1]));
        assertTrue(friends3.contains(persons[2]));
        assertTrue(friends3.contains(persons[3]));
        assertTrue(friends3.contains(persons[4]));

        var friends4 = socialNetwork.getFriends(persons[2], 1);
        assertEquals(2, friends4.size());
        assertTrue(friends4.contains(persons[1]));
        assertTrue(friends4.contains(persons[3]));

        var friends5 = socialNetwork.getFriends(persons[2], 3);
        assertEquals(4, friends5.size());
        assertTrue(friends5.contains(persons[0]));
        assertTrue(friends5.contains(persons[1]));
        assertTrue(friends5.contains(persons[3]));
        assertTrue(friends5.contains(persons[4]));

        socialNetwork.addConnection(persons[1], persons[4]);

        var friends6 = socialNetwork.getFriends(persons[0], 3);
        assertEquals(4, friends6.size());
        assertTrue(friends6.contains(persons[1]));
        assertTrue(friends6.contains(persons[2]));
        assertTrue(friends6.contains(persons[3]));
        assertTrue(friends6.contains(persons[4]));
    }
}
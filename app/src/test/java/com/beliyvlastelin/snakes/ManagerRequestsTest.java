package com.beliyvlastelin.snakes;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.ArrayList;


import static org.junit.Assert.*;

/**
 * Created by mikola on 10.12.2016.
 */
public class ManagerRequestsTest {

    ManagerRequests manager;
    

    @Test
    public void getListRoom() throws Exception {
        String data = new String("{'result': 'SUCCESS','room_list':[{'name':'room','number_users':'5','max_number_users':'10'}]}");

        final int roomNumbers = 1;
        final int someRoomNumbers = 10;
        final int numberUsersInFirstRoom = 5;
        final int falseMaxNumberUsersInFirstRoom = 9;

        ArrayList<Room> romms = (ArrayList<Room>) ManagerRequests.getListRoom(data);

        assertEquals(romms.size(), roomNumbers);

        assertEquals(romms.get(0).getUserCount(), numberUsersInFirstRoom);

        assertFalse(romms.get(0).getMaxUserCount() == falseMaxNumberUsersInFirstRoom);

        assertTrue(romms.size() < someRoomNumbers);


    }

    @Test
    public void getResponce() throws Exception {
        String test = "test";

        //mockito
        manager = mock(ManagerRequests.class);
        when(manager.getResponce()).thenReturn(test);
        assertEquals(test,manager.getResponce());

    }


}
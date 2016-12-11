package com.beliyvlastelin.snakes;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.beliyvlastelin.snakes.Constants.USER_NAME;
import static com.beliyvlastelin.snakes.Constants.USER_PASSWORD;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by mikola on 10.12.2016.
 */
public class ManagerRequestsTest {

    ManagerRequests manager;

    @Before
    public void setUp() throws Exception {
        manager = mock(ManagerRequests.class);
    }

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
    public void sendRequest() throws Exception {
        HashMap<String, String> map = new HashMap<>();

        map.put(USER_NAME, "name");
        map.put(USER_PASSWORD, "password");
        when(ManagerRequests.get(Constants.ip, Constants.port)).thenReturn(manager);
        manager.sendRequest(Constants.POST_REQUEST_SIGNIN, map);

//


    }


}
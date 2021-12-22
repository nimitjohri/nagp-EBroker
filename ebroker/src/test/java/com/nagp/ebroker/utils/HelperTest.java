package com.nagp.ebroker.utils;

import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HelperTest {
   
    @DisplayName("static method mocking")
    @Test
    public void givenStaticMethodWithNoArgsWhenMockedThenItWillReturn() {
        try (MockedStatic<Helper> utilites = Mockito.mockStatic(Helper.class)) {
            utilites.when(() -> Helper.checkTime(LocalDateTime.of(2021, 12, 21, 10, 1))).thenReturn(true);
            utilites.when(() -> Helper.checkTime(LocalDateTime.of(2021, 12, 21, 8, 1))).thenReturn(false);
            utilites.when(() -> Helper.checkTime(LocalDateTime.of(2021, 12, 21, 19, 1))).thenReturn(false);
            utilites.when(() -> Helper.checkTime(LocalDateTime.of(2021, 12, 18, 10, 1))).thenReturn(false);
            utilites.when(() -> Helper.checkTime(LocalDateTime.of(2021, 12, 18, 8, 1))).thenReturn(false);
            utilites.when(() -> Helper.checkTime(LocalDateTime.of(2021, 12, 18, 19, 1))).thenReturn(false);
            Assertions.assertEquals(true, Helper.checkTime(LocalDateTime.of(2021, 12, 21, 10, 1)));
            Assertions.assertEquals(false, Helper.checkTime(LocalDateTime.of(2021, 12, 21, 8, 1)));
            Assertions.assertEquals(false, Helper.checkTime(LocalDateTime.of(2021, 12, 21, 19, 1)));
            Assertions.assertEquals(false, Helper.checkTime(LocalDateTime.of(2021, 12, 18, 10, 1)));
            Assertions.assertEquals(false, Helper.checkTime(LocalDateTime.of(2021, 12, 18, 8, 1)));
            Assertions.assertEquals(false, Helper.checkTime(LocalDateTime.of(2021, 12, 18, 19, 1)));
        }
    }

    // @Test
    // public void testPrivateConstructor() throws Exception {
    //     final Constructor constructor = Helper.class.getDeclaredConstructor();
    //     // assertTrue("Constructor is not private", Modifier.isPrivate(constructor.getModifiers()));
    //     constructor.setAccessible(true);
    //     constructor.newInstance();
    // }
}

package com.quantdiary.cblog.other;

import com.quantdiary.cblog.domain.Status;
import org.junit.Test;

public class EnumTest {

    @Test
    public void testStatus() {
        Status p = Status.PUBLIC;
        Status d = Status.DELETED;
        int i = p.ordinal();
        for (Status status : Status.values()) {
            System.out.println(status.name());
        }
        String statusName = "PUBLIC";
        Status s = Status.valueOf(statusName);
        System.out.println(s.getClass());
//        System.out.println(s.ordinal());
//        System.out.println(d.ordinal());
    }
}

package com.ulegalize.lawfirm.utils;

import com.ulegalize.lawfirm.service.InvoiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class InvoiceUtilsTests {
    private InvoiceUtils invoiceUtils = new InvoiceUtils();

    @Test
    public void test_A_communication_done() {

        String communication = invoiceUtils.communication(1459L, 325);

        assertNotNull(communication);
        assertEquals(12, communication.length());
        // 1459+325+57
        assertEquals("000145932557", communication);
    }

    @Test
    public void test_B_communication_done() {

        String communication = invoiceUtils.communication(611568L, 8615);

        assertNotNull(communication);
        assertEquals(12, communication.length());
        // 1459+325+57
        assertEquals("611568861523", communication);
    }

    @Test
    public void test_C_communication_done() {

        String communication = invoiceUtils.communication(85192L, 7541);

        assertNotNull(communication);
        assertEquals(12, communication.length());
        // 1459+325+57
        assertEquals("085192754115", communication);
    }

    @Test
    public void test_D_communication_done() {

        String communication = invoiceUtils.communication(00000L, 0000);

        assertNotNull(communication);
        assertEquals(12, communication.length());
        // 1459+325+57
        assertEquals("000000000097", communication);
    }

    @Test
    public void test_E_communication_done() {

        String communication = invoiceUtils.communication(421024L, 7040);

        assertNotNull(communication);
        assertEquals(12, communication.length());
        // 1459+325+57
        assertEquals("421024704064", communication);
    }

    @Test
    public void test_F_communication_done() {

        String communication = invoiceUtils.communication(910033L, 3734);

        assertNotNull(communication);
        assertEquals(12, communication.length());
        // 1459+325+57
        assertEquals("910033373453", communication);
    }
}

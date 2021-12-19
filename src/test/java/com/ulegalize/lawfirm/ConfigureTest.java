package com.ulegalize.lawfirm;

import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Rollback
public abstract class ConfigureTest {
}

package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.ItemStringDto;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.TLangues;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class LanguageRepositoryTests extends EntityTest {
    @Autowired
    private TLanguesRepository languesRepository;

    @Test
    public void test_A_getLanguages() {
        TLangues language = createLanguage();

        List<ItemStringDto> itemStringDtos = languesRepository.findAllOrderByLgDesc();
        assertNotNull(itemStringDtos);

        assertEquals(1, itemStringDtos.size());
        assertEquals(language.getIdLg(), itemStringDtos.get(0).getValue());

    }
}

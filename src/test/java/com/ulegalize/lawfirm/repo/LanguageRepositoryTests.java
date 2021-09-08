package com.ulegalize.lawfirm.repo;

import com.ulegalize.dto.ItemStringDto;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.TLangues;
import com.ulegalize.lawfirm.repository.TLanguesRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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

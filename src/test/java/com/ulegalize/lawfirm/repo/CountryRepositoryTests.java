package com.ulegalize.lawfirm.repo;

import com.ulegalize.dto.ItemStringDto;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.TCountries;
import com.ulegalize.lawfirm.repository.TCountriesRepository;
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
public class CountryRepositoryTests extends EntityTest {
    @Autowired
    private TCountriesRepository countriesRepository;

    @Test
    public void test_A_getLanguages() {
        TCountries countries = createTCountries();

        List<ItemStringDto> itemStringDtos = countriesRepository.findAllOrderBy();
        assertNotNull(itemStringDtos);

        assertEquals(1, itemStringDtos.size());
        assertEquals(countries.getAlpha3(), itemStringDtos.get(0).getValue());

    }
}

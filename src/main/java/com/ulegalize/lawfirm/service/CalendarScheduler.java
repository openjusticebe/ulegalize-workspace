package com.ulegalize.lawfirm.service;

import com.ulegalize.lawfirm.exception.LawfirmBusinessException;

public interface CalendarScheduler {
    void executeInfo() throws LawfirmBusinessException;

    void executeInfoScheduler(Long userId) throws LawfirmBusinessException;
}

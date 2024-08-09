package com.woohaengshi.backend.service.timer;

import com.woohaengshi.backend.dto.response.studyrecord.ShowTimerResponse;

public interface TimerService {
    ShowTimerResponse getTimer(Long memberId);
}

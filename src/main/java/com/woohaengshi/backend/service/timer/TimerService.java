package com.woohaengshi.backend.service.timer;

import com.woohaengshi.backend.dto.response.timer.ShowTimerResponse;

public interface TimerService {
    ShowTimerResponse getTimer(Long memberId);
}

package com.woohaengshi.backend.repository.studyrecord;

import com.woohaengshi.backend.dto.result.ShowCalendarResult;

import java.util.List;

public interface StudyRecordCustomRepository {

    List<ShowCalendarResult> findStudyRecordInCalendar(int year, int month, Long memberId);
}

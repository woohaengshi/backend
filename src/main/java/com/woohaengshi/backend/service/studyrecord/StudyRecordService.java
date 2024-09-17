package com.woohaengshi.backend.service.studyrecord;

import com.woohaengshi.backend.dto.request.studyrecord.EditSubjectAndCommentRequest;
import com.woohaengshi.backend.dto.request.studyrecord.SaveCommentRequest;
import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;
import com.woohaengshi.backend.dto.response.studyrecord.ShowMonthlyRecordResponse;
import com.woohaengshi.backend.dto.response.studyrecord.ShowYearlyRecordResponse;

import java.time.YearMonth;

public interface StudyRecordService {
    void save(SaveRecordRequest request, Long memberId);

    ShowYearlyRecordResponse showYearlyRecord(int year, Long memberId);

    ShowMonthlyRecordResponse getMonthlyRecord(YearMonth date, Long memberId);

    void saveComment(SaveCommentRequest request, Long memberId);

    void editSubjectsAndComment(EditSubjectAndCommentRequest request, Long memberId);
}

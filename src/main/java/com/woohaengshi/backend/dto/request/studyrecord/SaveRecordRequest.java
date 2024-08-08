package com.woohaengshi.backend.dto.request.studyrecord;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.member.Member;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SaveRecordRequest {

    @PastOrPresent
    private LocalDate date;
    @Min(value = 1, message = "공부 기록은 1초 부터 저장할 수 있습니다. ")
    private int time;
    private List<String> subjects = new ArrayList<>();

    private SaveRecordRequest() {}

    public SaveRecordRequest(LocalDate date, int time, List<String> subjects) {
        this.date = date;
        this.time = time;
        this.subjects = subjects;
    }

    public StudyRecord toStudyRecord(Member member) {
        return StudyRecord.builder().date(date).time(time).member(member).build();
    }

}

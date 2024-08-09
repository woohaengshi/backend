package com.woohaengshi.backend.service.subject;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.Subject;
import com.woohaengshi.backend.dto.response.studyrecord.FindTimerResponse;
import com.woohaengshi.backend.dto.response.subject.FindSubjectsResponse;
import com.woohaengshi.backend.repository.StudyRecordRepository;
import com.woohaengshi.backend.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubjectServiceImpl implements SubjectService {

    private final StudyRecordRepository studyRecordRepository;
    private final SubjectRepository subjectRepository;

    @Override
    @Transactional
    public FindTimerResponse findTimer(Long memberId) {
        List<FindSubjectsResponse> subjectsResponses = findSubjectsResponses(memberId);
        int todayStudyTime = findTodayStudyTime(memberId, findTodayDate());
        return new FindTimerResponse(todayStudyTime, subjectsResponses);
    }

    private List<FindSubjectsResponse> findSubjectsResponses(Long memberId) {
        Stream<Subject> subjectStream = subjectRepository.findAllByMemberIdOrderByNameAsc(memberId);
        return subjectStream
                .map(subject -> new FindSubjectsResponse(subject.getId(), subject.getName()))
                .collect(Collectors.toList());
    }

    private int findTodayStudyTime(Long memberId, LocalDate date) {
        Optional<StudyRecord> studyRecordOptional =
                studyRecordRepository.findByDateAndMemberId(date, memberId);
        if (studyRecordOptional.isPresent()) return studyRecordOptional.get().getTime();
        else return 0;
    }

    private LocalDate findTodayDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalTime standardTime = LocalTime.of(5, 0);

        if (now.toLocalTime().isBefore(standardTime)) return now.toLocalDate().minusDays(1);
        else return now.toLocalDate();
    }
}

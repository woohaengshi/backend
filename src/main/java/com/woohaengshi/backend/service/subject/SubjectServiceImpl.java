package com.woohaengshi.backend.service.subject;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.Subject;
import com.woohaengshi.backend.dto.response.studyrecord.ShowTimerResponse;
import com.woohaengshi.backend.dto.response.subject.ShowSubjectsResponse;
import com.woohaengshi.backend.exception.ErrorCode;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.StudyRecordRepository;
import com.woohaengshi.backend.repository.SubjectRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final StudyRecordRepository studyRecordRepository;
    private final SubjectRepository subjectRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public ShowTimerResponse getTimer(Long memberId) {
        validateExistMember(memberId);
        List<ShowSubjectsResponse> subjectsResponses = getSubjectsResponses(memberId);
        int todayStudyTime = getTodayStudyTime(memberId, getTodayDate());
        return ShowTimerResponse.of(todayStudyTime, subjectsResponses);
    }

    private void validateExistMember(Long memberId) {
        if (!memberRepository.existsById(memberId))
            throw new WoohaengshiException(ErrorCode.MEMBER_NOT_FOUND);
    }

    private List<ShowSubjectsResponse> getSubjectsResponses(Long memberId) {
        Stream<Subject> subjectStream = subjectRepository.findAllByMemberIdOrderByNameAsc(memberId);
        return subjectStream
                .map(subject -> ShowSubjectsResponse.of(subject.getId(), subject.getName()))
                .collect(Collectors.toList());
    }

    private int getTodayStudyTime(Long memberId, LocalDate date) {
        return studyRecordRepository
                .findByDateAndMemberId(date, memberId)
                .map(StudyRecord::getTime)
                .orElse(0);
    }

    private LocalDate getTodayDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalTime standardTime = LocalTime.of(5, 0);

        if (now.toLocalTime().isBefore(standardTime)) return now.toLocalDate().minusDays(1);
        else return now.toLocalDate();
    }
}

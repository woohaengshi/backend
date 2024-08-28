package com.woohaengshi.backend.service.timer;

import com.woohaengshi.backend.constant.StandardTimeConstant;
import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.subject.Subject;
import com.woohaengshi.backend.dto.response.timer.ShowTimerResponse;
import com.woohaengshi.backend.exception.ErrorCode;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.SubjectRepository;
import com.woohaengshi.backend.repository.studyrecord.StudyRecordRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TimerServiceImpl implements TimerService {

    private final StudyRecordRepository studyRecordRepository;
    private final SubjectRepository subjectRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public ShowTimerResponse getTimer(Long memberId) {
        validateExistMember(memberId);
        List<Subject> subjects = subjectRepository.findAllByMemberIdAndIsActiveTrue(memberId);
        int todayStudyTime = getTodayStudyTime(memberId, getTodayDate());
        return ShowTimerResponse.of(todayStudyTime, subjects);
    }

    private void validateExistMember(Long memberId) {
        if (!memberRepository.existsById(memberId))
            throw new WoohaengshiException(ErrorCode.MEMBER_NOT_FOUND);
    }

    private int getTodayStudyTime(Long memberId, LocalDate date) {
        return studyRecordRepository
                .findByDateAndMemberId(date, memberId)
                .map(StudyRecord::getTime)
                .orElse(0);
    }

    private LocalDate getTodayDate() {
        LocalDateTime now = LocalDateTime.now();

        if (now.toLocalTime().isBefore(StandardTimeConstant.STANDARD_TIME))
            return now.toLocalDate().minusDays(1);
        return now.toLocalDate();
    }
}

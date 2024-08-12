package com.woohaengshi.backend.service.studyrecord;

import static com.woohaengshi.backend.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.woohaengshi.backend.exception.ErrorCode.SUBJECT_NOT_FOUND;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.StudySubject;
import com.woohaengshi.backend.domain.Subject;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;
import com.woohaengshi.backend.dto.response.studyrecord.ShowMonthlyRecordResponse;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.StudyRecordRepository;
import com.woohaengshi.backend.repository.StudySubjectRepository;
import com.woohaengshi.backend.repository.SubjectRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyRecordServiceImpl implements StudyRecordService {

    private final MemberRepository memberRepository;
    private final StudyRecordRepository studyRecordRepository;
    private final SubjectRepository subjectRepository;
    private final StudySubjectRepository studySubjectRepository;

    @Override
    public void save(SaveRecordRequest request, Long memberId) {
        validateExistMember(memberId);
        Optional<StudyRecord> optionalStudyRecord =
                studyRecordRepository.findByDateAndMemberId(request.getDate(), memberId);
        StudyRecord studyRecord = saveStudyRecord(request, memberId, optionalStudyRecord);
        saveSubjects(request.getSubjects(), studyRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public ShowMonthlyRecordResponse showMonthlyRecord(YearMonth date, Long memberId) {
        validateExistMember(memberId);

        return ShowMonthlyRecordResponse.of(
                date,
                studyRecordRepository.findByYearAndMonthAndMemberId(
                        date.getYear(), date.getMonthValue(), memberId));
    }

    private void validateExistMember(Long memberId) {
        if (!memberRepository.existsById(memberId))
            throw new WoohaengshiException(MEMBER_NOT_FOUND);
    }

    private StudyRecord saveStudyRecord(
            SaveRecordRequest request, Long memberId, Optional<StudyRecord> optionalStudyRecord) {
        if (optionalStudyRecord.isPresent()) {
            StudyRecord studyRecord = optionalStudyRecord.get();
            studyRecord.updateTime(request.getTime());
            return studyRecord;
        }
        return studyRecordRepository.save(request.toStudyRecord(findMemberById(memberId)));
    }

    private void saveSubjects(List<Long> subjects, StudyRecord studyRecord) {
        for (Long subjectId : subjects) {
            if (!studySubjectRepository.existsBySubjectIdAndStudyRecordId(
                    subjectId, studyRecord.getId())) {
                Subject subject = findSubjectById(subjectId);
                studySubjectRepository.save(createStudySubject(studyRecord, subject));
            }
        }
    }

    private Subject findSubjectById(Long subjectId) {
        return subjectRepository
                .findById(subjectId)
                .orElseThrow(() -> new WoohaengshiException(SUBJECT_NOT_FOUND));
    }

    private StudySubject createStudySubject(StudyRecord studyRecord, Subject subject) {
        return StudySubject.builder().subject(subject).studyRecord(studyRecord).build();
    }

    private Member findMemberById(Long memberId) {
        return memberRepository
                .findById(memberId)
                .orElseThrow(() -> new WoohaengshiException(MEMBER_NOT_FOUND));
    }
}

package com.woohaengshi.backend.service;

import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;
import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.Subject;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.exception.ErrorCode;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.StudyRecordRepository;
import com.woohaengshi.backend.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyRecordService {

    private final MemberRepository memberRepository;
    private final StudyRecordRepository studyRecordRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    public void save(SaveRecordRequest request, Long memberId) {
        Optional<StudyRecord> optionalStudyRecord =
                studyRecordRepository.findByDateAndMemberId(request.getDate(), memberId);
        StudyRecord studyRecord = saveStudyRecord(request, memberId, optionalStudyRecord);
        saveSubjects(request.getSubjects(), studyRecord);
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

    private void saveSubjects(List<String> subjects, StudyRecord studyRecord) {
        subjects.forEach(subject -> {
                if(!subjectRepository.existsByNameAndStudyRecordId(subject, studyRecord.getId()))
                subjectRepository.save(createSubject(studyRecord, subject));
        });
    }

    private Subject createSubject(StudyRecord studyRecord, String subject) {
        return Subject.builder()
                .name(subject)
                .studyRecord(studyRecord)
                .member(studyRecord.getMember())
                .build();
    }

    private Member findMemberById(Long memberId) {
        return memberRepository
                .findById(memberId)
                .orElseThrow(() -> new WoohaengshiException(ErrorCode.MEMBER_NOT_FOUND));
    }
}

package com.woohaengshi.backend.service.subject;

import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.subject.Subject;
import com.woohaengshi.backend.dto.request.subject.SubjectRequest;
import com.woohaengshi.backend.exception.ErrorCode;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.SubjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired private SubjectRepository subjectRepository;
    @Autowired private MemberRepository memberRepository;

    public void editSubjects(Long memberId, SubjectRequest requestDTO) {
        validateMemberExist(memberId);

        List<String> addSubjects = requestDTO.getAddSubjects();
        if (addSubjects != null && !addSubjects.isEmpty()) {
            insertSubjects(memberId, addSubjects);
        }

        List<Long> deleteSubjects = requestDTO.getDeleteSubjects();
        if (deleteSubjects != null && !deleteSubjects.isEmpty()) {
            deleteSubjects(deleteSubjects);
        }
    }

    private void validateMemberExist(Long memberId) {
        if (!memberRepository.existsById(memberId))
            throw new WoohaengshiException(ErrorCode.MEMBER_NOT_FOUND);
    }

    private void insertSubjects(Long memberId, List<String> addSubjects) {
        Member member = memberRepository.findById(memberId).get();
        addSubjects.forEach(
                subject -> {
                    validateAlreadyExistSubject(memberId, subject);
                    subjectRepository.save(Subject.builder().name(subject).member(member).build());
                });
    }

    private void deleteSubjects(List<Long> deleteSubjects) {
        deleteSubjects.forEach(
                subjectId -> {
                    validateNotExistSubject(subjectId);
                    subjectRepository.deleteById(subjectId);
                });
    }

    private void validateAlreadyExistSubject(Long member_id, String subject) {
        if (subjectRepository.existsByMemberIdAndName(member_id, subject)) {
            throw new WoohaengshiException(ErrorCode.SUBJECT_ALREADY_EXISTS);
        }
    }

    private void validateNotExistSubject(Long subjectId) {
        if (!subjectRepository.existsById(subjectId)) {
            throw new WoohaengshiException(ErrorCode.SUBJECT_NOT_FOUND);
        }
    }
}

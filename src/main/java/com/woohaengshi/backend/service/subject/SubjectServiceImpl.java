package com.woohaengshi.backend.service.subject;

import static com.woohaengshi.backend.exception.ErrorCode.*;

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
import java.util.Optional;

@Transactional
@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired private SubjectRepository subjectRepository;
    @Autowired private MemberRepository memberRepository;

    public void editSubjects(Long memberId, SubjectRequest requestDTO) {
        validateMemberExist(memberId);

        List<String> addSubjects = requestDTO.getAddedSubjects();
        if (addSubjects != null && !addSubjects.isEmpty()) {
            insertSubjects(memberId, addSubjects);
        }

        List<Long> deleteSubjects = requestDTO.getDeletedSubjects();
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
                subjectName -> {
                    Optional<Subject> subject =
                            subjectRepository.findByMemberIdAndName(memberId, subjectName);
                    validateAlreadyActiveSubject(subject);
                    subject.ifPresent(Subject::active);
                    subject.ifPresentOrElse(
                            Subject::active,
                            () -> subjectRepository.save(createNewSubject(subjectName, member)));
                });
    }

    private Subject createNewSubject(String subjectName, Member member) {
        return Subject.builder().name(subjectName).member(member).isActive(true).build();
    }

    private void deleteSubjects(List<Long> deleteSubjects) {
        deleteSubjects.forEach(
                subjectId -> {
                    Subject subject = findSubjectById(subjectId);
                    validateInActiveSubject(subject);
                    subject.inActive();
                });
    }

    private void validateAlreadyActiveSubject(Optional<Subject> subject) {
        if (subject.isPresent() && subject.get().isActive())
            throw new WoohaengshiException(SUBJECT_ALREADY_EXISTS);
    }

    private void validateInActiveSubject(Subject subject) {
        if (!subject.isActive()) throw new WoohaengshiException(INACTIVE_SUBJECT);
    }

    private Subject findSubjectById(Long subjectId) {
        return subjectRepository
                .findById(subjectId)
                .orElseThrow(() -> new WoohaengshiException(SUBJECT_NOT_FOUND));
    }
}

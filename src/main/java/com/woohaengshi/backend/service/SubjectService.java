package com.woohaengshi.backend.service;

import com.woohaengshi.backend.domain.Subject;
import com.woohaengshi.backend.domain.member.Member;
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
public class SubjectService {

  @Autowired private SubjectRepository subjectRepository;
  @Autowired private MemberRepository memberRepository;

  public void editSubjects(Long memberId, SubjectRequest requestDTO) {
    if (!memberRepository.existsById(memberId))
      throw new WoohaengshiException(ErrorCode.MEMBER_NOT_FOUND);

    List<String> addSubjects = requestDTO.getAddSubjects();
    if (addSubjects != null && !addSubjects.isEmpty()) {
      insertSubjects(memberId, addSubjects);
    }

    List<Long> deleteSubjects = requestDTO.getDeleteSubjects();
    if (deleteSubjects != null && !deleteSubjects.isEmpty()) {
      deleteSubjects(deleteSubjects);
    }
  }

  private void insertSubjects(Long memberId, List<String> addSubjects) {
    Member member = memberRepository.findById(memberId).get();
    addSubjects.stream()
        .forEach(
            s -> {
              validateAlreadyExistSubject(memberId, s);
              subjectRepository.save(Subject.builder().name(s).member(member).build());
            });
  }

  private void deleteSubjects(List<Long> deleteSubjects) {
    deleteSubjects.forEach(
        i -> {
          validateNotExistSubject(i);
          subjectRepository.deleteById(i);
        });
  }

  private void validateAlreadyExistSubject(Long member_id, String s) {
    if (subjectRepository.existsByMemberIdAndName(member_id, s)) {
      throw new WoohaengshiException(ErrorCode.SUBJECT_ALREADY_EXISTS);
    }
  }

  private void validateNotExistSubject(Long i) {
    if (!subjectRepository.existsById(i)) {
      throw new WoohaengshiException(ErrorCode.SUBJECT_NOT_EXISTS);
    }
  }
}

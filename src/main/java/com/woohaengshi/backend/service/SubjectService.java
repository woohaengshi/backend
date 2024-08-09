package com.woohaengshi.backend.service;

import com.woohaengshi.backend.domain.Subject;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.dto.request.SubjectRequestDTO;
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

  public void editSubjects(Long memberId, SubjectRequestDTO requestDTO) {
    if (!memberRepository.existsById(memberId))
      throw new WoohaengshiException(ErrorCode.MEMBER_NOT_FOUND);

    List<String> addSubjects = requestDTO.getAddSubjects();
    if (!addSubjects.isEmpty()) {
      insertSubjects(memberId, addSubjects);
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

  public void deleteSubjects(Long member_id, SubjectRequestDTO requestDTO) {
    List<Long> deleteSubjects = requestDTO.getDeleteSubjects();
    Member member = validateAlreadyExistMember(member_id);
    removeSubjects(member_id, deleteSubjects);
  }

  private void removeSubjects(Long member_id, List<Long> deleteSubjects) {
    deleteSubjects.stream()
            .forEach(
                    i -> {
                      validateNotExistSubject(member_id, i);
                      subjectRepository.deleteById(i);
                    });
  }

  private Member validateAlreadyExistMember(Long member_id) {
    return memberRepository
            .findById(member_id)
            .orElseThrow(() -> new WoohaengshiException(ErrorCode.MEMBER_NOT_FOUND));
  }

  private void validateAlreadyExistSubject(Long member_id, String s) {
    if (subjectRepository.existsByMemberIdAndName(member_id, s)) {
      throw new WoohaengshiException(ErrorCode.SUBJECT_ALREADY_EXISTS);
    }
  }

  private void validateNotExistSubject(Long member_id, Long i) {
    if (!subjectRepository.existsByMemberIdAndId(member_id, i)) {
      throw new WoohaengshiException(ErrorCode.SUBJECT_NOT_EXISTS);
    }
  }
}
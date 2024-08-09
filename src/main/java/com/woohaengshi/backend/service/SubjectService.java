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

  public void saveSubjects(Long member_id, SubjectRequestDTO requestDTO) {
    List<String> addSubjects = requestDTO.getAddSubjects();
    Member member = validateAlreadyExistMember(member_id);
    insertSubjects(member_id, addSubjects, member);
  }

  private void insertSubjects(Long member_id, List<String> addSubjects, Member member) {
    addSubjects.stream()
        .forEach(
            s -> {
              validateAlreadyExistSubject(member_id, s);
              subjectRepository.save(Subject.builder().name(s).member(member).build());
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
}

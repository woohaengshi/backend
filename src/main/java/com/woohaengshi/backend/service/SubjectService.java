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
    Member member = memberRepository.findById(member_id)
            .orElseThrow(() -> new WoohaengshiException(ErrorCode.MEMBER_NOT_FOUND));
  }
}

package com.woohaengshi.backend.service.subject;

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


public interface SubjectService {
    void editSubjects(Long memberId, SubjectRequest requestDTO);
}

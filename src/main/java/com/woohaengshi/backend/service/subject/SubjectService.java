package com.woohaengshi.backend.service.subject;

import com.woohaengshi.backend.dto.request.subject.SubjectRequest;

public interface SubjectService {
    void editSubjects(Long memberId, SubjectRequest requestDTO);
}

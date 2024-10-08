package com.woohaengshi.backend.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.dto.request.subject.SubjectRequest;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.SubjectRepository;
import com.woohaengshi.backend.service.subject.SubjectServiceImpl;
import com.woohaengshi.backend.support.fixture.MemberFixture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock private MemberRepository memberRepository;
    @Mock private SubjectRepository subjectRepository;
    @InjectMocks private SubjectServiceImpl subjectService;

    @Test
    void 과목을_저장한다() {
        // Given
        Member member = MemberFixture.builder().build();
        SubjectRequest request = new SubjectRequest(List.of("Java", "Spring"), List.of());

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        // When
        subjectService.editSubjects(member.getId(), request);

        // Then
        assertAll(
                () ->
                        verify(subjectRepository)
                                .save(
                                        argThat(
                                                subject ->
                                                        subject.getName().equals("Java")
                                                                && subject.getMember()
                                                                        .equals(member))),
                () ->
                        verify(subjectRepository)
                                .save(
                                        argThat(
                                                subject ->
                                                        subject.getName().equals("Spring")
                                                                && subject.getMember()
                                                                        .equals(member))));
    }

    @Test
    void 과목을_삭제한다() {
        // Given
        Member member = MemberFixture.builder().build();

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(subjectRepository.existsById(1L)).willReturn(true);
        given(subjectRepository.existsById(2L)).willReturn(true);
        given(subjectRepository.existsById(3L)).willReturn(true);

        SubjectRequest request = new SubjectRequest(List.of(), List.of(1L, 3L));

        // When
        subjectService.editSubjects(member.getId(), request);

        // Then
        assertAll(
                () -> verify(subjectRepository).deleteById(1L),
                () -> verify(subjectRepository).deleteById(3L),
                () -> verify(subjectRepository, never()).deleteById(2L),
                () -> assertTrue(subjectRepository.existsById(2L)));
    }

    @Test
    void 과목의_추가_삭제를_동시에_한다() {
        // Given
        Member member = MemberFixture.builder().build();

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(subjectRepository.existsById(1L)).willReturn(true);

        SubjectRequest request = new SubjectRequest(List.of("Java", "Spring"), List.of(1L));

        // When
        subjectService.editSubjects(member.getId(), request);

        // Then
        assertAll(
                () ->
                        verify(subjectRepository)
                                .save(argThat(subject -> subject.getName().equals("Java"))),
                () ->
                        verify(subjectRepository)
                                .save(argThat(subject -> subject.getName().equals("Spring"))),
                () -> verify(subjectRepository).deleteById(1L));
    }

    @Test
    void 이미_존재하는_과목이면_예외를_던진다() {
        // Given
        Member member = MemberFixture.builder().build();

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        SubjectRequest request = new SubjectRequest(List.of("Spring", "Java"), List.of());

        // When & Then
        assertThatThrownBy(() -> subjectService.editSubjects(member.getId(), request))
                .isExactlyInstanceOf(WoohaengshiException.class);
    }

    @Test
    void 존재하지_않는_과목이면_예외를_던진다() {
        // Given
        Member member = MemberFixture.builder().build();

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(subjectRepository.existsById(2L)).willReturn(false);

        SubjectRequest request = new SubjectRequest(List.of(), List.of(2L));

        // When $ Then
        assertThatThrownBy(() -> subjectService.editSubjects(member.getId(), request))
                .isExactlyInstanceOf(WoohaengshiException.class);
    }
}

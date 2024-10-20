package com.iodkovskaya.testingfordev.service;

import com.iodkovskaya.testingfordev.entity.DeveloperEntity;
import com.iodkovskaya.testingfordev.exception.DeveloperNotFoundException;
import com.iodkovskaya.testingfordev.exception.DeveloperWithDuplicateEmailException;
import com.iodkovskaya.testingfordev.repository.DeveloperRepository;
import com.iodkovskaya.testingfordev.util.DataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeveloperServiceImplTests {
    //    private DeveloperRepository developerRepository = Mockito.mock(DeveloperRepository.class);
    //    private DeveloperServiceImpl developerService = new DeveloperServiceImpl(developerRepository);
    @Mock
    private DeveloperRepository developerRepository;
    @InjectMocks
    private DeveloperServiceImpl developerService;

    @Test
    public void should_create_developer_with_correct_data() {
        //given
        DeveloperEntity developer = DataUtils.getFirstDeveloperWithoutId();
        BDDMockito.given(developerRepository.findByEmail(anyString()))
                .willReturn(null);
        BDDMockito.given(developerRepository.save(any(DeveloperEntity.class)))
                .willReturn(DataUtils.getFirstDeveloperWithoutId());
        //when
        DeveloperEntity savedDeveloper = developerService.saveDeveloper(developer);
        //then
        assertThat(savedDeveloper).isNotNull();
    }

    @Test
    public void should_throw_exception_when_developer_with_email_already_exists() {
        //given
        DeveloperEntity developer = DataUtils.getFirstDeveloperWithoutId();
        BDDMockito.given(developerRepository.findByEmail(anyString()))
                .willReturn(DataUtils.getFirstDeveloperWithoutId());
        //when
        //Executable e = () -> developerService.saveDeveloper(developer);
        assertThrows(DeveloperWithDuplicateEmailException.class, () -> developerService.saveDeveloper(developer));
        //then
        //assertThrows(DeveloperWithDuplicateEmailException.class, e);
        verify(developerRepository, never()).save(any(DeveloperEntity.class));
    }

    @Test
    public void should_update_developer_when_developer_exists() {
        //given
        DeveloperEntity developer = DataUtils.getFirstDeveloperWithoutId();
        developer.setId(1);
        BDDMockito.given(developerRepository.existsById(anyInt()))
                .willReturn(true);
        BDDMockito.given(developerRepository.save(any(DeveloperEntity.class)))
                .willReturn(developer);
        //when
        DeveloperEntity updatedDeveloper = developerService.updateDeveloper(developer);
        //then
        assertThat(updatedDeveloper).isNotNull();
        verify(developerRepository, times(1)).save(any(DeveloperEntity.class));
    }

    @Test
    public void should_throw_exception_when_wont_to_update_developer_not_exists() {
        //given
        DeveloperEntity developer = DataUtils.getFirstDeveloperWithoutId();
        developer.setId(1);
        BDDMockito.given(developerRepository.existsById(anyInt()))
                .willReturn(false);
        //when
        assertThrows(DeveloperNotFoundException.class, () -> developerService.updateDeveloper(developer));
        //then
        verify(developerRepository, never()).save(any(DeveloperEntity.class));
    }

    @Test
    public void should_find_developer_by_id() {
        //given
        DeveloperEntity developer = DataUtils.getFirstDeveloperWithoutId();
        developer.setId(1);
        BDDMockito.given(developerRepository.findById(anyInt()))
                .willReturn(Optional.of(developer));
        //when
        DeveloperEntity obtainedDeveloper = developerService.getDeveloperById(1);
        //then
        assertThat(obtainedDeveloper).isNotNull();
    }

    @Test
    public void should_throw_exception_when_developer_by_id_not_found() {
        //given
        BDDMockito.given(developerRepository.findById(anyInt()))
                .willThrow(DeveloperNotFoundException.class);
        //when
        assertThrows(DeveloperNotFoundException.class, () -> developerService.getDeveloperById(1));
        //then
    }

    @Test
    public void should_find_developer_by_email() {
        //given
        DeveloperEntity developer = DataUtils.getFirstDeveloperWithoutId();
        developerRepository.save(developer);
        String email = "john.doe@mail.com";
        BDDMockito.given(developerRepository.findByEmail(anyString()))
                .willReturn(developer);
        //when
        DeveloperEntity obtainedDeveloper = developerService.getDeveloperByEmail(email);
        //then
        assertThat(obtainedDeveloper).isNotNull();
    }

    @Test
    public void should_throw_exception_when_developer_with_email_not_exists() {
        //given
        String email = "john.doe@mail.com";
        BDDMockito.given(developerRepository.findByEmail(anyString()))
                .willThrow(DeveloperNotFoundException.class);
        //when
        assertThrows(DeveloperNotFoundException.class, () -> developerService.getDeveloperByEmail(email));
        //then
    }

    @Test
    public void should_find_all_active_developers() {
        //given
        DeveloperEntity developer1 = DataUtils.getFirstDeveloperWithoutId();
        DeveloperEntity developer2 = DataUtils.getSecondDeveloperWithoutId();
        DeveloperEntity developer3 = DataUtils.getThirdDeveloperWithoutId();
        developer1.setId(1);
        developer2.setId(2);
        developer3.setId(3);

        List<DeveloperEntity> developers = List.of(developer1, developer2, developer3);
        BDDMockito.given(developerRepository.findAll())
                .willReturn(developers);
        //when
        List<DeveloperEntity> obtainedDevelopers = developerService.getAllDevelopers();
        //then
        assertThat(CollectionUtils.isEmpty(obtainedDevelopers)).isFalse();
        assertThat(obtainedDevelopers.size()).isEqualTo(2);
    }

    @Test
    public void should_find_all_developers_active_and_with_specialty() {
        //given
        DeveloperEntity developer1 = DataUtils.getFirstDeveloperWithoutId();
        DeveloperEntity developer2 = DataUtils.getSecondDeveloperWithoutId();
        developer1.setId(1);
        developer2.setId(2);

        List<DeveloperEntity> developers = List.of(developer1, developer2);

        BDDMockito.given(developerRepository.findAllActiveBySpecialty(anyString()))
                .willReturn(developers);
        //when
        List<DeveloperEntity> obtainedDevelopers = developerService.getAllActiveBySpecialty("Java");
        //then
        assertThat(CollectionUtils.isEmpty(obtainedDevelopers)).isFalse();
        assertThat(obtainedDevelopers.size()).isEqualTo(2);
    }

    @Test
    public void should_do_soft_deleting_for_developer_by_id() {
        //given
        DeveloperEntity developer = DataUtils.getFirstDeveloperWithoutId();
        developer.setId(1);
        BDDMockito.given(developerRepository.findById(anyInt()))
                .willReturn(Optional.of(developer));
        //when
        developerService.softDeleteById(1);
        //then
        verify(developerRepository, times(1)).save(any(DeveloperEntity.class));
        verify(developerRepository, never()).deleteById(anyInt());
    }

    @Test
    public void should_throw_exception_when_needs_to_do_soft_deleting_but_developer_not_exists() {
        //given
        BDDMockito.given(developerRepository.findById(anyInt()))
                .willReturn(Optional.empty());
        //when
        assertThrows(DeveloperNotFoundException.class, () -> developerService.softDeleteById(1));
        //then
        verify(developerRepository, never()).save(any(DeveloperEntity.class));
    }

    @Test
    public void givenCorrectId_whenHardDeleteById_thenDeleteRepoMethodIsCalled() {
        //given
        DeveloperEntity developer = DataUtils.getFirstDeveloperWithoutId();
        developer.setId(1);
        BDDMockito.given(developerRepository.findById(anyInt()))
                .willReturn(Optional.of(developer));
        //when
        developerService.hardDeleteById(1);
        //then
        verify(developerRepository, times(1)).deleteById(anyInt());
    }


    @Test
    public void givenIncorrectId_whenHardDeleteById_thenExceptionIsThrown() {
        //given
        BDDMockito.given(developerRepository.findById(anyInt()))
                .willReturn(Optional.empty());
        //when
        assertThrows(DeveloperNotFoundException.class, () -> developerService.hardDeleteById(1));
        //then
        verify(developerRepository, never()).deleteById(anyInt());
    }
}

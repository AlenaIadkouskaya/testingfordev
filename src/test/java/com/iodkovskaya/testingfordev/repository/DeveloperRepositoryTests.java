package com.iodkovskaya.testingfordev.repository;

import com.iodkovskaya.testingfordev.entity.DeveloperEntity;
import com.iodkovskaya.testingfordev.util.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeveloperRepositoryTests {
    @Autowired
    private DeveloperRepository developerRepository;

    @BeforeEach
    public void setUp() {
        developerRepository.deleteAll();
    }

    @Test
    public void should_create_developer_with_correct_fields() {
        //given
        DeveloperEntity developerToSave = DataUtils.getFirstDeveloperWithoutId();

        //when
        DeveloperEntity savedDeveloper = developerRepository.save(developerToSave);
        //then
        assertThat(savedDeveloper).isNotNull();
        assertThat(savedDeveloper.getId()).isNotNull();
    }

    @Test
    public void should_update_email_for_developer_with_success() {
        //given
        String updetedEmail = "updated@gmail.com";
        DeveloperEntity developerToSave = DataUtils.getFirstDeveloperWithoutId();
        developerRepository.save(developerToSave);
        //when
        DeveloperEntity developerToUpdate = developerRepository.findById(developerToSave.getId())
                .orElse(null);
        developerToUpdate.setEmail(updetedEmail);
        DeveloperEntity developerUpdated = developerRepository.save(developerToUpdate);
        //then
        assertThat(developerUpdated).isNotNull();
        assertThat(developerUpdated.getEmail()).isEqualTo(updetedEmail);
    }

    @Test
    public void should_get_developer_by_id() {
        //given
        DeveloperEntity developerToSave = DataUtils.getFirstDeveloperWithoutId();
        developerRepository.save(developerToSave);
        //when
        DeveloperEntity foundedDeveloper = developerRepository.findById(developerToSave.getId())
                .orElse(null);
        //then
        assertThat(foundedDeveloper).isNotNull();
        assertThat(foundedDeveloper.getEmail()).isEqualTo("john.doe@gmail.com");
    }

    @Test
    public void should_get_exception_when_developer_with_input_id_not_exists() {
        //given
        //when
        DeveloperEntity developerEntity = developerRepository.findById(1).orElse(null);
        //then
        assertThat(developerEntity).isNull();
    }

    @Test
    public void should_get_all_developers() {
        //given
        DeveloperEntity developerFirst = DataUtils.getFirstDeveloperWithoutId();
        DeveloperEntity developerSecond = DataUtils.getSecondDeveloperWithoutId();
        DeveloperEntity developerThird = DataUtils.getThirdDeveloperWithoutId();

        developerRepository.saveAll(List.of(developerFirst, developerSecond, developerThird));
        //when
        List<DeveloperEntity> allDevelopers = developerRepository.findAll();
        //then
        assertThat(CollectionUtils.isEmpty(allDevelopers)).isFalse();
    }

    @Test
    public void should_find_developer_by_email() {
        //given
        DeveloperEntity developer = DataUtils.getFirstDeveloperWithoutId();
        developerRepository.save(developer);
        //when
        DeveloperEntity foundedDeveloper = developerRepository.findByEmail(developer.getEmail());
        //then
        assertThat(foundedDeveloper).isNotNull();
        assertThat(foundedDeveloper.getEmail()).isEqualTo(developer.getEmail());
    }

    @Test
    public void should_find_only_active_and_with_input_status_developers() {
        //given
        DeveloperEntity developerFirst = DataUtils.getFirstDeveloperWithoutId();
        DeveloperEntity developerSecond = DataUtils.getSecondDeveloperWithoutId();
        DeveloperEntity developerThird = DataUtils.getThirdDeveloperWithoutId();

        developerRepository.saveAll(List.of(developerFirst, developerSecond, developerThird));
        //when
        List<DeveloperEntity> listDevelopers = developerRepository.findAllActiveBySpecialty("Java");
        //then
        assertThat(CollectionUtils.isEmpty(listDevelopers)).isFalse();
        assertThat(listDevelopers.size()).isEqualTo(2);
    }

    @Test
    public void should_return_exception_when_find_by_id_and_developer_is_deleted() {
        //given
        DeveloperEntity developer = DataUtils.getFirstDeveloperWithoutId();
        developerRepository.save(developer);
        //when
        developerRepository.deleteById(1);
        //then
        DeveloperEntity developerFounded = developerRepository.findById(developer.getId()).orElse(null);
        assertThat(developerFounded).isNull();
    }
}

package com.iodkovskaya.testingfordev.service;

import com.iodkovskaya.testingfordev.entity.DeveloperEntity;
import com.iodkovskaya.testingfordev.entity.Status;
import com.iodkovskaya.testingfordev.exception.DeveloperNotFoundException;
import com.iodkovskaya.testingfordev.exception.DeveloperWithDuplicateEmailException;
import com.iodkovskaya.testingfordev.repository.DeveloperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DeveloperServiceImpl implements DeveloperService {
    private final DeveloperRepository developerRepository;

    @Override
    public DeveloperEntity saveDeveloper(DeveloperEntity developer) {
        DeveloperEntity duplicateCandidate = developerRepository.findByEmail(developer.getEmail());

        if (Objects.nonNull(duplicateCandidate)) {
            throw new DeveloperWithDuplicateEmailException("Developer with defined email is already exist");
        }
        return developerRepository.save(developer);
    }

    @Override
    public DeveloperEntity updateDeveloper(DeveloperEntity developer) {
        boolean isExists = developerRepository.existsById(developer.getId());

        if (!isExists) {
            throw new DeveloperNotFoundException("Developer not found");
        }
        return developerRepository.save(developer);
    }

    @Override
    public DeveloperEntity getDeveloperById(Integer id) {
        return developerRepository.findById(id)
                .orElseThrow(() -> new DeveloperNotFoundException("Developer not found"));
    }

    @Override
    public DeveloperEntity getDeveloperByEmail(String email) {
        DeveloperEntity obtainedDeveloper = developerRepository.findByEmail(email);

        if (Objects.isNull(obtainedDeveloper)) {
            throw new DeveloperNotFoundException("Developer not found");
        }
        return obtainedDeveloper;
    }


    @Override
    public List<DeveloperEntity> getAllDevelopers() {
        return developerRepository.findAll()
                .stream().filter(d -> {
                    return d.getStatus().equals(Status.ACTIVE);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DeveloperEntity> getAllActiveBySpecialty(String specialty) {
        return developerRepository.findAllActiveBySpecialty(specialty);
    }

    @Override
    public void softDeleteById(Integer id) {
        DeveloperEntity developer = developerRepository.findById(id)
                .orElseThrow(() -> new DeveloperNotFoundException("Developer not found"));
        developer.setStatus(Status.DELETED);
        developerRepository.save(developer);
    }

    @Override
    public void hardDeleteById(Integer id) {
        developerRepository.findById(id)
                .orElseThrow(() -> new DeveloperNotFoundException("Developer not found"));
        developerRepository.deleteById(id);
    }

}

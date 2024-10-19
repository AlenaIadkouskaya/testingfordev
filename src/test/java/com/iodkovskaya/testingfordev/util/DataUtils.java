package com.iodkovskaya.testingfordev.util;

import com.iodkovskaya.testingfordev.entity.DeveloperEntity;
import com.iodkovskaya.testingfordev.entity.Status;

public class DataUtils {
    public static DeveloperEntity getFirstDeveloperWithoutId() {
        return DeveloperEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .specialty("Java")
                .status(Status.ACTIVE)
                .build();
    }

    public static DeveloperEntity getSecondDeveloperWithoutId() {
        return DeveloperEntity.builder()
                .firstName("Frank")
                .lastName("Jones")
                .email("frank.jones@mail.com")
                .specialty("Java")
                .status(Status.DELETED)
                .build();
    }

    public static DeveloperEntity getThirdDeveloperWithoutId() {
        return DeveloperEntity.builder()
                .firstName("Mike")
                .lastName("Smith")
                .email("mike.smith@mail.com")
                .specialty("Java")
                .status(Status.ACTIVE)
                .build();
    }

}

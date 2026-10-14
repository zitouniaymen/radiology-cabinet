package com.cabinet.radiology.exam.service;

import com.cabinet.radiology.exam.repository.ExamRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class ExamReferenceGenerator {

    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 8;
    private static final int MAX_ATTEMPTS = 10;

    private final ExamRepository examRepository;
    private final SecureRandom random = new SecureRandom();

    public ExamReferenceGenerator(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public String next() {
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            String candidate = "EXM-" + randomCode();
            if (!examRepository.existsByReference(candidate)) {
                return candidate;
            }
        }
        throw new IllegalStateException("Unable to generate a unique exam reference");
    }

    private String randomCode() {
        StringBuilder builder = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            builder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return builder.toString();
    }
}

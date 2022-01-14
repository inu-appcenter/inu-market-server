package inu.market.major.service;

import inu.market.major.domain.Major;
import inu.market.major.domain.MajorRepository;
import inu.market.major.dto.MajorCreateRequest;
import inu.market.major.dto.MajorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MajorService {

    private final MajorRepository majorRepository;

    @Transactional
    public MajorResponse create(MajorCreateRequest request) {

        if (majorRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException(request.getName() + "는 이미 존재하는 학과입니다.");
        }

        Major major = Major.createMajor(request.getName());
        majorRepository.save(major);

        return MajorResponse.from(major);
    }
}

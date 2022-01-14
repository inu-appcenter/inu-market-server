package inu.market.major.service;

import inu.market.major.domain.Major;
import inu.market.major.domain.MajorRepository;
import inu.market.major.dto.MajorCreateRequest;
import inu.market.major.dto.MajorResponse;
import inu.market.major.dto.MajorUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public MajorResponse update(Long majorId, MajorUpdateRequest request) {
        Major findMajor = majorRepository.findById(majorId)
                .orElseThrow(() -> new RuntimeException(majorId + "는 존재하지 않는 학과 ID 입니다."));

        if (majorRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException(request.getName() + "는 이미 존재하는 학과입니다.");
        }

        findMajor.changeName(request.getName());
        return MajorResponse.from(findMajor);
    }

    @Transactional
    public void delete(Long majorId) {
        Major findMajor = majorRepository.findById(majorId)
                .orElseThrow(() -> new RuntimeException(majorId + "는 존재하지 않는 학과 ID 입니다."));

        majorRepository.delete(findMajor);
    }

    public List<MajorResponse> findAll() {
        List<Major> majors = majorRepository.findAll();
        return majors.stream()
                .map(major -> MajorResponse.from(major))
                .collect(Collectors.toList());
    }
}

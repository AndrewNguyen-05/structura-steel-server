package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.partnerservice.dto.request.PartnerProjectRequestDto;
import com.structura.steel.partnerservice.dto.response.PartnerProjectResponseDto;
import com.structura.steel.partnerservice.entity.Partner;
import com.structura.steel.partnerservice.entity.PartnerProject;
import com.structura.steel.partnerservice.client.ProductFeignClient;
import com.structura.steel.partnerservice.mapper.PartnerProjectMapper;
import com.structura.steel.partnerservice.repository.PartnerProjectRepository;
import com.structura.steel.partnerservice.repository.PartnerRepository;
import com.structura.steel.partnerservice.service.PartnerProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PartnerProjectServiceImpl implements PartnerProjectService {

    private final PartnerProjectRepository partnerProjectRepository;
    private final PartnerRepository partnerRepository;
    private final PartnerProjectMapper partnerProjectMapper;
    private final ProductFeignClient productFeignClient; // Để call sang product service

    @Override
    public PartnerProjectResponseDto createPartnerProject(PartnerProjectRequestDto dto) {
        Partner partner = partnerRepository.findById(dto.partnerId())
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + dto.partnerId()));

        PartnerProject project = partnerProjectMapper.toPartnerProject(dto);
        project.setPartner(partner);

        // Ở DB ta không thể lưu List<Product> vì Product ở service khác,
        // ta có thể lưu chuỗi productIds. Ví dụ:
        if (dto.productIds() != null && !dto.productIds().isEmpty()) {
            // Convert List<Long> -> "1,2,3" (đơn giản)
            String productIdsStr = dto.productIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            // Ta dùng tạm cột 'extraInfo' hoặc cột 'productIds' (tự thêm) để lưu trữ
            // Ở đây ví dụ thêm cột "productIds" ở PartnerProject:
            // project.setProductIds(productIdsStr);
        }

        PartnerProject saved = partnerProjectRepository.save(project);

        return partnerProjectMapper.toPartnerProjectResponseDto(saved);
    }

    @Override
    public PartnerProjectResponseDto updatePartnerProject(Long id, PartnerProjectRequestDto dto) {
        PartnerProject existing = partnerProjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PartnerProject not found with id: " + id));

        partnerProjectMapper.updatePartnerProjectFromDto(dto, existing);
        // Lưu ý nếu dto.getPartnerId() thay đổi, cần set lại partner:
        if (dto.partnerId() != null) {
            Partner partner = partnerRepository.findById(dto.partnerId())
                    .orElseThrow(() -> new RuntimeException("Partner not found with id: " + dto.partnerId()));
            existing.setPartner(partner);
        }

        // Tương tự, xử lý productIds
//        if (dto.productIds() != null) {
//            // Convert sang chuỗi
//            String productIdsStr = dto.productIds().stream()
//                    .map(String::valueOf)
//                    .collect(Collectors.joining(","));
//            existing.setProductIds(productIdsStr);
//        }

        PartnerProject updated = partnerProjectRepository.save(existing);

        return partnerProjectMapper.toPartnerProjectResponseDto(updated);
    }

    @Override
    public PartnerProjectResponseDto getPartnerProject(Long id) {
        PartnerProject project = partnerProjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PartnerProject not found with id: " + id));
        return partnerProjectMapper.toPartnerProjectResponseDto(project);
    }

    @Override
    public void deletePartnerProject(Long id) {
        PartnerProject project = partnerProjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PartnerProject not found with id: " + id));
        partnerProjectRepository.delete(project);
    }

    @Override
    public List<PartnerProjectResponseDto> getAllPartnerProjects() {
        List<PartnerProject> projects = partnerProjectRepository.findAll();
        return partnerProjectMapper.toPartnerProjectResponseDtoList(projects);
    }
}

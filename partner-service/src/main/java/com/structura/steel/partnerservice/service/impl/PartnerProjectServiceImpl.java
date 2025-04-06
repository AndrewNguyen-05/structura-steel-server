package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.dto.request.PartnerProjectRequestDto;
import com.structura.steel.dto.response.PartnerProjectResponseDto;
import com.structura.steel.dto.response.ProductResponseDto;
import com.structura.steel.partnerservice.client.ProductFeignClient;
import com.structura.steel.partnerservice.entity.Partner;
import com.structura.steel.partnerservice.entity.PartnerProject;
import com.structura.steel.partnerservice.mapper.PartnerProjectMapper;
import com.structura.steel.partnerservice.repository.PartnerProjectRepository;
import com.structura.steel.partnerservice.repository.PartnerRepository;
import com.structura.steel.partnerservice.service.PartnerProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PartnerProjectServiceImpl implements PartnerProjectService {

    private final PartnerProjectRepository partnerProjectRepository;
    private final PartnerRepository partnerRepository;
    private final PartnerProjectMapper partnerProjectMapper;

    private final ProductFeignClient productFeignClient;

    @Override
    public PartnerProjectResponseDto createPartnerProject(Long partnerId, PartnerProjectRequestDto dto) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + partnerId));

        PartnerProject project = partnerProjectMapper.toPartnerProject(dto);
        project.setPartner(partner);
        PartnerProject saved = partnerProjectRepository.save(project);

        return entityToResponseWithProduct(saved);
    }

    @Override
    public PartnerProjectResponseDto updatePartnerProject(Long partnerId, Long projectId, PartnerProjectRequestDto dto) {
        // Xác thực Partner
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + partnerId));
        PartnerProject existing = partnerProjectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("PartnerProject not found with id: " + projectId));

        // Kiểm tra xem project có thuộc partnerId không (nếu cần)
        if (!existing.getPartner().getId().equals(partnerId)) {
            throw new RuntimeException("PartnerProject id " + projectId + " không thuộc Partner id " + partnerId);
        }

        partnerProjectMapper.updatePartnerProjectFromDto(dto, existing);
        PartnerProject updated = partnerProjectRepository.save(existing);
        return entityToResponseWithProduct(updated);
    }

    @Override
    public PartnerProjectResponseDto getPartnerProject(Long partnerId, Long projectId) {
        // Xác thực Partner
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + partnerId));
        PartnerProject project = partnerProjectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("PartnerProject not found with id: " + projectId));
        if (!project.getPartner().getId().equals(partnerId)) {
            throw new RuntimeException("PartnerProject id " + projectId + " not belong to Partner id " + partnerId);
        }
        return entityToResponseWithProduct(project);
    }

    @Override
    public void deletePartnerProject(Long partnerId, Long projectId) {
        // Xác thực Partner
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + partnerId));
        PartnerProject project = partnerProjectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("PartnerProject not found with id: " + projectId));
        if (!project.getPartner().getId().equals(partnerId)) {
            throw new RuntimeException("PartnerProject id " + projectId + " không thuộc Partner id " + partnerId);
        }
        partnerProjectRepository.delete(project);
    }

    @Override
    public List<PartnerProjectResponseDto> getAllPartnerProjectsByPartnerId(Long partnerId) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + partnerId));
        List<PartnerProject> projects = partner.getPartnerProjects();
        List<PartnerProjectResponseDto> partnerProjectResponseDtoList = projects.stream()
                .map(partnerProjectMapper::toPartnerProjectResponseDto).toList();

        for(int i = 0; i < projects.size(); i++) {
            List<Long> productIds = projects.get(i).getProductIds();
            List<ProductResponseDto> products = new ArrayList<>();
            if (productIds != null) {
                for (Long productId : productIds) {
                    ProductResponseDto product = productFeignClient.getProductById(productId);
                    products.add(product);
                }
            }
            partnerProjectResponseDtoList.get(i).setProducts(products);
        }

        return partnerProjectResponseDtoList;
    }

    private PartnerProjectResponseDto entityToResponseWithProduct(PartnerProject project) {
        PartnerProjectResponseDto responseDto = partnerProjectMapper.toPartnerProjectResponseDto(project);

        List<Long> productIds = project.getProductIds();
        List<ProductResponseDto> products = new ArrayList<>();
        if (productIds != null) {
            for (Long productId : productIds) {
                ProductResponseDto product = productFeignClient.getProductById(productId);
                products.add(product);
            }
        }
        responseDto.setProducts(products);

        return responseDto;
    }
}

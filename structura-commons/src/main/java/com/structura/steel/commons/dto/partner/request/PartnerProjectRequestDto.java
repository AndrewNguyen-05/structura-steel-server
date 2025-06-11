package com.structura.steel.commons.dto.partner.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record PartnerProjectRequestDto (

		Long partnerId,

		@NotBlank(message = "Project name is required")
    	String projectName,

		@NotBlank(message = "Project address must not be blank")
		String projectAddress,
		String projectRepresentative,

		@Pattern(
				regexp = "^(0|\\+84)(\\d{9})$",
				message = "Phone number not valid"
		)
		String projectRepresentativePhone,
		String contactPerson,

		@Pattern(
				regexp = "^(0|\\+84)(\\d{9})$",
				message = "Phone number not valid"
		)
		String contactPersonPhone,
		String address,

    	List<Long> productIds
) {}

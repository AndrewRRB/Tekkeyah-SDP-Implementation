package com.tekkeyah.model.DTO;

/**
 * ============================================================================
 * PATTERN : DTO (Data Transfer Object)
 * PART    : What a beneficiary sends to add something they need
 * ----------------------------------------------------------------------------
 * HOW IT CONNECTS
 *   POST /api/users/{id}/beneficiary/needs -> UserService.addNeed(...) ->
 *   BeneficiaryProfile.addNeed(...).
 * ============================================================================
 */
public record NeedRequest(String need) {
}

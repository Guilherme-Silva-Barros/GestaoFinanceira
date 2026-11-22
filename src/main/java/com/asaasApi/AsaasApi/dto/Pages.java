package com.asaasApi.AsaasApi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Pages<T>(Boolean hasMore, int totalCount, int limit, int offset, List<T> data) {
}

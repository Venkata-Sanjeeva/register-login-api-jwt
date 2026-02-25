package com.auth.auth_service.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GlobalRequest<T> {
	private T body;
}

package com.stream.app.spring_stream_backend.Controller.playload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CustomMessage {
	
	private String message;
    private boolean success = false;
}

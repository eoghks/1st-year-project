package dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultDto<T> {

	private T data;

}

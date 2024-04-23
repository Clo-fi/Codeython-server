package clofi.codeython.socket.controller.response;

public record DataResponse<T>(T data, DataType type) {
}

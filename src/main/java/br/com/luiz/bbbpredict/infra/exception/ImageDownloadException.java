package br.com.luiz.bbbpredict.infra.exception;

public class ImageDownloadException extends RuntimeException {
    public ImageDownloadException(String message) {
        super(message);
    }
}
